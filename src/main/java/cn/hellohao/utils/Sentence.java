package cn.hellohao.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Sentence {

    /**
     * 根据URL请求json数据
     *
     * @return //parm：请求的url链接  返回的是json字符串
     */

    public static String getURLContent() {
//        String str = "";
//        try {
//            URL url = new URL("https://v1.hitokoto.cn/");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                str += line;
//            }
//            reader.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return str;







            String result = "";
            String urlName = "https://v1.hitokoto.cn/";
            try {
                URL realURL = new URL(urlName);
                URLConnection conn = realURL.openConnection();
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                //conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
                conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
                conn.connect();
                Map<String, List<String>> map = conn.getHeaderFields();
                for (String s : map.keySet()) {
                    System.out.println(s + "-->" + map.get(s));
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result += "\n" + line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


}
