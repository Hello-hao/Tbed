package cn.hellohao.utils;

import org.apache.commons.lang3.StringUtils;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Set;

public class NetWorkTools {

    /**
     * @return
     * @throws MalformedObjectNameException
     * 获取当前机器的端口号
     */
    public static String getLocalPort()  {//throws MalformedObjectNameException
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = null;
        try {
            objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                    Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        String port = objectNames.iterator().next().getKeyProperty("port");
        return port;
    }

    /**
     * @return
     * 获取当前机器的IP
     */
    public static String getLocalIP() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }


    /*
    * 获取协议头
    * */
    private final  static String HTTP= "http://";
    private final  static  String HTTPS =  "https://";
    private static String newurl;

    //判断协议 能连接上则协议正确
    public static String getProtocol(String url,String referer) {
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
    private static String clearUrl(String url) {
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
    private static boolean exists(String url, String referer) {
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


}
