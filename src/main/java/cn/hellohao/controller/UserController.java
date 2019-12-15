package cn.hellohao.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.utils.Base64Encryption;
import cn.hellohao.utils.Print;
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
    public String Register(@Valid User u,Integer zctmp) {
        JSONObject jsonObject = new JSONObject();
        if((zctmp-number2)==(istmp2-number2)){
            User user = new User();
            UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
            EmailConfig emailConfig = emailConfigService.getemail();
            Integer countusername = userService.countusername(u.getUsername());
            Integer countmail = userService.countmail(u.getEmail());
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
                    user.setEmail(u.getEmail());
                    user.setUsername(u.getUsername());
                    user.setPassword(Base64Encryption.encryptBASE64(u.getPassword().getBytes()));
                    Config config = configService.getSourceype();
                    System.err.println("是否启用了邮箱激活："+emailConfig.getUsing());
                    Integer type = 0;
                    if(emailConfig.getUsing()==1){
                        user.setIsok(0);
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
                    }
                    jsonObject.put("ret",ret);
                    jsonObject.put("zctype",type);
                } else {
                    jsonObject.put("ret",-2);
                }
            }else{
                jsonObject.put("ret",-3); //管理员关闭的注册
            }
        }else{
            jsonObject.put("ret",-4);//非法注册
        }
        return jsonObject.toString();
    }


    @RequestMapping("/login")
    @ResponseBody
    public String login( HttpSession httpSession, String email, String password,Integer logotmp) {
        JSONArray jsonArray = new JSONArray();
        if((logotmp-number1)==(istmp1-number1)){
            String basepass = Base64Encryption.encryptBASE64(password.getBytes());
            Integer ret = userService.login(email, basepass);
            if (ret > 0) {
                User user = userService.getUsers(email);
                if (user.getIsok() == 1) {
                    httpSession.setAttribute("user", user);
                    httpSession.setAttribute("email", user.getEmail());
                    jsonArray.add(1);
                } else if(ret==-1){
                    jsonArray.add(-1);
                }else {
                    jsonArray.add(-2);
                }
            } else {
                jsonArray.add(0);
            }
        }else{jsonArray.add(-3);//非法登录
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
        Config config = configService.getSourceype();
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
        }
    }
    @PostMapping(value = "/verification")
    @ResponseBody
    public Integer verification(HttpSession session,Integer tmp,Integer type) {
        Random random = new Random();
        if(type==1){
            number1 = random.nextInt(100);
            istmp1 = tmp;
            Print.Normal(tmp-number1);
        }
        if(type==2){
            number2 = random.nextInt(100);
            istmp2 = tmp;
            Print.Normal(tmp-number2);
        }
        return 1;
    }

    private Integer number1;
    private Integer istmp1;
    private Integer number2;
    private Integer istmp2;
}
