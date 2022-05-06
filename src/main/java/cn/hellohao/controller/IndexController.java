package cn.hellohao.controller;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.*;
import cn.hellohao.utils.verifyCode.IVerifyCodeGen;
import cn.hellohao.utils.verifyCode.SimpleCharVerifyCodeGenImpl;
import cn.hellohao.utils.verifyCode.VerifyCode;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private ImgService imgService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    IRedisService iRedisService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private  UploadServicel uploadServicel;
    @Autowired
    private deleImages deleimages;
    @Autowired
    AlbumServiceImpl albumService;


    @RequestMapping(value = "/webInfo")
    @ResponseBody
    public Msg webInfo() {
        final Msg msg = new Msg();
        Config config = configService.getSourceype();
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        SysConfig sysConfig = sysConfigService.getstate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("webname",config.getWebname());
        jsonObject.put("websubtitle",config.getWebsubtitle());
        jsonObject.put("keywords",config.getWebkeywords());
        jsonObject.put("description",config.getWebms());
        jsonObject.put("explain",config.getExplain());
        jsonObject.put("favicon",config.getWebfavicons());
        jsonObject.put("baidu",config.getBaidu());
        jsonObject.put("links",config.getLinks());
        jsonObject.put("aboutinfo",config.getAboutinfo());
        jsonObject.put("logo",config.getLogo());
        jsonObject.put("api",updateConfig.getApi());
        jsonObject.put("register",sysConfig.getRegister());
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping(value = "/upload")// new
    @ResponseBody
    public Msg upimg(HttpServletRequest request,HttpSession httpSession
            , @RequestParam(value = "file", required = false) MultipartFile multipartFile,Integer day) {
        return uploadServicel.uploadForLoc(request,multipartFile,day,null);
    }

    @PostMapping(value = "/uploadForUrl") //new
    @ResponseBody
    public Msg upurlimg(HttpServletRequest request,@RequestParam(value = "data", defaultValue = "") String data) {
        JSONObject jsonObj = JSONObject.parseObject(data);
        Integer setday = jsonObj.getInteger("day");
        String imgUrl = jsonObj.getString("imgUrl");
        String[] URLArr = imgUrl.split("\n");

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        int syscounts = 0;
        if(null==user){
            syscounts = updateConfig.getImgcounttourists();
        }else{
            syscounts = updateConfig.getImgcountuser();
        }
        Msg retMsg = new Msg();
        JSONObject jsonObject = new JSONObject();
        JSONArray retArray = new JSONArray();
        int errcounts = 0;
        int excess = 0;
        for (int i = 0; i < URLArr.length; i++) {
            int temp = i+1;
            if(syscounts>=temp){
                Msg msg = uploadServicel.uploadForLoc(request, null, setday, URLArr[i]);
                if(!msg.getCode().equals("200")){
                    errcounts++;
                }else{
                    retArray.add(msg);
                }
            }else{
                excess++;
            }
        }
        jsonObject.put("counts",URLArr.length);
        jsonObject.put("errcounts",errcounts);
        jsonObject.put("excess",excess);
        jsonObject.put("urls",retArray);
        retMsg.setData(jsonObject);
        return retMsg;
    }

    @RequestMapping(value = "/getUploadInfo")//new
    @ResponseBody
    public Msg getUploadInfo() {
        Msg msg = new Msg();
        JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        try {
            UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
            jsonObject.put("suffix",updateConfig.getSuffix().split(","));
            if(null==user){
                jsonObject.put("filesize",Integer.valueOf(updateConfig.getFilesizetourists())/1024);
                jsonObject.put("imgcount",updateConfig.getImgcounttourists());
                jsonObject.put("uploadSwitch",updateConfig.getIsupdate());
                jsonObject.put("uploadInfo","您登陆后才能使用此功能哦");
            }else{
                jsonObject.put("filesize",Integer.valueOf(updateConfig.getFilesizeuser())/1024);
                jsonObject.put("imgcount",updateConfig.getImgcountuser());
                jsonObject.put("uploadSwitch",updateConfig.getUserclose());
                jsonObject.put("uploadInfo","系统暂时关闭了用户上传功能");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        msg.setData(jsonObject);
        return msg;
    }

    @RequestMapping("/checkStatus")
    @ResponseBody
    public Msg checkStatus(HttpServletRequest request) {
        Msg msg = new Msg();
        String token = request.getHeader("Authorization");
        if(token != null) {
            JSONObject tokenJson = JWTUtil.checkToken(token);
            if(tokenJson.getBoolean("check")){
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken tokenOBJ = new UsernamePasswordToken(tokenJson.getString("email"),tokenJson.getString("password"));
                tokenOBJ.setRememberMe(true);
                try {
                    subject.login(tokenOBJ);
                    SecurityUtils.getSubject().getSession().setTimeout(3600000);
                    User u = (User) subject.getPrincipal();
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("RoleLevel",u.getLevel()==2?"admin":"user");
                    jsonObject.put("userName",u.getUsername());
                    msg.setCode("200");
                    msg.setData(jsonObject);
                } catch (Exception e) {
                    msg.setCode("40041");
                    msg.setInfo("登录失效，请重新登录");
                    System.err.println("登录失效，请重新登录");
                    e.printStackTrace();
                }
            }else{
                msg.setCode("40041");
                msg.setInfo("登录失效，请重新登录");
            }
        }else{
            msg.setCode("40040");
            msg.setInfo("当前未登录，请先登录");
        }
        return msg;
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 38);
            String code = verifyCode.getCode();
            String userIP = GetIPS.getIpAddr(request);
            iRedisService.setValue(userIP+"_hellohao_verifyCode",code);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/verifyCodeForRegister")
    public void verifyCodeForRegister(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 38);
            String code = verifyCode.getCode();
            String userIP = GetIPS.getIpAddr(request);
            iRedisService.setValue(userIP+"_hellohao_verifyCodeForRegister",code);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/verifyCodeForRetrieve")
    public void verifyCodeForRetrieve(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 38);
            String code = verifyCode.getCode();
            System.out.println("verifyCodeForRetrieve-zhaoHui httpSession ID==="+httpSession.getId());
            String userIP = GetIPS.getIpAddr(request);
            iRedisService.setValue(userIP+"_hellohao_verifyCodeForEmailRetrieve",code);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/deleImagesByUid") //new
    @ResponseBody
    public Msg deleImagesByUid(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        String imguid = jsonObj.getString("imguid");
        Images image = imgService.selectImgUrlByImgUID(imguid);
        if(null==image){
            msg.setInfo("图像已不存在，删除成功");
            return msg;
        }
        //判断是不是自己的图片 因为这里的删除功能只能是首页上传后才会调用的接口  能删除的图片只能是自己的
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if(null!=user){
            //由于integer类型的==比较方式适用于-128~127之间，所以超过这个范围就会不对
//            if(user.getId()!=image.getUserid()){
            if(!user.getId().equals(image.getUserid())){
                msg.setInfo("删除失败，该图片不允许你执行操作");
                msg.setCode("100403");
                return msg;
            }
        }
        msg = deleimages.dele(null,image.getId());
        List<Long> Delids = (List<Long>)msg.getData();
        if(!Delids.contains(image.getId())){
            msg.setCode("500");
            msg.setInfo("图像删除失败");
        }else{
            msg.setCode("200");
            msg.setInfo("图像已成功删除");
        }
        return msg;
    }

    @RequestMapping("/authError")
    @ResponseBody
    public Msg authError(HttpServletRequest request){
        Msg msg = new Msg();
        msg.setCode("4031");
        msg.setInfo("You don't have authority");
        return msg;
    }

    @RequestMapping("/jurisError")
    @ResponseBody
    public Msg jurisError(HttpServletRequest request){
        Msg msg = new Msg();
        msg.setCode("4031");
        msg.setInfo("Authentication request failed");
        return msg;
    }



}
