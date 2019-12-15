package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.GetCurrentSource;
import cn.hellohao.utils.Print;
import cn.hellohao.utils.StringUtils;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/admin/root")
public class AdminRootController {
    @Autowired
    private NOSImageupload nOSImageupload;
    @Autowired
    private ConfigService configService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailConfigService emailConfigService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private GroupService groupService;

    @Value("${systemupdate}")
    private String systemupdate;

    @RequestMapping(value = "/touser")
    public String touser() {
        return "admin/user";
    }

    @RequestMapping(value = "tostorage")
    public String tostorage(HttpSession session, Model model, HttpServletRequest request) {
        User u = (User) session.getAttribute("user");
        Integer Sourcekey = GetCurrentSource.GetSource(u.getId());
        Keys  key= keysService.selectKeys(Sourcekey);
        Boolean b = StringUtils.doNull(Sourcekey,key);
        Integer StorageType = 0;
        if(Sourcekey!=5){
            if(b){
                model.addAttribute("AccessKey", key.getAccessKey());
                model.addAttribute("AccessSecret", key.getAccessSecret());
                model.addAttribute("Endpoint", key.getEndpoint());
                model.addAttribute("Bucketname", key.getBucketname());
                model.addAttribute("RequestAddress", key.getRequestAddress());
                model.addAttribute("StorageType", Sourcekey);
            }
            if(Sourcekey==4){
                model.addAttribute("Endpoint2", key.getEndpoint());
            }else{
                model.addAttribute("Endpoint2", 0);
            }
        }else{
            model.addAttribute("StorageType", 5);
            model.addAttribute("Endpoint2", 0);
        }
        return "admin/storageconfig";
    }

    @PostMapping("/getkey")
    @ResponseBody
    public String getkey(Integer storageType) {
        JSONArray jsonArray = new JSONArray();
        Keys key = keysService.selectKeys(storageType);
        jsonArray.add(key);
        return jsonArray.toString();
    }

    @PostMapping("/getkeyourceype")
    @ResponseBody
    public Integer getkeyourceype(HttpSession session) {
        User u = (User) session.getAttribute("user");
        Integer Sourcekey = GetCurrentSource.GetSource(u.getId());
        Integer ret = 0;
        if(Sourcekey!=null){
            ret = Sourcekey;
        }
        return ret;
    }

    @PostMapping("/updatekey")
    @ResponseBody
    public String updatekey(Keys key) {
        JSONArray jsonArray = new JSONArray();
        Config config = new Config();
        config.setSourcekey(key.getStorageType());
        Integer val = configService.setSourceype(config);
        Integer ret = -2;
        //修改完初始化
        if(key.getStorageType()==1){
            ret =nOSImageupload.Initialize(key);//实例化网易
        }else if (key.getStorageType()==2){
            ret = OSSImageupload.Initialize(key);
        }else if(key.getStorageType()==3){
            ret = USSImageupload.Initialize(key);
        }else if(key.getStorageType()==4){
            ret = KODOImageupload.Initialize(key);
        }else if(key.getStorageType()==6){
            ret = COSImageupload.Initialize(key);
        }else if(key.getStorageType()==7){
            ret = FTPImageupload.Initialize(key);
        }
        else{
            Print.Normal("为获取到存储参数，或者使用存储源是本地的。");
        }
        if(ret>0){
            ret = keysService.updateKey(key);
        }

        //-1 对象存储有参数为空,初始化失败
        //0，保存失败
        //1 正确
        jsonArray.add(ret);
        return jsonArray.toString();
    }

    @PostMapping("/deleuser")
    @ResponseBody
    public String deleuser(HttpSession session, Integer id) {
        JSONArray jsonArray = new JSONArray();
        User u = (User) session.getAttribute("user");
        if(u.getId()==id){
            jsonArray.add("-1");
        }else{
            Integer ret = userService.deleuser(id);
            jsonArray.add(ret);
        }
        return jsonArray.toString();
    }

    @RequestMapping(value = "/emailconfig")
    public String emailconfig(Model model) {
        EmailConfig emailConfig = emailConfigService.getemail();
        model.addAttribute("emailConfig",emailConfig);
        return "admin/emailconfig";
    }

    @PostMapping("/updateemail")
    @ResponseBody
    public Integer updateemail(HttpSession session,String emails, String emailkey, String emailurl, String port, String emailname, Integer using ) {
        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setEmailname(emailname);
        emailConfig.setEmails(emails);
        emailConfig.setEmailkey(emailkey);
        emailConfig.setEmailurl(emailurl);
        emailConfig.setPort(port);
        emailConfig.setUsing(using);
        Integer ret = emailConfigService.updateemail(emailConfig);
        return ret;
    }

    @RequestMapping(value = "/towebconfig")
    public String towebconfig(HttpSession session,Model model) {
        Config config = configService.getSourceype();
        User u = (User) session.getAttribute("user");
        Integer Sourcekey = GetCurrentSource.GetSource(u.getId());
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        SysConfig sysConfig = sysConfigService.getstate();
        model.addAttribute("config",config);
        model.addAttribute("updateConfig",updateConfig);
        model.addAttribute("sysconfig",sysConfig);
        model.addAttribute("group",Sourcekey);
        return "admin/webconfig";
    }
    @PostMapping("/updateconfig")
    @ResponseBody
    public Integer updateconfig(Config config ) {
        Integer ret = configService.setSourceype(config);
        return ret;
    }
    //修改上传配置
    @PostMapping("/scconfig")
    @ResponseBody
    public Integer scconfig(UploadConfig updateConfig) {
        Integer ret = uploadConfigService.setUpdateConfig(updateConfig);
        return ret;
    }

    @PostMapping("/setisok")
    @ResponseBody
    public String setisok(HttpSession session, User user) {
        Integer ret = userService.setisok(user);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(ret);
        return jsonArray.toString();
    }

    @PostMapping("/setmemory")
    @ResponseBody
    public String setmemory(HttpSession session, User user) {
        Integer ret = userService.setmemory(user);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(ret);
        return jsonArray.toString();
    }

    @PostMapping("/setstate")
    @ResponseBody
    public Integer setstate(HttpSession session, SysConfig sysConfig) {
        Integer ret =-1;
        ret = sysConfigService.setstate(sysConfig);
        return ret;
    }

    @RequestMapping(value = "/modifyuser")
    public String modifyuser(Model model,String uid,Integer id) {
    User user = userService.getUsersMail(uid);
        model.addAttribute("memory",user.getMemory());
        model.addAttribute("groupid",user.getGroupid());
        model.addAttribute("uid",uid);
        model.addAttribute("id",id);
        return "admin/modifyuser";
    }

    @PostMapping("/settstoragetype")
    @ResponseBody
    public Integer settstoragetype(Integer storagetype) {
        Config config = new Config();
        config.setSourcekey(storagetype);
        Integer val = configService.setSourceype(config);
        return val;
    }

    @RequestMapping("/about")
    public String about(HttpSession session,Model model ) {
        //Integer ret = uploadConfigService.setUpdateConfig(updateConfig);
        User u = (User) session.getAttribute("user");
        model.addAttribute("level",u.getLevel());
        model.addAttribute("systemupdate",systemupdate);
        return "admin/about";
    }

    @PostMapping("/sysupdate")
    @ResponseBody
    public Integer sysupdate(String  dates) {
        HashMap<String, Object> paramMap = new HashMap<>();
        String urls ="http://tc.hellohao.cn/systemupdate";
        paramMap.put("dates",dates);
        String result= HttpUtil.post(urls, paramMap);
        return Integer.parseInt( result );
    }

}
