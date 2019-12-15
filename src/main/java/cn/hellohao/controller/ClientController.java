package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-18 17:22
 */
@RestController
public class ClientController {
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
    private USSImageupload ussImageupload;
    @Autowired
    private KODOImageupload kodoImageupload;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private COSImageupload cosImageupload;
    @Autowired
    private FTPImageupload ftpImageupload;
    @Autowired
    private DomainService domainService;
    @Autowired
    private ImgService imgService;

    @Value("${systemupdate}")
    private String systemupdate;

    @PostMapping(value = "/clientupimg")
    @ResponseBody
    public ResultBean clientupimg(HttpServletRequest request,@RequestParam("file") List<MultipartFile> file, String email, String pass) throws Exception {
        String userip = GetIPS.getIpAddr(request);
        Print.Normal("上传者ip:"+userip);
        ResultBean resultBean = null;
        JSONArray jsonArray = new JSONArray();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        if (uploadConfig.getApi() == 1) {
            if (email != null && pass != null) {
                Integer ret = userService.login(email, Base64Encryption.encryptBASE64(pass.getBytes()));
                if (ret > 0) {
                    User user = userService.getUsers(email);
                    if (user.getIsok() == 1) {
                        User u = userService.getUsers(email);
                        Config config = configService.getSourceype();//查询当前系统使用的存储源类型。

                        Integer Sourcekey = GetCurrentSource.GetSource(u.getId());

                        Keys key = keysService.selectKeys(Sourcekey);
                        if (key.getStorageType() != 0 && key.getStorageType() != null) {
                            if (key.getStorageType() == 1) {
                                nOSImageupload.Initialize(key);//实例化网易
                            } else if (key.getStorageType() == 2) {
                                OSSImageupload.Initialize(key);
                            } else if (key.getStorageType() == 3) {
                                USSImageupload.Initialize(key);
                            } else if (key.getStorageType() == 4) {
                                KODOImageupload.Initialize(key);
                            } else if (key.getStorageType() == 6) {
                                COSImageupload.Initialize(key);
                            } else if (key.getStorageType() == 7) {
                                FTPImageupload.Initialize(key);
                            } else {
                                System.err.println("客户端：未获取到对象存储参数，初始化失败。");
                            }
                        }
                        Print.Normal("客户端：初始化上传。");
                        Boolean b = false;
                        if (Sourcekey == 5) {
                            b = true;
                        } else {
                            b = StringUtils.doNull(Sourcekey, key);//判断对象是否有空值
                        }
                        if (b) {
                            long stime = System.currentTimeMillis();
                            String userpath = "tourist";
                            if (uploadConfig.getUrltype() == 2) {
                                java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                userpath = dateFormat.format(new Date());
                            } else {
                                if (u != null) {
                                    userpath = u.getUsername();
                                }
                            }
                            Map<String, MultipartFile> map = new HashMap<>();
                            for (MultipartFile multipartFile : file) {
                                //获取文件名
                                String fileName = multipartFile.getOriginalFilename();
                                String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀
                                if (!multipartFile.isEmpty()) { //判断文件是否为空
                                    map.put(lastname, multipartFile);
                                }
                            }
                            Map<ReturnImage, Integer> m = null;
                            Map<ReturnImage, Integer> m2 = null;
                            if (key.getStorageType() == 1) {
                                m = nOSImageupload.clientuploadNOS(map, userpath, uploadConfig);
                            } else if (key.getStorageType() == 2) {
                                m = ossImageupload.clientuploadOSS(map, userpath, uploadConfig);
                            } else if (key.getStorageType() == 3) {
                                m = ussImageupload.clientuploadUSS(map, userpath, uploadConfig);
                            } else if (key.getStorageType() == 4) {
                                m = kodoImageupload.clientuploadKODO(map, userpath, uploadConfig);
                            } else if (key.getStorageType() == 5) {
                                m2 = LocUpdateImg.clientuploadFTP(map, userpath, uploadConfig);
                            } else if (key.getStorageType() == 6) {
                                m = cosImageupload.clientuploadCOS(map, userpath, uploadConfig);
                            } else if (key.getStorageType() == 7) {
                                m = ftpImageupload.clientuploadFTP(map, userpath, uploadConfig);
                            } else {
                                System.err.println("未获取到对象存储参数，上传失败。");
                            }
                            Images img = new Images();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String times = df.format(new Date());
                            System.out.println("上传图片的时间是：" + times);
                            if (key.getStorageType() == 5) {
                                for (Map.Entry<ReturnImage, Integer> entry : m2.entrySet()) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("Imgname", entry.getKey().getImgname());
                                    if (key.getStorageType() == 5) {
                                        if (config.getDomain() != null) {
                                            jsonObject.put("Imgurl", config.getDomain() + "/links/" + entry.getKey().getImgurl());
                                            img.setImgurl(config.getDomain() + "/links/" + entry.getKey().getImgurl());//图片链接
                                        } else {
                                            jsonObject.put("Imgurl", config.getDomain() + "/links/" + entry.getKey().getImgurl());
                                            img.setImgurl("http://" + IPPortUtil.getLocalIP() + ":" + IPPortUtil.getLocalPort() + "/links/" + entry.getKey().getImgurl());//图片链接
                                        }
                                    } else {
                                        jsonObject.put("Imgname", entry.getKey().getImgurl());
                                        img.setImgurl(entry.getKey().getImgurl());//图片链接
                                    }
                                    img.setUpdatetime(times);
                                    img.setSource(key.getStorageType());
                                    if (u == null) {
                                        img.setUserid(0);//用户id
                                    } else {
                                        img.setUserid(u.getId());//用户id
                                    }
                                    img.setSizes((entry.getValue()));
                                    img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                                    img.setAbnormal(userip);
                                    img.setImgtype(0);
                                    //-1证明超出大小，
                                    if (entry.getValue() != -1) {
                                        userService.insertimg(img);
                                    }
                                    long etime = System.currentTimeMillis();
                                    System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                                    jsonArray.add(jsonObject);
                                }
                            } else {
                                for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("Imgname", entry.getKey().getImgname());
                                    if (key.getStorageType() == 5) {
                                        if (config.getDomain() != null) {
                                            jsonObject.put("Imgurl", config.getDomain() + "/links/" + entry.getKey().getImgurl());
                                            img.setImgurl(config.getDomain() + "/links/" + entry.getKey().getImgurl());//图片链接
                                        } else {
                                            jsonObject.put("Imgurl", config.getDomain() + "/links/" + entry.getKey().getImgurl());
                                            img.setImgurl("http://" + IPPortUtil.getLocalIP() + ":" + IPPortUtil.getLocalPort() + "/links/" + entry.getKey().getImgurl());//图片链接
                                        }
                                    } else {
                                        jsonObject.put("Imgurl", entry.getKey().getImgurl());
                                        img.setImgurl(entry.getKey().getImgurl());//图片链接
                                    }
                                    img.setUpdatetime(times);
                                    img.setSource(key.getStorageType());
                                    if (u == null) {
                                        img.setUserid(0);//用户id
                                    } else {
                                        img.setUserid(u.getId());//用户id
                                    }
                                    img.setSizes((entry.getValue())/1024);
                                    img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                                    img.setAbnormal(userip);
                                    img.setImgtype(0);
                                    //-1证明超出大小，
                                    if (entry.getValue() != -1) {
                                        userService.insertimg(img);
                                    }
                                    long etime = System.currentTimeMillis();
                                    System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                                    jsonArray.add(jsonObject);
                                }
                            }
                            resultBean = ResultBean.success(jsonArray);
                        } else {resultBean = ResultBean.error(-1, "服务器内部错误，请联系管理员");}
                    }
                } else {resultBean = ResultBean.error(-2, "此用户信息不正确。");}
            } else {resultBean = ResultBean.error(-3, "邮箱密码为空");}
        }
        else{resultBean = ResultBean.error(-4, "管理员关闭了API接口");}
        return resultBean;
    }

    @PostMapping(value = "/clientupurlimg")
    @ResponseBody
    public ResultBean clientupurlimg( String imgurl, HttpServletRequest request, Integer setday,
                           String email,String pass) throws Exception {
        String userip = GetIPS.getIpAddr(request);
        Print.Normal("上传者ip:"+userip);
        ResultBean resultBean = null;
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        if (uploadConfig.getApi() == 1) {
            if (email != null && pass != null) {
                Integer ret = userService.login(email, Base64Encryption.encryptBASE64(pass.getBytes()));
                if (ret > 0) {
                    User u = userService.getUsers(email);
                    Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
                    Integer usermemory =0;
                    Integer memory =0;
                    Integer Sourcekey=0;
                    if(u==null){
                        Sourcekey = GetCurrentSource.GetSource(null);
                        memory = uploadConfig.getVisitormemory();
                        usermemory= imgService.getusermemory(0);
                        if(usermemory==null){usermemory = 0;}
                    }else{
                        Sourcekey = GetCurrentSource.GetSource(u.getId());
                        memory = userService.getUsers(u.getEmail()).getMemory();
                        usermemory= imgService.getusermemory(u.getId());
                        if(usermemory==null){usermemory = 0;}
                    }
                    String userpath = "tourist";
                    if(uploadConfig.getUrltype()==2){
                        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
                        userpath = dateFormat.format(new Date());
                    }else{if (u != null) { userpath = u.getUsername();}}
                    JSONArray jsonArray = new JSONArray();

                    Keys key = keysService.selectKeys(Sourcekey);
                    long imgsize = ImgUrlUtil.getFileLength(imgurl);
                    Integer youke = uploadConfig.getFilesizetourists();
                    Integer yonghu = uploadConfig.getFilesizeuser();
                    String uuid= UUID.randomUUID().toString().replace("-", "");
                    Boolean bo =false;
                    if(Sourcekey==5){
                        bo =true;
                    }else{bo = StringUtils.doNull(Sourcekey,key);//判断对象是否有空值
                    }
//        //容量判断
                    if(u==null){
                        memory = uploadConfig.getVisitormemory();
                        usermemory= imgService.getusermemory(0);
                        if(usermemory==null){usermemory = 0;}
                    }else{
                        memory = userService.getUsers(u.getEmail()).getMemory();
                        usermemory= imgService.getusermemory(u.getId());
                        if(usermemory==null){usermemory = 0;}
                    }
                    Print.warning("上传地址是："+request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/");
                    if(bo){
                        if(usermemory/1024<memory){
                            long stime = System.currentTimeMillis();
                            if(u!=null){
                                if(imgsize>0 && imgsize<=(yonghu*1024*1024)){
                                    try{
                                        boolean bl =ImgUrlUtil.downLoadFromUrl(imgurl,
                                                uuid, request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/");
                                        if(bl==true){
                                            FileInputStream is = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                                            byte[] b = new byte[3];
                                            is.read(b, 0, b.length);
                                            String xxx = ImgUrlUtil.bytesToHexString(b);
                                            xxx = xxx.toUpperCase();
                                            String ooo = TypeDict.checkType(xxx);
                                            if(is!=null){
                                                is.close();
                                            }
                                            if(!ooo.equals("0000")){
                                                Map<String, String> map = new HashMap<>();
                                                map.put(ooo, request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                                                Map<ReturnImage, Integer> m = null;
                                                if(key.getStorageType()==1){
                                                    m = nOSImageupload.Imageupload(null, userpath,map,setday);
                                                }else if (key.getStorageType()==2){
                                                    m = ossImageupload.ImageuploadOSS(null, userpath,map,setday);
                                                }else if(key.getStorageType()==3){
                                                    m = ussImageupload.ImageuploadUSS(null, userpath,map,setday);
                                                }else if(key.getStorageType()==4){
                                                    m = kodoImageupload.ImageuploadKODO(null, userpath,map,setday);
                                                }else if(key.getStorageType()==5){
                                                    m = LocUpdateImg.ImageuploadLOC(null,userpath,map,setday);
                                                }else if(key.getStorageType()==6){
                                                    m = cosImageupload.ImageuploadCOS(null,userpath,map,setday);
                                                }else if(key.getStorageType()==7){
                                                    m =  ftpImageupload.ImageuploadFTP(null,userpath,map,setday);
                                                }
                                                else{
                                                    System.err.println("未获取到对象存储参数，上传失败。");
                                                }
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
                                                    if (u == null) {
                                                        img.setUserid(0);
                                                    } else {
                                                        img.setUserid(u.getId());
                                                    }
                                                    img.setSizes((entry.getValue()));
                                                    img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                                                    img.setAbnormal(userip);
                                                    img.setImgtype(0);
                                                    userService.insertimg(img);
                                                    long etime = System.currentTimeMillis();
                                                    System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                                                }
                                                resultBean = ResultBean.success(jsonArray);
                                            }else{
                                                resultBean = ResultBean.error(-3,"文件类型不符合要求");
                                            }
                                        }
                                    }catch (Exception e) {
                                        // TODO: handle exception
                                        Print.warning(e.toString());
                                        resultBean = ResultBean.error(-4,"该文件不支持上传");
                                    }
                                }else{
                                    resultBean = ResultBean.error(-2,"文件过大");
                                }
                            }else{
                                if(imgsize>0 && imgsize<=(youke*1024*1024)){
                                    try{
                                        boolean bl = ImgUrlUtil.downLoadFromUrl(imgurl,
                                                uuid, request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/");
                                        if(bl==true){
                                            FileInputStream is = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                                            byte[] b = new byte[3];
                                            is.read(b, 0, b.length);
                                            String xxx = ImgUrlUtil.bytesToHexString(b);
                                            xxx = xxx.toUpperCase();
                                            String ooo = TypeDict.checkType(xxx);
                                            if(is!=null){
                                                is.close();
                                            }
                                            if(!xxx.equals("0000")){
                                                Map<String, String> map = new HashMap<>();
                                                map.put(ooo, request.getSession().getServletContext().getRealPath("/")+"/hellohaotmp/"+uuid);
                                                Map<ReturnImage, Integer> m = null;
                                                if(key.getStorageType()==1){
                                                    m = nOSImageupload.Imageupload(null, userpath,map,setday);
                                                }else if (key.getStorageType()==2){
                                                    m = ossImageupload.ImageuploadOSS(null, userpath,map,setday);
                                                }else if(key.getStorageType()==3){
                                                    m = ussImageupload.ImageuploadUSS(null, userpath,map,setday);
                                                }else if(key.getStorageType()==4){
                                                    m = kodoImageupload.ImageuploadKODO(null, userpath,map,setday);
                                                }else if(key.getStorageType()==5){
                                                    m =LocUpdateImg.ImageuploadLOC(null,userpath, map,setday);
                                                }else if(key.getStorageType()==6){
                                                    m =cosImageupload.ImageuploadCOS(null,userpath, map,setday);
                                                }else if(key.getStorageType()==7){
                                                    m =  ftpImageupload.ImageuploadFTP(null,userpath,map,setday);
                                                }
                                                else{
                                                    System.err.println("未获取到对象存储参数，上传失败。");
                                                }
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
                                                    if (u == null) {
                                                        img.setUserid(0);//用户id
                                                    } else {
                                                        img.setUserid(u.getId());//用户id
                                                    }
                                                    img.setSizes((entry.getValue()));
                                                    img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                                                    img.setAbnormal(userip);
                                                    img.setImgtype(0);
                                                    userService.insertimg(img);
                                                    long etime = System.currentTimeMillis();
                                                    System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                                                }
                                                resultBean = ResultBean.success(jsonArray);
                                            }else{
                                                resultBean = ResultBean.error(-3,"文件类型不符合要求");
                                            }
                                        }
                                    }catch (Exception e) {
                                        // TODO: handle exception
                                        Print.warning(e.toString());
                                        resultBean = ResultBean.error(-4,"文件类型不符合要求");
                                    }
                                }else{
                                    resultBean = ResultBean.error(-2,"图片太大或不存在");
                                }
                            }
                        }else{
                            resultBean = ResultBean.error(-5,"可用空间不足");
                        }
                    }else{
                        resultBean = ResultBean.error(-1,"未配置存储源");
                    }
                }else {resultBean = ResultBean.error(-2, "此用户信息不正确。");}
            }else {resultBean = ResultBean.error(-3, "邮箱或密码为空");}
        }else{resultBean = ResultBean.error(-4, "管理员关闭了API接口");}
Print.Normal(resultBean.toString());
        return resultBean;
/**
 * 错误返回值含义：
 * -1 存储源key未配置
 * -2 目标图片太大或者不存在
 * -3 文件类型不符合要求
 * */
    }

    @RequestMapping("/clientlogin")
    @ResponseBody
    public String login( HttpSession httpSession, String email, String password) {
        JSONArray jsonArray = new JSONArray();
        String basepass = Base64Encryption.encryptBASE64(password.getBytes());
        Integer ret = userService.login(email, basepass);
        if (ret > 0) {
            User user = userService.getUsers(email);
            if (user.getIsok() == 1) {
                jsonArray.add(1);
            } else if(ret==-1){
                jsonArray.add(-1);
            }else {
                jsonArray.add(-2);
            }
        } else {
            jsonArray.add(0);
        }
        return jsonArray.toString();
    }

    @GetMapping (value = "/notices")
    @ResponseBody
    public String notices() throws Exception {
        return "-1";//-1就是没有公告，客户端不显示
    }
    //主端接口
    @PostMapping("/systemupdate")
    @ResponseBody
    public String sysupdate(String  dates) {
        Integer i=dates.compareTo(systemupdate);//小于0则需要更新
        return i.toString();
    }
    //主端接口
    @PostMapping("/getdomain")
    @ResponseBody
    public String getdomain(String  domain) {
        Integer count = domainService.getDomain(domain);
        return count.toString();
    }

}
