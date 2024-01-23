package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.NewSendEmail;
import cn.hellohao.utils.Print;
import cn.hellohao.utils.SetFiles;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/root")
public class AdminRootController {
    @Autowired
    private ConfdataService confdataService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailConfigService emailConfigService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private ImgreviewService imgreviewService;
    @Autowired
    private AppClientService appClientService;
    @Autowired
    private NOSImageupload nOSImageupload;
    @Autowired
    private OSSImageupload ossImageupload;
    @Autowired
    private USSImageupload ussImageupload;
    @Autowired
    private KODOImageupload kodoImageupload;
    @Autowired
    private COSImageupload cosImageupload;
    @Autowired
    private FtpServiceImpl ftpService;
    @Autowired
    private S3Imageupload s3Imageupload;
    @Autowired
    private WebDAVImageupload webDAVImageupload;


    @PostMapping(value = "/getUserList")
    @ResponseBody
    public Msg getUserList(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        try{
            JSONObject jsonObj = JSONObject.parseObject(data);
            Integer pageNum = jsonObj.getInteger("pageNum");
            Integer pageSize = jsonObj.getInteger("pageSize");
            String queryText = jsonObj.getString("queryText");
            PageHelper.startPage(pageNum, pageSize);
            List<User> users = userService.getuserlist(queryText);
            PageInfo<User> rolePageInfo = new PageInfo<>(users);
            msg.setData(rolePageInfo);
        }catch (Exception e){
            msg.setCode("500");
        }
        return msg;
    }


    @PostMapping(value = "/updateUserInfo")
    @ResponseBody
    public Msg updateUserInfo(@RequestParam(value = "data", defaultValue = "") String data) {
        final Msg msg = new Msg();
        try {
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            JSONObject jsonObj = JSONObject.parseObject(data);
            Integer id = jsonObj.getInteger("id");
            String email = jsonObj.getString("email");
            Long memory = jsonObj.getLong("memory");
            Integer groupid = jsonObj.getInteger("groupid");
            Integer isok = jsonObj.getInteger("isok");
            if (memory < 0 || memory > 1048576L) {
                msg.setCode("500");
                msg.setInfo("容量不得超过1048576");
                return msg;
            }
            final User user = new User();
            final User user2 = new User();
            user2.setId(id);
            User userInfo = userService.getUsers(user2);
            user.setId(id);
            user.setEmail(email);
            user.setMemory(Long.toString(memory * 1024 * 1024));
            user.setGroupid(groupid);
            if (userInfo.getLevel() == 1) {
                user.setIsok(isok == 1 ? 1 : -1);
            }
            userService.changeUser(user);
            msg.setInfo("修改成功");
        } catch (Exception e) {
            msg.setCode("500");
            msg.setInfo("修改失败");
            e.printStackTrace();
        }
        return msg;
    }


    @PostMapping("/disableUser")
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
                if (u2.getLevel() == 1) {
                    User user = new User();
                    user.setId(userIdList.getInteger(i));
                    user.setIsok(-1);
                    userService.changeUser(user);
                }

            }
            msg.setInfo("所选用户已被禁用");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setInfo("系统错误");
            msg.setCode("500");
        }
        return msg;
    }

    @PostMapping("/deleUser")
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
                if (user.getLevel() == 1) {
                    userService.deleuser(userIdList.getInteger(i));
                } else {
                    b = true;
                }
            }
            if (b && userIdList.size() == 1) {
                msg.setInfo("管理员账户不可删除");
            } else {
                msg.setInfo("用户已删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setInfo("系统错误");
            msg.setCode("500");
        }
        return msg;
    }

    @PostMapping("/getKeysList")
    @ResponseBody
    public Msg getKeysList() {
        Msg msg = new Msg();
        List<Keys> list = keysService.getKeys();
        msg.setData(list);
        return msg;
    }

    @PostMapping("/LoadInfo")
    @ResponseBody
    public Msg LoadInfo(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        try {
            JSONObject jsonData = JSONObject.parseObject(data);
            Integer keyId = jsonData.getInteger("keyId");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", keyId);
            Keys key = keysService.selectKeys(keyId);
            Integer ret = 0;
            if (key.getStorageType() == 1) {
                ret = nOSImageupload.Initialize(key);
            } else if (key.getStorageType() == 2) {
                ret = ossImageupload.Initialize(key);
            } else if (key.getStorageType() == 3) {
                ret = ussImageupload.Initialize(key);
            } else if (key.getStorageType() == 4) {
                ret = kodoImageupload.Initialize(key);
            } else if (key.getStorageType() == 6) {
                ret = cosImageupload.Initialize(key);
            } else if (key.getStorageType() == 7) {
                ret = ftpService.Initialize(key);
            } else if (key.getStorageType() == 8) {
                ret = s3Imageupload.Initialize(key);
            }else if (key.getStorageType() == 9) {
                ret = webDAVImageupload.Initialize(key);
            }
            Long l = imgService.getsourcememory(keyId);
            jsonObject.put("isok", ret);
            jsonObject.put("storagetype", key.getStorageType());
            if (l == null) {
                jsonObject.put("usedCapacity", 0);
            } else {
                jsonObject.put("usedCapacity", SetFiles.readableFileSize(l));
            }
            msg.setData(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("500");
        }
        return msg;
    }


    @PostMapping("/updateStorage")
    @ResponseBody
    public Msg updateStorage(@RequestParam(value = "data", defaultValue = "") String data) {
        JSONObject jsonObj = JSONObject.parseObject(data);
        Keys keys = JSON.toJavaObject(jsonObj,Keys.class);
        Msg msg = keysService.updateKey(keys);
        return msg;
    }

    @PostMapping("/getStorageById")
    @ResponseBody
    public Msg getselectkey(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        Integer keyid = jsonObj.getInteger("id");
        Keys keys = keysService.selectKeys(keyid);
        msg.setData(keys);
        return msg;
    }

    @PostMapping("/getSettingConfig")
    @ResponseBody
    public Msg getSettingConfig(@RequestParam(value = "data", defaultValue = "") String data) {
        final Msg msg = new Msg();
        final JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        User u = (User) subject.getPrincipal();
        try {
            UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
            final Confdata confdata = confdataService.selectConfdata("config");
            SysConfig sysConfig = sysConfigService.getstate();
            AppClient appClientData = appClientService.getAppClientData("app");
            uploadConfig.setUsermemory(Long.toString(Long.valueOf(uploadConfig.getUsermemory()) / 1024 / 1024));
            uploadConfig.setVisitormemory(Long.toString(Long.valueOf(uploadConfig.getVisitormemory()) / 1024 / 1024));
            uploadConfig.setFilesizetourists(Long.toString(Long.valueOf(uploadConfig.getFilesizetourists()) / 1024 / 1024));
            uploadConfig.setFilesizeuser(Long.toString(Long.valueOf(uploadConfig.getFilesizeuser()) / 1024 / 1024));
            jsonObject.put("uploadConfig", uploadConfig);
            jsonObject.put("config", JSONObject.parseObject(confdata.getJsondata()));
            jsonObject.put("sysConfig", sysConfig);
            jsonObject.put("appClient", appClientData);
            msg.setData(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("操作失败");
        }
        return msg;
    }


    @PostMapping("/updateConfig")
    @ResponseBody
    public Msg updateConfig(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            UploadConfig uploadConfig = JSON.toJavaObject((JSON) jsonObject.get("uploadConfig"), UploadConfig.class);
            String vm = uploadConfig.getVisitormemory();
            if ((Long.valueOf(vm) < -1) || Long.valueOf(vm) > 104857600 || Long.valueOf(uploadConfig.getFilesizetourists()) < 0 || Long.valueOf(uploadConfig.getFilesizetourists()) > 5120
                    || Long.valueOf(uploadConfig.getUsermemory()) < 0 || Long.valueOf(uploadConfig.getUsermemory()) > 1048576
                    || Long.valueOf(uploadConfig.getFilesizeuser()) < 0 || Long.valueOf(uploadConfig.getFilesizeuser()) > 5120) {
                msg.setInfo("你输入的值不正确");
                msg.setCode("500");
                return msg;
            }
//            Config config = JSON.toJavaObject((JSON) jsonObject.get("config"), Config.class);
            SysConfig sysConfig = JSON.toJavaObject((JSON) jsonObject.get("sysConfig"), SysConfig.class);
            AppClient appClient = JSON.toJavaObject((JSON) jsonObject.get("appClient"), AppClient.class);
            if (Integer.valueOf(vm) == -1) {
                uploadConfig.setVisitormemory("-1");
            } else {
                uploadConfig.setVisitormemory(Long.toString(Long.valueOf(uploadConfig.getVisitormemory()) * 1024 * 1024));
            }
            uploadConfig.setFilesizetourists(Long.toString(Long.valueOf(uploadConfig.getFilesizetourists()) * 1024 * 1024));
            uploadConfig.setUsermemory(Long.toString(Long.valueOf(uploadConfig.getUsermemory()) * 1024 * 1024));
            uploadConfig.setFilesizeuser(Long.toString(Long.valueOf(uploadConfig.getFilesizeuser()) * 1024 * 1024));
            uploadConfigService.setUpdateConfig(uploadConfig);
            Confdata confdata = new Confdata();
            confdata.setKey("config");
            confdata.setJsondata(jsonObject.getJSONObject("config").toJSONString());
            confdataService.updateConfdata(confdata);
            sysConfigService.setstate(sysConfig);
            if (!appClient.getIsuse().equals("on")) {
                appClient.setIsuse("off");
            }
            appClientService.editAppClientData(appClient);
            msg.setInfo("配置保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setInfo("操作出现异常");
            msg.setCode("500");
        }
        return msg;
    }


    @PostMapping(value = "/getOrderConfig")
    @ResponseBody
    public Msg emailconfig() {
        final Msg msg = new Msg();
        EmailConfig emailConfig = null;
        Imgreview imgreview = null;
        try {
            final JSONObject jsonObject = new JSONObject();
            emailConfig = emailConfigService.getemail();
            imgreview = imgreviewService.selectByPrimaryKey(1);
            jsonObject.put("emailConfig", emailConfig);
            jsonObject.put("imgreview", imgreview);
            msg.setData(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("获取相关配置信息失败");
        }
        return msg;
    }


    @PostMapping("/updateEmailConfig")
    @ResponseBody
    public Msg updateemail(@RequestParam(value = "data", defaultValue = "") String data) {
        final Msg msg = new Msg();
        try {
            JSONObject jsonObj = JSONObject.parseObject(data);
            EmailConfig emailConfig = JSON.toJavaObject(jsonObj, EmailConfig.class);
            if (null == emailConfig.getId() || null == emailConfig.getEmailname() || null == emailConfig.getEmailurl() || null == emailConfig.getEmails()
                    || null == emailConfig.getEmailkey() || null == emailConfig.getPort() || null == emailConfig.getUsing()
                    || emailConfig.getEmailname().equals("") || emailConfig.getEmailurl().equals("") || emailConfig.getEmails().equals("")
                    || emailConfig.getEmailkey().equals("") || emailConfig.getPort().equals("")) {
                msg.setCode("110400");
                msg.setInfo("各参数不能为空");
                return msg;
            }
            emailConfigService.updateemail(emailConfig);
            msg.setInfo("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("保存过程出现错误");
        }
        return msg;
    }

    @PostMapping("/mailTest")
    @ResponseBody
    public Msg mailTest(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        String tomail = jsonObj.getString("tomail");
        EmailConfig emailConfig = JSON.toJavaObject(jsonObj, EmailConfig.class);
        if (null == emailConfig.getEmails() || null == emailConfig.getEmailkey() || null == emailConfig.getEmailurl()
                || null == emailConfig.getPort() || null == emailConfig.getEmailname() || null == tomail) {
            msg.setCode("110400");
            msg.setInfo("邮箱配置参数不能为空");
        } else {
            msg = NewSendEmail.sendTestEmail(emailConfig, tomail);
        }
        return msg;
    }

    @PostMapping("/sysVersion")
    @ResponseBody
    public Msg sysupdate(@RequestParam(value = "data", defaultValue = "") String data) {
        final Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        String  dates = jsonObj.getString("dates");
        HashMap<String, Object> paramMap = new HashMap<>();
        String urls = "http://check.hellohao.cn:8090/getNoticeText";
        JSONObject jsonObject =null;
        try {
            URL u = new URL(urls);
            HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();
            uConnection.connect();
            if(uConnection.getResponseCode()==200){
                    jsonObject =JSONObject.parseObject(HttpUtil.get(urls));
                    String openv = jsonObject.getString("openv");
                    String opentext = jsonObject.getString("opentext");
                    JSONObject versionJson = new JSONObject();
                    if(Double.valueOf(openv)>Double.valueOf(dates)){
                        versionJson.put("code","110200");
                        versionJson.put("newVersion",openv);
                        versionJson.put("versionMsg",opentext);
                        msg.setCode("110200");
                        msg.setData(versionJson);
                    }else{
                        versionJson.put("code","110300");
                        versionJson.put("newVersion",openv);
                        versionJson.put("versionMsg","当前已是最新版本");
                        msg.setCode("110300");
                        msg.setData(versionJson);
                    }
            }else{
                msg.setCode("110500");
                msg.setData("未获取到更新状态");
            }
            uConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Print.warning("connect failed");
            msg.setCode("110500");
            msg.setData("未获取到更新状态");
        }
        return msg;
    }


}
