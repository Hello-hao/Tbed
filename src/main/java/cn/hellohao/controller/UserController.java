package cn.hellohao.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailConfigService emailConfigService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping("/register")
    @ResponseBody
    public String Register(@Valid User user) {
        //取当前时间
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        JSONObject jsonObject = new JSONObject();
        EmailConfig emailConfig = emailConfigService.getemail();
        Integer countusername = userService.countusername(user.getUsername());
        Integer countmail = userService.countmail(user.getEmail());
        SysConfig sysConfig = sysConfigService.getstate();
        if(sysConfig.getRegister()==1){
            if (countusername == 0 && countmail == 0) {
                String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                String birthder = df.format(new Date());// new Date()为获取当前系统时间
                user.setLevel(1);
                user.setUid(uid);
                user.setBirthder(birthder);
                user.setMemory(updateConfig.getUsermemory());
                user.setGroupid(1);
                //查询是否启用了邮箱验证。
                Config config = configService.getSourceype();
                System.err.println("是否启用了邮箱激活："+emailConfig.getUsing());
                Integer type = 0;
                if(emailConfig.getUsing()==1){
                    user.setIsok(0);
                    //初始化邮箱
                    MimeMessage message = SendEmail.Emails(emailConfig);
                    //注册完发激活链接
                    Thread thread = new Thread() {
                        public void run() {
                            Integer a = SendEmail.sendEmail(message, user.getUsername(), uid, user.getEmail(),emailConfig,config);
                        }
                    };
                    thread.start();
                    type = 1;
                }else{
                    //直接注册
                    user.setIsok(1);
                    type = 2;
                }
                Integer ret = userService.register(user);
                if(ret>0){
//                    UserGroup userGroup = new UserGroup();
//                    User user1 = userService.getUsersMail(uid);
//                    userGroup.setUserid(user1.getId());
//                    userGroup.setGroupid(1);
//                    userGroupService.addusergroup(userGroup);
                }
                jsonObject.put("ret",ret);
                jsonObject.put("zctype",type);
            } else {
                jsonObject.put("ret",-2);
            }
        }else{
            jsonObject.put("ret",-3); //管理员关闭的注册
        }
        return jsonObject.toString();
    }


    @RequestMapping("/login.do")
    @ResponseBody
    public String login( HttpSession httpSession, String email, String password) {
        JSONArray jsonArray = new JSONArray();

        Integer ret = userService.login(email, password);
        if (ret > 0) {
            User user = userService.getUsers(email);
            if (user.getIsok() == 1) {
                httpSession.setAttribute("user", user);
                httpSession.setAttribute("email", user.getEmail());
                httpSession.setAttribute("pass", user.getPassword());
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

    //退出
    @RequestMapping(value = "/exit.do")
    @ResponseBody
    public String exit(Model model, HttpServletRequest request, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        //注销，移除session
        User user = (User) session.getAttribute("user");
        if (user.getEmail() != null && user.getPassword() != null) {
            session.removeAttribute(user.getEmail());
            session.removeAttribute(user.getPassword());
            session.removeAttribute("user");

        }
        //刷新view
        session.invalidate();
        jsonObject.put("exit", 1);

        return jsonObject.toString();
    }

    //邮箱激活
    @RequestMapping(value = "/activation.do", method = RequestMethod.GET)
    public String activation(Model model, HttpServletRequest request, HttpSession session, String activation, String username) {
        Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
        Integer ret = 0;
        User user = userService.getUsersMail(activation);
        model.addAttribute("config", config);
        if (user != null && user.getIsok() == 0) {
            Integer setisok = userService.uiduser(activation);
            model.addAttribute("setisok", ret);
            model.addAttribute("username", username);

            return "isok";
        } else {
            return "redirect:/index";
            //return "isok";
        }

    }

}
