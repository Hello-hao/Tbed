package cn.mq.tbed.interceptor;

import cn.mq.tbed.pojo.User;
import cn.mq.tbed.service.impl.UserServiceImpl;
import cn.mq.tbed.utils.SpringContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
public class InterceptorConfigWeb implements HandlerInterceptor {
    //private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);

    /**
     * 进入controller层之前拦截请求
     */
    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserServiceImpl userService = SpringContextHolder.getBean(UserServiceImpl.class);
            HttpSession session = request.getSession();
        //这里的User是登陆时放入session的
        User user = (User) session.getAttribute("user");
        if(user==null){
            Cookie[] cookies = request.getCookies();
            String Hellohao_UniqueUserKey = "";
//            for (Cookie cookie : cookies) {
//                if(cookie.getName().equals("Hellohao_UniqueUserKey") && Hellohao_UniqueUserKey.equals("")){
//                    Hellohao_UniqueUserKey = URLDecoder.decode(cookie.getValue(), "GBK");
//                }
//            }
//
//            if(Hellohao_UniqueUserKey!=null && !Hellohao_UniqueUserKey.equals("")){
//                //String basepass = Base64Encryption.encryptBASE64(pass.getBytes());
//                Integer ret = userService.login(null, null,Hellohao_UniqueUserKey);
//                if (ret > 0) {
//                    User u = userService.getUsersMail(Hellohao_UniqueUserKey);
//                    if (u.getIsok() == 1) {
//                        session.setAttribute("user", u);
//                        session.setAttribute("email", u.getEmail());
//                        //request.getRequestDispatcher("/admin/goadmin").forward(request, response);
//                    }
//                }
//            }
        }

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

}
