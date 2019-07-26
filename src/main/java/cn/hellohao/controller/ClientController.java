package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.KODOImageupload;
import cn.hellohao.service.impl.NOSImageupload;
import cn.hellohao.service.impl.OSSImageupload;
import cn.hellohao.service.impl.USSImageupload;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${systemupdate}")
    private String systemupdate;

    @PostMapping(value = "/clientupimg")
    @ResponseBody
    public String clientupimg(@RequestParam("file") List<MultipartFile> file, String email,String pass) throws Exception {
        JSONArray jsonArray = new JSONArray();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
                if(email!=null && pass!=null){
            Integer ret = userService.login(email, pass);
            if (ret > 0) {
                User user = userService.getUsers(email);
                if (user.getIsok() == 1) {
                    User u = userService.getUsers(email);
                    Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
                    Keys key = keysService.selectKeys(config.getSourcekey());
                    if(key.getStorageType()!=0 && key.getStorageType()!=null){
                        if(key.getStorageType()==1){
                            nOSImageupload.Initialize(key);//实例化网易
                        }else if (key.getStorageType()==2){
                            OSSImageupload.Initialize(key);
                        }else if(key.getStorageType()==3){
                            USSImageupload.Initialize(key);
                        }else if(key.getStorageType()==4){
                            KODOImageupload.Initialize(key);
                        }else{
                            System.err.println("客户端：未获取到对象存储参数，初始化失败。");
                        }
                    }
                    Print.Normal("客户端：初始化上传。");
                    Boolean b =false;
                    if(config.getSourcekey()==5){
                        b =true;
                    }else{
                        b = StringUtils.doNull(config.getSourcekey(),key);//判断对象是否有空值
                    }
                    if(b){
                        long stime = System.currentTimeMillis();
                        String userpath = "tourist";
                        if(uploadConfig.getUrltype()==2){
                            java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
                            userpath = dateFormat.format(new Date());
                        }else{
                            if (u != null) {
                                userpath = u.getUsername();
                            }
                        }
                        Map<String, MultipartFile> map = new HashMap<>();
                        for (MultipartFile multipartFile : file) {
                            //获取文件名
                            System.out.println(multipartFile.toString());
                            String fileName = multipartFile.getOriginalFilename();
                            String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);//获取文件后缀
                            if (!multipartFile.isEmpty()) { //判断文件是否为空
                                map.put(lastname, multipartFile);
                            }
                        }
                        Map<String, Integer> m = null;
                        Map<ReturnImage, Integer> m2 = null;
                        if(key.getStorageType()==1){
                            m = nOSImageupload.clientuploadNOS(map, userpath);
                        }else if (key.getStorageType()==2){
                            m = ossImageupload.clientuploadOSS(map, userpath);
                        }else if(key.getStorageType()==3){
                            m = ussImageupload.clientuploadUSS(map, userpath);
                        }else if(key.getStorageType()==4){
                            m = kodoImageupload.clientuploadKODO(map, userpath);
                        }else if(key.getStorageType()==5){
                            m2 = LocUpdateImg.ImageuploadLOC(map, userpath,null);
                        }else{
                            System.err.println("未获取到对象存储参数，上传失败。");
                        }
                        Images img = new Images();
                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                        String times = df.format(new Date());
                        System.out.println("上传图片的时间是："+times);
                        if(key.getStorageType()==5){
                            for (Map.Entry<ReturnImage, Integer> entry : m2.entrySet()) {
                                if(key.getStorageType()==5){
                                    if(config.getDomain()!=null){
                                        jsonArray.add(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                                        img.setImgurl(config.getDomain()+"/links/"+entry.getKey().getImgurl());//图片链接
                                    }else{
                                        jsonArray.add(config.getDomain()+"/links/"+entry.getKey().getImgurl());
                                        img.setImgurl("http://"+ IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/links/"+entry.getKey().getImgurl());//图片链接
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
                                img.setAbnormal(0);
                                userService.insertimg(img);
                                long etime = System.currentTimeMillis();
                                System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                            }
                        }else{
                            for (Map.Entry<String, Integer> entry : m.entrySet()) {
                                if(key.getStorageType()==5){
                                    if(config.getDomain()!=null){
                                        jsonArray.add(config.getDomain()+"/links/"+entry.getKey());
                                        img.setImgurl(config.getDomain()+"/links/"+entry.getKey());//图片链接
                                    }else{
                                        jsonArray.add(config.getDomain()+"/links/"+entry.getKey());
                                        img.setImgurl("http://"+ IPPortUtil.getLocalIP()+":"+IPPortUtil.getLocalPort()+"/links/"+entry.getKey());//图片链接
                                    }
                                }else{
                                    jsonArray.add(entry.getKey());
                                    img.setImgurl(entry.getKey());//图片链接
                                }
                                img.setUpdatetime(times);
                                img.setSource(key.getStorageType());
                                if (u == null) {
                                    img.setUserid(0);//用户id
                                } else {
                                    img.setUserid(u.getId());//用户id
                                }
                                img.setSizes((entry.getValue()));
                                img.setImgname(SetText.getSubString(entry.getKey(), key.getRequestAddress() + "/", ""));
                                img.setAbnormal(0);
                                userService.insertimg(img);
                                long etime = System.currentTimeMillis();
                                System.out.println("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
                            }
                        }
                    }else{
                        jsonArray.add(-1);
                    }
                    Print.Normal(jsonArray.toString());
                }
            }else{
                //邮箱或密码不正确，登录失败
                jsonArray.add(-2);
            }
        }else{
            //邮箱、密码为空
            jsonArray.add(-3);
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
        System.out.println(i);
        return i.toString();
    }

}
