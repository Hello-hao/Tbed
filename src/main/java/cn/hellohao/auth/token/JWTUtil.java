package cn.hellohao.auth.token;

import cn.hellohao.pojo.User;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/11 18:50
 */
public class JWTUtil {

    private static String EXPIRE_TIME = "";
    private static String SECRET = "www.hellohao.cn";

    //生成Token
    public static String createToken(User user){
//        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,604800 );//单位秒，604800 为7天
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String token = JWT.create()
                .withClaim("email", user.getEmail())
                .withClaim("username", user.getUsername())
                .withClaim("uid", user.getUid())
                .withClaim("password", user.getPassword())
                //到期时间
                .withExpiresAt(calendar.getTime())
                //创建一个新的JWT，并使用给定的算法进行标记
                .sign(algorithm);
        return token;
    }

    //验证Token
    public static JSONObject checkToken(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();//验证对象
        JSONObject jsonObject = new JSONObject();
        if(null==token){
            jsonObject.put("check",false);
            return jsonObject;
        }
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            Date expiresAt = verify.getExpiresAt();
            jsonObject.put("check",true);
            jsonObject.put("email",verify.getClaim("email").asString());
            jsonObject.put("password",verify.getClaim("password").asString());
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


    public static void main(String[] args) {
        User user = new User();
        user.setEmail("Hellohao@qq.com");
        user.setUsername("Hellohao");
        String token = createToken(user);
        System.out.println(token);
        checkToken(token);
//        checkToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjM3NDYwOTUsImVtYWlsIjoiSGVsbG9oYW9AcXEuY29tIiwidXNlcm5hbWUiOiJIZWxsb2hhbyJ9.sFnAq1VU0wuiowaTtgJNNkMzRHPmRg4leNguhh0OMCA");
    }
}
