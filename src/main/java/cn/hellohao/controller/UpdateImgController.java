package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.*;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.text.StringEscapeUtils;
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
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
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
                Integer ret = userService.login(u.getEmail(), u.getPassword(),null);
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
            vu = IdUtil.simpleUUID();
            model.addAttribute("vu",vu);
            if(config.getTheme()==1){
                return "index";
            }else{
                return "index-Minimalism";
            }
    }

    @RequestMapping(value = "/upimg")
    @ResponseBody
    public Msg upimg( HttpSession session,HttpServletRequest request
            , @RequestParam(value = "file", required = false) MultipartFile multipartFile,Integer setday,String upurlk) throws Exception {
        Msg msg = new Msg();
        msg = uploadServicel.uploadForLoc(session,request,multipartFile,setday,upurlk,iparr);
        return msg;
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
        //if(Integer.parseInt(Base64Encryption.decryptBASE64(upurlk))!=yzupdate()){
        if (!upurlk.equals(UpdateImgController.vu)) {
            jsonArray.add(-403);
            return jsonArray.toString();
        }
        Keys key = keysService.selectKeys(Sourcekey);
        long imgsize = ImgUrlUtil.getFileLength(imgurl);
        Integer youke = uploadConfig.getFilesizetourists();
        Integer yonghu = uploadConfig.getFilesizeuser();
        String uuid= UUID.randomUUID().toString().replace("-", "");
        //Boolean bo =false;
        //bo = Sourcekey==5?true:StringUtils.doNull(Sourcekey,key);
//        if(!bo){
//            jsonArray.add(-1);
//            return jsonArray.toString();
//        }
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
                        uuid, request.getSession().getServletContext().getRealPath("/")+File.separator+"hellohaotmp"+File.separator);
                if(bl==true){
                    FileInputStream is = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+File.separator+"hellohaotmp"+File.separator+uuid);
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
                    map.put(TypeDict.checkType(xxx), request.getSession().getServletContext().getRealPath("/")+"hellohaotmp"+ File.separator+uuid);
                    Map<ReturnImage, Integer> m = null;
                    m = GetSource.storageSource(key.getStorageType(), null, userpath,map,setday);
                    Images img = new Images();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    String times = df.format(new Date());
                    System.out.println("上传图片的时间是："+times);
                    for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
                        if(key.getStorageType()==5){
                            if(config.getDomain()!=null){
                                jsonArray.add(config.getDomain()+"/"+entry.getKey().getImgurl());
                                img.setImgurl(config.getDomain()+"/"+entry.getKey().getImgurl());//图片链接
                            }else{
                                jsonArray.add(config.getDomain()+"/"+entry.getKey().getImgurl());
                                img.setImgurl("http://"+IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/"+entry.getKey().getImgurl());//图片链接
                            }
                        }else{
                            jsonArray.add(entry.getKey().getImgurl());
                            img.setImgurl(entry.getKey().getImgurl());
                        }
                        img.setUpdatetime(times);
                        img.setSource(key.getStorageType());
                        img.setUserid(u == null?0:u.getId());
                        img.setSizes((entry.getValue()));
                        //img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                        img.setImgname(entry.getKey().getImgurl());
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
                        uuid, request.getSession().getServletContext().getRealPath("/")+"hellohaotmp"+File.separator);
                if(bl==true){
                    FileInputStream is = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"hellohaotmp"+File.separator+uuid);
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
                    map.put(TypeDict.checkType(xxx), request.getSession().getServletContext().getRealPath("/")+File.separator+"hellohaotmp"+File.separator+uuid);
                    Map<ReturnImage, Integer> m = null;
                    m = GetSource.storageSource(key.getStorageType(), null, userpath,map,setday);
                    Images img = new Images();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    String times = df.format(new Date());
                    System.out.println("上传图片的时间是："+times);
                    for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
                        if(key.getStorageType()==5){
                            if(config.getDomain()!=null){
                                jsonArray.add(config.getDomain()+"/"+entry.getKey().getImgurl());
                                img.setImgurl(config.getDomain()+"/"+entry.getKey().getImgurl());
                            }else{
                                jsonArray.add(config.getDomain()+"/"+entry.getKey().getImgurl());
                                img.setImgurl("http://"+IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/"+entry.getKey().getImgurl());//图片链接
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
