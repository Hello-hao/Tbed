package cn.hellohao.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hellohao.pojo.User;
import cn.hellohao.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("/register.do")
	@ResponseBody
	public String Register(User  user) {
		//取当前时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String birthder = df.format(new Date());// new Date()为获取当前系统时间
        user.setLevel(1);
        user.setBirthder(birthder);
		Integer ret = userService.register(user);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(ret);
		return jsonArray.toString();
	}
	
	@RequestMapping("/login.do")
	@ResponseBody
	public String login(HttpServletRequest request,HttpSession httpSession,String email,String password) {
		JSONArray jsonArray = new JSONArray();

		Integer ret = userService.login(email, password);
		if(ret>0) {
			jsonArray.add(1);
			User user = userService.getUsers(email);
			httpSession.setAttribute("user",user);
			httpSession.setAttribute("email",user.getEmail());
			httpSession.setAttribute("pass",user.getPassword());

			//登录成功之后把账号密码存入session
			
		}else {
			jsonArray.add(0);
		}
		return jsonArray.toString();
	}
	//退出
    @RequestMapping(value="/exit.do")
    @ResponseBody
    public String exit(Model model,HttpServletRequest request,HttpSession session){
    	JSONObject jsonObject = new JSONObject();
    	//注销，移除session
    	User user = (User) session.getAttribute("user");
    	if(user.getEmail()!=null&&user.getPassword()!=null) {
    		session.removeAttribute(user.getEmail());
            session.removeAttribute(user.getPassword());
          //刷新view
            session.invalidate();
            jsonObject.put("exit", 1);
    	}

    	return jsonObject.toString();
    }



}
