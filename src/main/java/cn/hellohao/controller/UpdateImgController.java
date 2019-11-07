package cn.hellohao.controller;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.hellohao.exception.StorageSourceInitException;
import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.IPPortUtil;
import cn.hellohao.utils.*;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONArray;

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

    private Integer updatekey;

    @RequestMapping({"/", "/index"})
    public String indexImg(Model model, HttpSession httpSession) {
        //boolean b = VerificationDomain.verification();
        boolean b = true;
        if(b){
            Print.Normal("欢迎使用Hellohao图床源码。者也许是最好用的java图床项目。");
            Print.Normal("当前项目路径："+System.getProperty("user.dir"));
            Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
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
            //Boolean b =false;
            //Integer Sourcekey = GetCurrentSource.GetSource(u==null?null:u.getId());//查询当当前用户或者游客使用的数据源
            if (email != null) {
                //登陆成功
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
                isupdate = (u == null) ? 0: 1;//如果u等于null那么isupdate就等于0，否则等于1
            }
            model.addAttribute("ykxz", isupdate);
        }else{
            return "index";
        }
        Random random = new Random();
        updatekey = random.nextInt(10000);
        model.addAttribute("updatekey", updatekey);
        return "index";

    }

    @RequestMapping(value = "/upimg")
    @ResponseBody
    public String upimg( HttpSession session
            , @RequestParam(value = "file", required = false) MultipartFile multipartFile,Integer setday,String upurlk) throws Exception {
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Config config = configService.getSourceype();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        User u = (User) session.getAttribute("user");
        Integer usermemory =0;
        Integer memory =0;
        Integer Sourcekey=0;
        Integer maxsize = 0;
        String userpath = "tourist";
        Boolean b =false;

        if(Integer.parseInt(Base64Encryption.decryptBASE64(upurlk))!=updatekey){
            jsonObject.put("imgurls",403);//非法调用
            return jsonObject.toString();
        }
        if(u==null){
            Sourcekey = GetCurrentSource.GetSource(null);
            memory = uploadConfig.getVisitormemory();
            maxsize = uploadConfig.getFilesizetourists();
            usermemory= imgService.getusermemory(0);
            if(usermemory==null){usermemory = 0;}
        }else{
            userpath = u.getUsername();
            Sourcekey = GetCurrentSource.GetSource(u.getId());
            memory = userService.getUsers(u.getEmail()).getMemory();
            maxsize = uploadConfig.getFilesizeuser();
            usermemory= imgService.getusermemory(u.getId());
            if(usermemory==null){usermemory = 0;}
        }
        //查询到如果使用目录格式是日期，就重新给userpath赋值
        if(uploadConfig.getUrltype()==2){
            userpath = dateFormat.format(new Date());
        }
        Keys key = keysService.selectKeys(Sourcekey);
        if(Sourcekey==5){b =true;}
        else{b = StringUtils.doNull(Sourcekey,key);}//判断对象是否有空值
        if(!b){
            jsonObject.put("imgurls",-1);//存储源不完整
            return jsonObject.toString();
        }
        //如果memory为-1说明无限制给值-2就行，
        int tmp =memory==-1? -2:(usermemory/1024);
        if(tmp>=memory){
            jsonObject.put("imgurls",-5);//可用空间不足
            return jsonObject.toString();
        }
        long stime = System.currentTimeMillis();
        Map<String, MultipartFile> map = new HashMap<>();
        //for (MultipartFile multipartFile : file) {
        Print.Normal("文件大小："+multipartFile.getSize());
        if(multipartFile.getSize()>maxsize*1024*1024){
            jsonObject.put("imgurls",-6);//超出设定大小
            return jsonObject.toString();
        }
        // 获取ImageReader对象的迭代器
        String fileName = multipartFile.getOriginalFilename();
        String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀
        if (!multipartFile.isEmpty()) { //判断文件是否为空
            map.put(lastname, multipartFile);
        }
        //}
        Map<ReturnImage, Integer> m = null;
        m = GetSource.storageSource(key.getStorageType(),map, userpath,null,setday);
        //开始存数据
        Images img = new Images();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Print.Normal("上传图片的时间是："+df.format(new Date()));
        for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
            if(key.getStorageType()==5){
                if(config.getDomain()!=null){
                    jsonObject.put("imgurls",config.getDomain()+"/links/"+entry.getKey().getImgurl());
                    jsonObject.put("imgnames",entry.getKey().getImgname());
                    img.setImgurl(config.getDomain()+"/links/"+entry.getKey().getImgurl());//图片链接
                }else{
                    jsonObject.put("imgurls",config.getDomain()+"/links/"+entry.getKey().getImgurl());
                    jsonObject.put("imgnames",entry.getKey().getImgname());
                    img.setImgurl("http://"+IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/links/"+entry.getKey().getImgurl());//图片链接
                }
            }else{
                jsonObject.put("imgurls",entry.getKey().getImgurl());
                jsonObject.put("imgnames",entry.getKey().getImgname());
                img.setImgurl(entry.getKey().getImgurl());//图片链接
            }
            jsonArray.add(jsonObject);
            img.setUpdatetime(df.format(new Date()));
            img.setSource(key.getStorageType());
            img.setUserid(u == null?0:u.getId());
            img.setSizes((entry.getValue()) / 1024);
            img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
            img.setImgtype(setday>0?1:0);
            userService.insertimg(img);
            long etime = System.currentTimeMillis();
            Print.Normal("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
            //多线程插入ip值
            GetIPS getIPS = new GetIPS();
            getIPS.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
            Thread thread = new Thread(getIPS);
            thread.start();
        }
        return jsonObject.toString();
    }

//根据网络图片url上传
    @PostMapping(value = "/upurlimg")
    @ResponseBody
    public String upurlimg(HttpSession session, String imgurl, HttpServletRequest request,Integer setday,String upurlk) throws Exception {
        Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        User u = (User) session.getAttribute("user");
        Integer usermemory =0;
        Integer memory =0;
        Integer Sourcekey=0;
        String userpath = "tourist";
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
        JSONArray jsonArray = new JSONArray();
        //判断非法调用
        if(Integer.parseInt(Base64Encryption.decryptBASE64(upurlk))!=updatekey){
            jsonArray.add(-403);//非法调用
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
            jsonArray.add(-1);//存储源不完整
            return jsonArray.toString();
        }
        //先判断对象存储key是不是null
        Print.warning("上传地址是："+request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/");
                if(usermemory/1024>=memory) {
                    jsonArray.add(-5);
                    return jsonArray.toString();
                }
                long stime = System.currentTimeMillis();
                //判断是会员还是游客
                if(u!=null){
                    //判断文件大小
                    if(imgsize>0 && imgsize>=(yonghu*1024*1024)) {
                        //文件过大
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
                            //判断文件头是否是图片
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
                                //img.setAbnormal(Sentence.getIPs());
                                //多线程插入ip值
                                GetIPS getIPS = new GetIPS();
                                getIPS.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                                Thread thread = new Thread(getIPS);
                                thread.start();
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
                            //判断文件头是否是图片
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
                                        img.setImgurl(config.getDomain()+"/links/"+entry.getKey().getImgurl());//图片链接
                                    }else{
                                        jsonArray.add(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                                        img.setImgurl("http://"+IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/links/"+entry.getKey().getImgurl());//图片链接
                                    }
                                }else{
                                    jsonArray.add(entry.getKey().getImgurl());
                                    img.setImgurl(entry.getKey().getImgurl());//图片链接
                                }
                                img.setUpdatetime(times);
                                img.setSource(key.getStorageType());
                                img.setUserid(u == null?0:u.getId());
                                img.setSizes((entry.getValue()));
                                img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                                img.setImgtype(setday>0?1:0);
                                userService.insertimg(img);
                                long etime = System.currentTimeMillis();
                                System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                                //多线程插入ip值
                                GetIPS getIPS = new GetIPS();
                                getIPS.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                                Thread thread = new Thread(getIPS);
                                thread.start();
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

    //刪除用戶
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

    @RequestMapping("/err")
    public String err() {
        return "err";
    }

}
