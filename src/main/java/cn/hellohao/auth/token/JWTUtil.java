package cn.hellohao.auth.token;

import cn.hellohao.pojo.User;
import cn.hellohao.utils.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Calendar;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/11 18:50
 */
@Component
public class JWTUtil {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JWTUtil(@Value("${jwt.secret:}") String secret) {
        if (!StringUtils.hasText(secret) || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("Configure HELLOHAO_JWT_SECRET with a unique random secret of at least 32 bytes");
        }
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public static String createToken(User user){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,2592000 );//单位秒
        Algorithm algorithm = SpringContextHolder.getBean(JWTUtil.class).algorithm;
        String token = JWT.create()
                .withClaim("email", user.getEmail())
                .withClaim("username", user.getUsername())
                .withClaim("uid", user.getUid())
                .withClaim("credentialsVersion", credentialsVersion(user))
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
        return token;
    }

    private static String credentialsVersion(User user) {
        byte[] material = ("tbed-credentials:" + user.getUid() + ":" + user.getPassword()).getBytes(StandardCharsets.UTF_8);
        byte[] signature = SpringContextHolder.getBean(JWTUtil.class).algorithm.sign(
                "tbed-credentials".getBytes(StandardCharsets.UTF_8), material);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
    }

    public static boolean matchesCredentials(DecodedJWT jwt, User user) {
        String version = jwt.getClaim("credentialsVersion").asString();
        return version != null && MessageDigest.isEqual(version.getBytes(StandardCharsets.UTF_8),
                credentialsVersion(user).getBytes(StandardCharsets.UTF_8));
    }

    public static DecodedJWT verifyToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new JWTVerificationException("Token is missing");
        }
        DecodedJWT verified = SpringContextHolder.getBean(JWTUtil.class).verifier.verify(token);
        if (verified.getExpiresAt() == null) {
            throw new JWTVerificationException("Token expiration is required");
        }
        return verified;
    }

    public static JSONObject checkToken(String token){
        JSONObject jsonObject = new JSONObject();
        if(null==token){
            jsonObject.put("check",false);
            return jsonObject;
        }
        try {
            DecodedJWT verify = verifyToken(token);
            jsonObject.put("check",true);
            jsonObject.put("email",verify.getClaim("email").asString());
            jsonObject.put("uid",verify.getClaim("uid").asString());
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            System.out.println("token认证已过期，请重新登录获取");
            jsonObject.put("check",false);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("token无效");
            jsonObject.put("check",false);
        }
        return jsonObject;
    }



}
