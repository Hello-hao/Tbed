package cn.hellohao.controller;

import cn.hellohao.pojo.Config;
import cn.hellohao.pojo.EmailConfig;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.service.*;
import cn.hellohao.utils.Print;
import cn.hellohao.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/root")
public class AdminRootController {

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

    //返回对象存储界面
    @RequestMapping(value = "/touser")
    public String touser() {
        return "admin/user";
    }

    //返回对象存储界面
    @RequestMapping(value = "tostorage")
    public String tostorage(HttpSession session, Model model, HttpServletRequest request) {
        Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
        Keys  key= keysService.selectKeys(config.getSourcekey());//然后根据类型再查询key
        Boolean b = StringUtils.doNull(key);//判断对象是否有空值
        Integer StorageType = 0;
        if(config.getSourcekey()!=5){
            if(b){
                //key信息
                model.addAttribute("AccessKey", key.getAccessKey());
                model.addAttribute("AccessSecret", key.getAccessSecret());

                model.addAttribute("Endpoint", key.getEndpoint());
                model.addAttribute("Bucketname", key.getBucketname());
                model.addAttribute("RequestAddress", key.getRequestAddress());
                model.addAttribute("StorageType", config.getSourcekey());
            }
            //如果是4就是七牛
            if(config.getSourcekey()==4){
                model.addAttribute("Endpoint2", key.getEndpoint());
            }else{
                model.addAttribute("Endpoint2", 0);
            }
        }else{
            model.addAttribute("StorageType", 5);//切换到本地
            model.addAttribute("Endpoint2", 0);
        }


        return "admin/storageconfig";
    }

    //根据下拉框选的存储源查询对应的key
    @PostMapping("/getkey")
    @ResponseBody
    public String getkey(Integer storageType) {
        JSONArray jsonArray = new JSONArray();
        Keys key = keysService.selectKeys(storageType);
        jsonArray.add(key);
        return jsonArray.toString();
    }

    @PostMapping("/updatekey")
    @ResponseBody
    public String updatekey(Keys key) {
        JSONArray jsonArray = new JSONArray();
        Config config = new Config();
        config.setSourcekey(key.getStorageType());
        Integer val = configService.setSourceype(config);
        if (val > 0) {
            Integer ret = keysService.updateKey(key);
            jsonArray.add(ret);
        } else {
            jsonArray.add(0);
        }
        return jsonArray.toString();
    }

    //刪除用戶
    @PostMapping("/deleuser")
    @ResponseBody
    public String deleuser(HttpSession session, Integer id) {
        JSONArray jsonArray = new JSONArray();
        Integer ret = userService.deleuser(id);
        jsonArray.add(ret);
        return jsonArray.toString();
    }

    //跳转邮箱配置页面
    @RequestMapping(value = "/emailconfig")
    public String emailconfig(Model model) {
        EmailConfig emailConfig = emailConfigService.getemail();
        model.addAttribute("emailConfig",emailConfig);
        return "admin/emailconfig";
    }

    //修改邮箱验证
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
    //跳转系统配置页面
    @RequestMapping(value = "/towebconfig")
    public String towebconfig(Model model) {
        Config config = configService.getSourceype();
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        model.addAttribute("config",config);
        model.addAttribute("updateConfig",updateConfig);
        return "admin/webconfig";
    }
    //修改站点配置
    @PostMapping("/updateconfig")
    @ResponseBody
    public Integer updateconfig(String webname,String explain, String logos,
                                String footed, String links, String notice,String baidu,
                                String domain,String background1,String background2 ) {
        Config config = new Config();
        config.setWebname(webname);
        config.setExplain(explain);
        config.setLogos(logos);
        config.setFooted(footed);
        config.setLinks(links);
        config.setNotice(notice);
        config.setBaidu(baidu);
        config.setDomain(domain);
        config.setBackground1(background1);
        config.setBackground2(background2);
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

}
