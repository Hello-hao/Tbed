package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.GetCurrentSource;
import cn.hellohao.utils.Print;
import cn.hellohao.utils.StringUtils;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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




    @PostMapping(value = "/getUserList")//new selectusertable
    @ResponseBody
    public Map<String, Object> getUserList(@RequestParam(value = "data", defaultValue = "") String data) {
        JSONObject jsonObj = JSONObject.parseObject(data);
        Integer pageNum = jsonObj.getInteger("pageNum");
        Integer pageSize = jsonObj.getInteger("pageSize");
        String queryText = jsonObj.getString("queryText");
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userService.getuserlist(queryText);
        PageInfo<User> rolePageInfo = new PageInfo<>(users);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", rolePageInfo.getTotal());
        map.put("users", rolePageInfo.getList());
        return map;

    }



    @PostMapping(value = "/updateUserInfo")//new
    @ResponseBody
    public Msg updateUserInfo(@RequestParam(value = "data", defaultValue = "") String data) {
        final Msg msg = new Msg();
        try{
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            JSONObject jsonObj = JSONObject.parseObject(data);
            Integer id = jsonObj.getInteger("id");
            String email = jsonObj.getString("email");
            Integer memory = jsonObj.getInteger("memory");
            Integer groupid = jsonObj.getInteger("groupid");
            Integer isok = jsonObj.getInteger("isok");
            final User user = new User();
            final User user2 = new User();
            user2.setId(id);
            User userInfo = userService.getUsers(user2);
            user.setId(id);
            user.setEmail(email);
            user.setMemory(Integer.toString(memory*1024*1024));
            user.setGroupid(groupid);
            if(userInfo.getLevel()==1){
                user.setIsok(isok==1?1:-1);
            }
            userService.changeUser(user);
            msg.setInfo("修改成功");
        }catch (Exception e){
            msg.setCode("500");
            msg.setInfo("修改失败");
            e.printStackTrace();
        }
        return msg;
    }


    @PostMapping("/disableUser")//new
    @ResponseBody
    public Msg disableUser(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        try {
            JSONObject jsonObj = JSONObject.parseObject(data);
            JSONArray userIdList = jsonObj.getJSONArray("arr");
            for (int i = 0; i < userIdList.size(); i++) {
                User u = new User();
                u.setId(userIdList.getInteger(i));
                User u2 = userService.getUsers(u);
                if(u2.getLevel()==1){
                    User user = new User();
                    user.setId(userIdList.getInteger(i));
                    user.setIsok(-1);
                    userService.changeUser(user);
                }

            }
            msg.setInfo("所选用户已被禁用");
        }catch (Exception e){
            e.printStackTrace();
            msg.setInfo("系统错误");
            msg.setCode("500");
        }
        return msg;
    }

    @PostMapping("/deleUser")//new
    @ResponseBody
    public Msg deleuser(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        try {
            JSONObject jsonObj = JSONObject.parseObject(data);
            JSONArray userIdList = jsonObj.getJSONArray("arr");
            boolean b = false;
            for (int i = 0; i < userIdList.size(); i++) {
                User u = new User();
                u.setId(userIdList.getInteger(i));
                User user = userService.getUsers(u);
                if(user.getLevel()==1){
                    userService.deleuser(userIdList.getInteger(i));
                }else{
                    b = true;
                }
            }
            if(b && userIdList.size()==1){
                msg.setInfo("管理员账户不可删除");
            }else {
                msg.setInfo("用户已删除成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            msg.setInfo("系统错误");
            msg.setCode("500");
        }
        return msg;
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
        JSONArray jsonArray = new JSONArray();
        User u = (User) session.getAttribute("user");
        if(u.getId()==user.getId()){
            jsonArray.add(-2);
        }else{
            Integer ret = userService.setisok(user);
            jsonArray.add(ret);
        }
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

    @PostMapping("/tocheck")
    @ResponseBody
    public JSONObject tocheck() {
        JSONObject jsonObject = new JSONObject();
            List<Keys> keylist = keysService.getKeys();
            for (Keys key : keylist) {
                if(key.getStorageType()!=0 && key.getStorageType()!=null){
                    int ret =0;
                    if(key.getStorageType()==1){
                        ret =NOSImageupload.Initialize(key);
                        jsonObject.put(key.getStorageType().toString(),ret);
                    }else if (key.getStorageType()==2){
                        ret =OSSImageupload.Initialize(key);
                        jsonObject.put(key.getStorageType().toString(),ret);
                    }else if(key.getStorageType()==3){
                        ret = USSImageupload.Initialize(key);
                        jsonObject.put(key.getStorageType().toString(),ret);
                    }else if(key.getStorageType()==4){
                        ret = KODOImageupload.Initialize(key);
                        jsonObject.put(key.getStorageType().toString(),ret);
                    }else if(key.getStorageType()==6){
                        ret = COSImageupload.Initialize(key);
                        jsonObject.put(key.getStorageType().toString(),ret);
                    }else if(key.getStorageType()==7){
                        ret = FTPImageupload.Initialize(key);
                        jsonObject.put(key.getStorageType().toString(),ret);
                    }else if(key.getStorageType()==8){
                        ret = UFileImageupload.Initialize(key);
                        jsonObject.put(key.getStorageType().toString(),ret);
                    }
                }
            }
        jsonObject.put("5",1);
        return jsonObject;
    }

}
