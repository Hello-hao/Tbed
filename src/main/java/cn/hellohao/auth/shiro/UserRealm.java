package cn.hellohao.auth.shiro;

import cn.hellohao.pojo.User;
import cn.hellohao.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/*
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/3 10:39
 * 自定义UserRealm

*/
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        System.out.println("..///执行了授权方法");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        ArrayList<String> roleList = new ArrayList();
        if(user.getLevel()==2){
            //赋予管理员权限
//            info.addRole("admin");
            roleList.add("admin");
            roleList.add("user");
//            info.addStringPermission("user:one");
        }else{
//            info.addRole("user");
            roleList.add("user");
        }
        info.addRoles(roleList);
        return info;
    }

    //认证(登录)
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken tokenOBJ) throws AuthenticationException {
//        System.out.println("执行了认证方法");
        UsernamePasswordToken userToken = null;
        userToken = (UsernamePasswordToken)tokenOBJ;
        User user = new User();
        user.setEmail(userToken.getUsername());
        User u = userService.getUsers(user);
        //用户名认证
        if(u==null){
            //返回null 就是异常：UnknownAccountException
            return null;
        }
        //密码认证（防止泄露，不需要我们做）
        return new SimpleAuthenticationInfo(u,u.getPassword(),"");
    }
}
