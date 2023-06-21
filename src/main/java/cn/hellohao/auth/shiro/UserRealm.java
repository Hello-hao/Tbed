package cn.hellohao.auth.shiro;

import cn.hellohao.pojo.User;
import cn.hellohao.service.UserService;
import cn.hellohao.utils.SetText;
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

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        ArrayList<String> roleList = new ArrayList();
        if(user.getLevel()==2){
            roleList.add("admin");
            roleList.add("user");
        }else{
            roleList.add("user");
        }
        info.addRoles(roleList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken tokenOBJ) throws AuthenticationException {
        UsernamePasswordToken userToken = null;
        userToken = (UsernamePasswordToken)tokenOBJ;
        User user = new User();
        if(SetText.checkEmail(userToken.getUsername())){
            user.setEmail(userToken.getUsername());
        }else{
            user.setUsername(userToken.getUsername());
        }
        User u = userService.getUsers(user);
        if(u==null){
            return null;
        }
        //密码认证（防止泄露，不需要我们做）
        return new SimpleAuthenticationInfo(u,u.getPassword(),"");
    }
}
