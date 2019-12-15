package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UpdateImgController {
    @Autowired
    private NOSImageupload nOSImageupload;
    @Autowired
    private UserService userService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private OSSImageupload ossImageupload;
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

    private String[] iparr;

    @RequestMapping({"/", "/index"})
    public String indexImg(Model model, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
            Print.Normal("当前项目路径："+System.getProperty("user.dir"));
            Config config = configService.getSourceype();
            UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
            User u = (User) httpSession.getAttribute("user");
            String email = (String) httpSession.getAttribute("email");
            Integer filesizetourists = 0;
            Integer filesizeuser = 0;
            Integer imgcounttourists = 0;
            Integer imgcountuser = 0;
            if(uploadConfig.getFilesizetourists()!=null){filesizetourists = uploadConfig.getFilesizetourists();}
            if(uploadConfig.getFilesizeuser()!=null){filesizeuser = uploadConfig.getFilesizeuser();}
            if(uploadConfig.getImgcounttourists()!=null){imgcounttourists = uploadConfig.getImgcounttourists();}
            if(uploadConfig.getImgcountuser()!=null){imgcountuser = uploadConfig.getImgcountuser();}
            if (email != null) {
                Integer ret = userService.login(u.getEmail(), u.getPassword());
                if (ret > 0) {
                    User user = userService.getUsers(u.getEmail());
                    model.addAttribute("username", user.getUsername());
                    model.addAttribute("level", user.getLevel());
                    model.addAttribute("loginid", 100);
                    model.addAttribute("imgcount", imgcountuser);
                    model.addAttribute("filesize", filesizeuser*1024*1024);
                } else {
                    model.addAttribute("loginid", -1);
                    model.addAttribute("imgcount", imgcounttourists);
                }
            } else {
                model.addAttribute("loginid", -2);
                model.addAttribute("imgcount", imgcounttourists);
                model.addAttribute("filesize", filesizetourists*1024*1024);
            }
            model.addAttribute("suffix", uploadConfig.getSuffix());
            model.addAttribute("config", config);
            model.addAttribute("uploadConfig", uploadConfig);
            Integer isupdate = 1;
            if(uploadConfig.getIsupdate()!=1){
                isupdate = (u == null) ? 0: 1;
            }
            model.addAttribute("VisitorUpload", isupdate);
        return "index";
    }

    @RequestMapping(value = "/upimg")
    @ResponseBody
    public String upimg( HttpSession session,HttpServletRequest request
            , @RequestParam(value = "file", required = false) MultipartFile multipartFile,Integer setday,String upurlk) throws Exception {
        String userip = GetIPS.getIpAddr(request);
        Print.Normal("上传者ip:"+userip);
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Config config = configService.getSourceype();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        User u = (User) session.getAttribute("user");
        Integer usermemory =0;
        Integer memory =0;
        Integer sourcekey=0;
        Integer maxsize = 0;
        String userpath = "tourist";
        Boolean b =false;
        if(uploadConfig.getBlacklist()!=null){
            iparr = uploadConfig.getBlacklist().split(";");
            for (String s : iparr) {
                if(s.equals(userip)){
                    jsonObject.put("imgurls",911);
                    return jsonObject.toString();
                }
            }
        }
        if(Integer.parseInt(Base64Encryption.decryptBASE64(upurlk))!=yzupdate()){
            jsonObject.put("imgurls",403);
            return jsonObject.toString();
        }
        if(u==null){
            sourcekey = GetCurrentSource.GetSource(null);
            memory = uploadConfig.getVisitormemory();
            maxsize = uploadConfig.getFilesizetourists();
            usermemory= imgService.getusermemory(0);
            if(usermemory==null){usermemory = 0;}
        }else{
            userpath = u.getUsername();
            sourcekey = GetCurrentSource.GetSource(u.getId());
            memory = userService.getUsers(u.getEmail()).getMemory();
            maxsize = uploadConfig.getFilesizeuser();
            usermemory= imgService.getusermemory(u.getId());
            if(usermemory==null){usermemory = 0;}
        }
        if(uploadConfig.getUrltype()==2){
            userpath = dateFormat.format(new Date());
        }
        Keys key = keysService.selectKeys(sourcekey);
        if(sourcekey==5){b =true;}
        else{b = StringUtils.doNull(sourcekey,key);}
        if(!b){
            jsonObject.put("imgurls",-1);
            return jsonObject.toString();
        }
        int tmp =(memory==-1? -2:(usermemory/1024));
        if(tmp>=memory){
            jsonObject.put("imgurls",-5);
            return jsonObject.toString();
        }
        long stime = System.currentTimeMillis();
        Map<String, MultipartFile> map = new HashMap<>();
        Print.Normal("文件大小："+multipartFile.getSize());
        if(multipartFile.getSize()>maxsize*1024*1024){
            jsonObject.put("imgurls",-6);
            return jsonObject.toString();
        }
        String fileName = multipartFile.getOriginalFilename();
        String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!multipartFile.isEmpty()) {
            map.put(lastname, multipartFile);
        }
        Map<ReturnImage, Integer> m = null;
        m = GetSource.storageSource(key.getStorageType(),map, userpath,null,setday);
        Images img = new Images();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Print.Normal("上传图片的时间是："+df.format(new Date()));
        for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
            if(key.getStorageType()==5){
                if(config.getDomain()!=null){
                    jsonObject.put("imgurls",config.getDomain()+"/links/"+entry.getKey().getImgurl());
                    jsonObject.put("imgnames",entry.getKey().getImgname());
                    img.setImgurl(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                }else{
                    jsonObject.put("imgurls",config.getDomain()+"/links/"+entry.getKey().getImgurl());
                    jsonObject.put("imgnames",entry.getKey().getImgname());
                    img.setImgurl("http://"+IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/links/"+entry.getKey().getImgurl());//图片链接
                }
            }else{
                jsonObject.put("imgurls",entry.getKey().getImgurl());
                jsonObject.put("imgnames",entry.getKey().getImgname());
                img.setImgurl(entry.getKey().getImgurl());
            }
            jsonArray.add(jsonObject);
            img.setUpdatetime(df.format(new Date()));
            img.setSource(key.getStorageType());
            img.setUserid(u == null?0:u.getId());
            img.setSizes((entry.getValue()) / 1024);
            img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
            img.setImgtype(setday>0?1:0);
            img.setAbnormal(userip);
            userService.insertimg(img);
            long etime = System.currentTimeMillis();
            Print.Normal("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
        }
        return jsonObject.toString();
    }

//根据网络图片url上传
    @PostMapping(value = "/upurlimg")
    @ResponseBody
    public String upurlimg(HttpSession session, String imgurl, HttpServletRequest request,Integer setday,String upurlk) throws Exception {
        JSONArray jsonArray = new JSONArray();
        Config config = configService.getSourceype();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        User u = (User) session.getAttribute("user");
        Integer usermemory =0;
        Integer memory =0;
        Integer Sourcekey=0;
        String userpath = "tourist";
        String userip = GetIPS.getIpAddr(request);
        Print.Normal("上传者ip:"+userip);
        iparr = uploadConfig.getBlacklist().split(";");
        for (String s : iparr) {
            if(s.equals(userip)){
                jsonArray.add(911);
                return jsonArray.toString();
            }
        }
        if(u==null){
            Sourcekey = GetCurrentSource.GetSource(null);
            memory = uploadConfig.getVisitormemory();
            usermemory= imgService.getusermemory(0);
            if(usermemory==null){usermemory = 0;}
        }else{
            userpath = u.getUsername();
            Sourcekey = GetCurrentSource.GetSource(u.getId());
            memory = userService.getUsers(u.getEmail()).getMemory();
            usermemory= imgService.getusermemory(u.getId());
            if(usermemory==null){usermemory = 0;}
        }
        if(uploadConfig.getUrltype()==2){
            java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
            userpath = dateFormat.format(new Date());
        }
        if(Integer.parseInt(Base64Encryption.decryptBASE64(upurlk))!=yzupdate()){
            jsonArray.add(-403);
            return jsonArray.toString();
        }
        Keys key = keysService.selectKeys(Sourcekey);
        long imgsize = ImgUrlUtil.getFileLength(imgurl);
        Integer youke = uploadConfig.getFilesizetourists();
        Integer yonghu = uploadConfig.getFilesizeuser();
        String uuid= UUID.randomUUID().toString().replace("-", "");
        Boolean bo =false;
        bo = Sourcekey==5?true:StringUtils.doNull(Sourcekey,key);
        if(!bo){
            jsonArray.add(-1);
            return jsonArray.toString();
        }
        Print.warning("上传地址是："+request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/");

        if(usermemory/1024>=memory) {
            jsonArray.add(-5);
            return jsonArray.toString();
        }
        long stime = System.currentTimeMillis();
        if(u!=null){
            if(imgsize>0 && imgsize>=(yonghu*1024*1024)) {
                jsonArray.add(-2);
                return jsonArray.toString();
            }
            try{
                boolean bl =ImgUrlUtil.downLoadFromUrl(imgurl,
                        uuid, request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/");
                if(bl==true){
                    FileInputStream is = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                    byte[] b = new byte[3];
                    is.read(b, 0, b.length);
                    String xxx = ImgUrlUtil.bytesToHexString(b);
                    xxx = xxx.toUpperCase();
                    if(is!=null){is.close();}
                    if(TypeDict.checkType(xxx).equals("0000")) {
                        jsonArray.add(-3);//不是图片格式
                        return jsonArray.toString();
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put(TypeDict.checkType(xxx), request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                    Map<ReturnImage, Integer> m = null;
                    m = GetSource.storageSource(key.getStorageType(), null, userpath,map,setday);
                    Images img = new Images();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    String times = df.format(new Date());
                    System.out.println("上传图片的时间是："+times);
                    for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
                        if(key.getStorageType()==5){
                            if(config.getDomain()!=null){
                                jsonArray.add(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                                img.setImgurl(config.getDomain()+"/links/"+entry.getKey().getImgurl());//图片链接
                            }else{
                                jsonArray.add(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                                img.setImgurl("http://"+IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/links/"+entry.getKey().getImgurl());//图片链接
                            }
                        }else{
                            jsonArray.add(entry.getKey().getImgurl());
                            img.setImgurl(entry.getKey().getImgurl());
                        }
                        img.setUpdatetime(times);
                        img.setSource(key.getStorageType());
                        img.setUserid(u == null?0:u.getId());
                        img.setSizes((entry.getValue()));
                        img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                        img.setAbnormal(userip);
                        if(setday>0){img.setImgtype(1);}
                        else{img.setImgtype(0);}
                        userService.insertimg(img);
                        long etime = System.currentTimeMillis();
                        System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                    }
                }
            }catch (Exception e) {
                Print.warning(e.toString());
                jsonArray.add(-4);
            }
        }else{
            if(imgsize>0 && imgsize>=(youke*1024*1024)){
                //文件过大
                jsonArray.add(-2);
                return jsonArray.toString();
            }
            try{
                boolean bl = ImgUrlUtil.downLoadFromUrl(imgurl,
                        uuid, request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/");
                if(bl==true){
                    FileInputStream is = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                    byte[] b = new byte[3];
                    is.read(b, 0, b.length);
                    String xxx = ImgUrlUtil.bytesToHexString(b);
                    xxx = xxx.toUpperCase();
                    if(is!=null){is.close(); }
                    if(xxx.equals("0000")) {
                        jsonArray.add(-3);
                        return jsonArray.toString();
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put(TypeDict.checkType(xxx), request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                    Map<ReturnImage, Integer> m = null;
                    m = GetSource.storageSource(key.getStorageType(), null, userpath,map,setday);
                    Images img = new Images();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    String times = df.format(new Date());
                    System.out.println("上传图片的时间是："+times);
                    for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
                        if(key.getStorageType()==5){
                            if(config.getDomain()!=null){
                                jsonArray.add(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                                img.setImgurl(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                            }else{
                                jsonArray.add(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                                img.setImgurl("http://"+IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/links/"+entry.getKey().getImgurl());//图片链接
                            }
                        }else{
                            jsonArray.add(entry.getKey().getImgurl());
                            img.setImgurl(entry.getKey().getImgurl());
                        }
                        img.setUpdatetime(times);
                        img.setSource(key.getStorageType());
                        img.setUserid(u == null?0:u.getId());
                        img.setSizes((entry.getValue()));
                        img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                        img.setImgtype(setday>0?1:0);
                        img.setAbnormal(userip);
                        userService.insertimg(img);
                        long etime = System.currentTimeMillis();
                        System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                    }
                }
            }catch (Exception e) {
                Print.warning(e.toString());
                jsonArray.add(-4);
            }
        }
        return jsonArray.toString();
/**
 * 错误返回值含义：
 * -1 存储源key未配置
 * -2 目标图片太大或者不存在
 * -3 文件类型不符合要求
 * */
    }

    @RequestMapping("/sentence")
    @ResponseBody
    public String sentence(HttpSession session, Integer id) {
        JSONArray jsonArray = new JSONArray();
        String text = Sentence.getURLContent();
        jsonArray.add(text);
        return jsonArray.toString();
    }

    //ajax查询用户是否已经登录
    @RequestMapping(value = "/islogin")
    @ResponseBody
    public String islogin(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        User user = (User) session.getAttribute("user");
        if(user!=null){
            if (user.getEmail() != null && user.getPassword() != null) {
                jsonObject.put("username",user.getUsername());
                jsonObject.put("level",user.getLevel());
                jsonObject.put("lgoinret",1);
            }else{
                jsonObject.put("lgoinret",0);
            }
        }
        return jsonObject.toString();
    }

    @GetMapping(value = "/images/{id}")
    @ResponseBody
    public Images selectByFy(@PathVariable("id") Integer id) {
        return imgService.selectByPrimaryKey(id);
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
