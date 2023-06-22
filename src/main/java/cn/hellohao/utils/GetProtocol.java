package cn.hellohao.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;

public class  GetProtocol {
    private final  static String HTTP= "http://";
    private final  static  String HTTPS =  "https://";
    private String newurl;

    //判断协议 能连接上则协议正确
    public  String getProtocol(String url,String referer) {
        newurl = clearUrl(url);
        url = HTTP + newurl;
        if (exists(url,referer)) {
            return url;
        } else {
            url = HTTPS + newurl;
            if (exists(url,referer)) {
                return url;
            } else {
                return null;
            }
        }
 
    }
    //清除URL里多余的符号
    private  String clearUrl(String url) {
        if (url.contains(HTTP)) {
            url = url.substring(url.lastIndexOf(HTTP) + HTTP.length());
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/' || url.charAt(i) == '.'
                        || url.charAt(i) == '\\') {
                } else {
                    url = url.substring(i);
                    return url;
                }
            }
        } else if (url.contains(HTTPS)) {
            url = url.substring(url.lastIndexOf(HTTPS) + HTTPS.length());
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/' || url.charAt(i) == '.'
                        || url.charAt(i) == '\\') {
                } else {
                    url = url.substring(i);
                    return url;
                }
            }
        }else{
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/' || url.charAt(i) == '.'
                        || url.charAt(i) == '\\') {
                } else {
                    url = url.substring(i);
                    return url;
                }
            }
        }
        return url;
    }
    //是否能连接上
   private boolean exists(String url,String referer) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setConnectTimeout(3000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
            con.setRequestProperty("referer", StringUtils.isBlank(referer)?"no-referrer":referer);
            System.out.println(con.getResponseCode() );
            return (con.getResponseCode() == 200);
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args){

        try {
            String url = "https://img-blog.csdnimg.cn/cbf3201a3cfe4fc38a7cef006487158c.png";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String referer = con.getRequestProperty("Referer");
            System.out.println("Referer: " + referer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}