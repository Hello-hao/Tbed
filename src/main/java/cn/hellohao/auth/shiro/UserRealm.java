package cn.hellohao.auth.shiro;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.auth.token.JwtAuthenticationToken;
import cn.hellohao.pojo.User;
import cn.hellohao.service.UserService;
import cn.hellohao.utils.SetText;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtAuthenticationToken || super.supports(token);
    }

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
        if (tokenOBJ instanceof JwtAuthenticationToken) {
            DecodedJWT jwt;
            try {
                jwt = JWTUtil.verifyToken((String) tokenOBJ.getCredentials());
            } catch (JWTVerificationException e) {
                throw new IncorrectCredentialsException("Invalid or expired JWT", e);
            }
            String email = jwt.getClaim("email").asString();
            String uid = jwt.getClaim("uid").asString();
            if (!StringUtils.hasText(email) || !StringUtils.hasText(uid)) {
                throw new IncorrectCredentialsException("JWT identity is missing");
            }
            User lookup = new User();
            lookup.setEmail(email);
            lookup.setUid(uid);
            User user = userService.getUsers(lookup);
            if (user == null) {
                throw new UnknownAccountException();
            }
            if (user.getIsok() == null || user.getIsok() < 1) {
                throw new DisabledAccountException();
            }
            if (!JWTUtil.matchesCredentials(jwt, user)) {
                throw new IncorrectCredentialsException("JWT credentials are no longer current");
            }
            // JWT signature verification authenticates this branch, not a password claim.
            return new SimpleAuthenticationInfo(user, tokenOBJ.getCredentials(), getName());
        }
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
        //密码认证
        return new SimpleAuthenticationInfo(u,u.getPassword(),"");
    }
}
