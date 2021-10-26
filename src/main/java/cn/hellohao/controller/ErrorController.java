package cn.hellohao.controller;

import cn.hellohao.pojo.Msg;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 错误页面拦截
 * */

@Controller
class MainsiteErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 401){
            return "401";
        }else if(statusCode == 404){
            return "404";
        }else if(statusCode == 403){
            return "403";
        }else{
            return "500";
        }

    }
    //认证失败
    @RequestMapping("/jurisError")
    @ResponseBody
    public Msg jurisError(HttpServletRequest request){
        Msg msg = new Msg();
        msg.setCode("4031");
        msg.setInfo("Authentication request failed");
        return msg;
    }


}