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
    @ResponseBody
    public Msg handleError(HttpServletRequest request){
        Msg msg = new Msg();
        msg.setExceptions("Error intercepted by Hellohao");
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 401){
            msg.setCode("401");
            msg.setInfo("Request error for 401");
            return msg;
        }else if(statusCode == 404){
            msg.setCode("404");
            msg.setInfo("Request not found 404");
            return msg;
        }else if(statusCode == 403){
            msg.setCode("403");
            msg.setInfo("Request error for 403");
            return msg;
        }else{
            msg.setCode("500");
            msg.setInfo("System internal error");
            return msg;
        }

    }


}