package cn.hellohao.utils;

import cn.hutool.http.HttpUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/21 10:31
 */
public class VerificationDomain {
   // public static  ;
    public static  String domain2;

    public static boolean verification(){
        String domain=null;
        HttpServletRequest re = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 网络协议
        String networkProtocol = re.getScheme();
        // 网络ip
        String host = re.getServerName();
        boolean b =false;
            if(domain2==null){
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("domain",host);
                domain= HttpUtil.post("http://tc.hellohao.cn/getdomain", paramMap);
            }else{
                return domain2.equals(host);
            }
        b = Integer.parseInt(domain)>0;
        if(b==true){
            domain2=host;
        }
        return b;
    }
}
