package cn.mq.tbed.controller;

import cn.mq.tbed.exception.StorageSourceInitException;
import cn.mq.tbed.utils.Print;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.net.SocketException;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/6 8:57
 */

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler
    //@ResponseBody
    public ModelAndView test(StorageSourceInitException ssie){
        ModelAndView modelAndView = new ModelAndView("index");
        Print.Normal("异常拦截成功："+ ssie.getMessage());
        modelAndView.addObject("error",ssie.getMessage());
        //返回错误信息，并显示给用户
        return modelAndView;
    }

    @ExceptionHandler
    public String test4(SocketException e){
        ModelAndView modelAndView = new ModelAndView("/index");
        Print.Normal("存储源配置不正确，初始化失败"+ e.getMessage());
        e.printStackTrace();
        modelAndView.addObject("error",e.getMessage());
        //返回错误信息，并显示给用户
        return e.getMessage();
    }
    @ExceptionHandler
    public String test3(Exception e){
        ModelAndView modelAndView = new ModelAndView("/index");
        Print.Normal("系统内部错误："+ e.getMessage());
        e.printStackTrace();
        modelAndView.addObject("error",e.getMessage());
        //返回错误信息，并显示给用户
        return e.getMessage();
    }

    @ExceptionHandler
    public String test3(HttpRequestMethodNotSupportedException e,Model model){

        ModelAndView modelAndView = new ModelAndView("/index");
        Print.Normal("URL访问类型不正确："+ e.getMessage());
        //e.printStackTrace();
        modelAndView.addObject("error","URL访问类型不正确。");
        model.addAttribute("error","URL访问类型不正确。");
        //返回错误信息，并显示给用户
        //return e.getMessage();
        return "exception";
    }



//    @ExceptionHandler
//    public String methodArgumentNotValid(BindException e) {
//        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
//        StringBuilder errorMessage = new StringBuilder();
//        for (int i = 0; i < allErrors.size(); i++) {
//            ObjectError error = allErrors.get(i);
//            errorMessage.append(error.getDefaultMessage());
//            if (i != allErrors.size() - 1) {
//                errorMessage.append(", ");
//            }
//        }
//        // do something
//        System.out.println(errorMessage.toString());// 这里面是错误信息
//        return errorMessage.toString();
//    }

}
