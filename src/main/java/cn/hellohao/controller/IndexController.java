package cn.hellohao.controller;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.*;
import cn.hellohao.utils.verifyCode.IVerifyCodeGen;
import cn.hellohao.utils.verifyCode.SimpleCharVerifyCodeGenImpl;
import cn.hellohao.utils.verifyCode.VerifyCode;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    IRedisService iRedisService;
    @Autowired
    private NOSImageupload nOSImageupload;
    @Autowired
    private UserService userService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private USSImageupload ussImageupload;
    @Autowired
    private KODOImageupload kodoImageupload;
    @Autowired
    private COSImageupload cosImageupload;
    @Autowired
    private FTPImageupload ftpImageupload;
    @Autowired
    private ImgService imgService;
    @Autowired
    private  UploadServicel uploadServicel;

    private String[] iparr;

    public static String vu;



    @RequestMapping(value = "/webInfo")//upimg new
    @ResponseBody
    public Msg webInfo(HttpSession httpSession) {
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
//        jsonObject.put("watermark",updateConfig.getWatermark());
//        jsonObject.put("guidepage",sysConfig.getGuidepage());
        jsonObject.put("register",sysConfig.getRegister());
        msg.setData(jsonObject);
        return msg;
    }


    @PostMapping(value = "/upload")//upimg new
    @ResponseBody
    public Msg upimg(HttpServletRequest request,HttpSession httpSession
            , @RequestParam(value = "file", required = false) MultipartFile multipartFile,Integer day,
                     @RequestParam(value = "classifications", defaultValue = "" ) String classifications) {
        final JSONArray jsonArray = new JSONArray();
        if(!classifications.equals("")){
            String[] calssif = classifications.split(",");
            for (int i = 0; i < calssif.length; i++) {
                jsonArray.add(calssif[i]);
            }
        }
        return uploadServicel.uploadForLoc(request,multipartFile,day,null,jsonArray);
    }


    @RequestMapping("/sentence")
    @ResponseBody
    public String sentence(HttpSession session, Integer id) {
        JSONArray jsonArray = new JSONArray();
        String text = Sentence.getURLContent();
        jsonArray.add(text);
        return jsonArray.toString();
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        msg.setData(jsonObject);
        return msg;
    }


    @RequestMapping("/checkStatus")
    @ResponseBody
    public Msg checkStatus( HttpSession httpSession,HttpServletRequest request) {
        Msg msg = new Msg();
        String token = request.getHeader("Authorization");
        if(token != null) {
            JSONObject tokenJson = JWTUtil.checkToken(token);
//            boolean check = (boolean) result.get("check");
//            String password = (String) result.get("password");
            if(tokenJson.getBoolean("check")){
                //获取当前用户
                Subject subject = SecurityUtils.getSubject();
                //封装用户的登录数据（shiro认证的时候使用）
                UsernamePasswordToken tokenOBJ = new UsernamePasswordToken(tokenJson.getString("email"),tokenJson.getString("password"));
                //设置记住我
                tokenOBJ.setRememberMe(true);
                try {
                    //执行登录方法，如果没有异常，说明登录成功
                    subject.login(tokenOBJ);
                    SecurityUtils.getSubject().getSession().setTimeout(3600000);//一小时
                    User u = (User) subject.getPrincipal();
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("RoleLevel",u.getLevel()==2?"admin":"user");
                    jsonObject.put("userName",u.getUsername());
                    msg.setCode("200");
                    msg.setData(jsonObject);
                } catch (Exception e) {
                    //此异常说明用户名不存在
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
            //登录页面验证码
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

    @GetMapping(value = "/images/{id}")
    @ResponseBody
    public Images selectByFy(@PathVariable("id") Integer id) {
        return imgService.selectByPrimaryKey(id);
    }


    @RequestMapping("/{key1}/TOIMG{key2}N.{key3}")
    public ResponseEntity<Object> selectByFyOne(final HttpServletRequest request,
                                                HttpServletResponse response,
                                                @PathVariable("key1") String key1, @PathVariable("key2") String key2,
                                                @PathVariable("key3") String key3, Model model){
        MediaType mediaType =null;
        File file = new File(File.separator+"HellohaoData"+File.separator+key1+"/TOIMG"+key2+"N."+key3);
        if (!file.exists()) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>404 FILE NOT FOUND</h1>");
            }
        }
        if(key3.equals("png")){
            mediaType = MediaType.IMAGE_PNG;
        }else if(key3.equals("gif")){
            mediaType = MediaType.IMAGE_GIF;
        }else{
            mediaType = MediaType.IMAGE_JPEG;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        // headers.setContentDispositionFormData("attachment", URLUtil.encode(file.getName()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(new FileSystemResource(file));

        //return "forward:/links/"+key1+"/TOIMG"+key2+"N."+key3;
    }

    // @RequestMapping("/{key1:\\d+}/{key2}/{key3}/TOIMG{key4}N.{key5}")
    public void selectByFy2(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("key1") String key1,@PathVariable("key2") String key2,
                            @PathVariable("key3") String key3,@PathVariable("key4") String key4,
                            @PathVariable("key5") String key5,Model model) {
        String head = "jpg";
        if(key5.equals("jpg")||key5.equals("jpeg")){
            head = "jpeg";
        }else if(key5.equals("png")){
            head = "png";
        }else if(key5.equals("bmp")){
            head = "bmp";
        }else if(key5.equals("gif")){
            head = "gif";
        }else{
            head = key5;
        }
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/"+head);
        //InputStream is= null;
        BufferedImage bi=null;
        try {
            //is = new FileInputStream(new File(File.separator+"HellohaoData"+File.separator+key1+"/"+key2+"/"+key3+"/TOIMG"+key4+"N."+key5));
            bi= ImageIO.read(new File(File.separator+"HellohaoData"+File.separator+key1+"/"+key2+"/"+key3+"/TOIMG"+key4+"N."+key5));
            //is.close();
            //将图片输出给浏览器
            BufferedImage image = (bi) ;
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, head, os);
        } catch (Exception e) {
            Print.warning("寻找本地文件出错："+e.getMessage());
            e.printStackTrace();
            try {
                response.sendRedirect("/404");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @GetMapping("/{key1:\\d+}/{key2}/{key3}/TOIMG{key4}N.{key5}")
    @ResponseBody
    public ResponseEntity<Object> selectByFyTow(final HttpServletRequest request,
                                                HttpServletResponse response,
                                                @PathVariable("key1") String key1,
                                                @PathVariable("key2") String key2,
                                                @PathVariable("key3") String key3,
                                                @PathVariable("key4") String key4,
                                                @PathVariable("key5") String key5) {
        MediaType mediaType =null;
        File file = new File(File.separator+"HellohaoData"+File.separator+key1+"/"+key2+"/"+key3+"/TOIMG"+key4+"N."+key5);
        if (!file.exists()) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>404 FILE NOT FOUND</h1>");
            }
        }
        if(key5.equals("png")){
            mediaType = MediaType.IMAGE_PNG;
        }else if(key5.equals("gif")){
            mediaType = MediaType.IMAGE_GIF;
        }else{
            mediaType = MediaType.IMAGE_JPEG;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        // headers.setContentDispositionFormData("attachment", URLUtil.encode(file.getName()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }

    private Integer yzupdate(){
        Calendar cal = Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m=cal.get(Calendar.MONTH);
        int d=cal.get(Calendar.DATE);
        //int h=cal.get(Calendar.HOUR_OF_DAY);
        //int mm=cal.get(Calendar.MINUTE);
        return y+m+d+999;
    }

    @RequestMapping("/err")
    public String err() {
        return "err";
    }


}
