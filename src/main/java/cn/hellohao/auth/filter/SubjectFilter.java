package cn.hellohao.auth.filter;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.pojo.User;
import cn.hellohao.service.impl.UserServiceImpl;
import cn.hellohao.utils.SpringContextHolder;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/16 17:43
 */

public class SubjectFilter extends BasicHttpAuthenticationFilter {

    public static String WEBHOST = null;
    private String CODE ="000";

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        UserServiceImpl userService = SpringContextHolder.getBean(UserServiceImpl.class);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse =(HttpServletResponse) response;
        String serviceName = httpServletRequest.getServletPath();//获取接口
        String Users_Origin = httpServletRequest.getHeader("usersOrigin");
        String token = httpServletRequest.getHeader("Authorization");
        if(httpServletRequest.getMethod().equals("POST") && !serviceName.contains("/api") && !serviceName.contains("/verifyCode")
                && !serviceName.contains("/getClientVersion") && !serviceName.contains("/client")){
            try{
                if(Users_Origin.compareTo(SecureUtil.md5(WEBHOST))!=0){
                    System.out.println("前端域名校验未通过");
                    System.out.println("request-MD5:"+Users_Origin);
                    System.out.println("配置文件-MD5:"+SecureUtil.md5(WEBHOST));
                    System.out.println("配置Host:"+WEBHOST);
                    this.CODE = "406";
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                this.CODE = "500";
                return false;
            }
        }

        if(serviceName.contains("/client")){
            token = httpServletRequest.getHeader("jwttoken");
            String userToken = httpServletRequest.getHeader("userToken");
            User user = new User();
            user.setToken(userToken);
            User userData = userService.loginByToken(userToken);
            if(null==userData){
                this.CODE = "110404";
                return false;
            }else{
                if(userData.getIsok()<1){
                    this.CODE = "110403";
                    return false;
                }
                Subject sub = SecurityUtils.getSubject();
                User u = (User) sub.getPrincipal();
                if(null==u){
                    httpServletRequest.getSession().setAttribute("user",userData);
                    Subject subject = SecurityUtils.getSubject();
                    UsernamePasswordToken tokenOBJ = new UsernamePasswordToken(userData.getEmail(),userData.getPassword());
                    tokenOBJ.setRememberMe(true);
                    subject.login(tokenOBJ);
                    SecurityUtils.getSubject().getSession().setTimeout(3600000);
                    User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
                }
            }
        }

//        String token = httpServletRequest.getHeader("Authorization");
        JSONObject jsonObject = JWTUtil.checkToken(token);
        if(!jsonObject.getBoolean("check")){
            if(!serviceName.contains("admin") || serviceName.contains("admin/client")){
                return true;
            }else{
                this.CODE = "403";
                return false;
            }
        }else{
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if(user==null){
                UsernamePasswordToken tokenOBJ = new UsernamePasswordToken(jsonObject.getString("email"),jsonObject.getString("password"));
                tokenOBJ.setRememberMe(true);
                try {
                    subject.login(tokenOBJ);
                    SecurityUtils.getSubject().getSession().setTimeout(3600000);//一小时
                } catch (Exception e) {
                    this.CODE = "403";
                    return false;
                }
            }else{
                if(null!=user){
                    try{
                        if(null != user.getId()){
                            if(userService.getUsers(user).getIsok()<1){
                                subject.logout();
                                this.CODE = "403";
                                return false;
                            }
                        }
                    }catch (Exception e){
                        System.out.println("拦截器判断用户isOK的时候报错了");
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue)  {
        String info = "未知错误";
        try {
            if(this.CODE.equals("406")){
                info = "前端域名配置不正确";
            }else if(this.CODE.equals("403")){
                info = "当前用户无权访问该请求";
            }else if(this.CODE.equals("402")){
                info = "当前web请求不合规";
            }else if(this.CODE.equals("110403")){
                info = "该账户暂时无法使用";
            }else if(this.CODE.equals("110404")){
                info = "用户未找到";
            }
            System.err.println("拦截器False-"+info);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            PrintWriter writer = httpServletResponse.getWriter();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",this.CODE);
            jsonObject.put("info",info);
            writer.write(jsonObject.toJSONString());
            writer.flush();
        } catch (Exception e) {
            System.out.println("返回token验证失败403请求，报异常了");
        }

        return false;
    }

}