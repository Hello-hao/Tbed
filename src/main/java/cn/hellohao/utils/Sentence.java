package cn.hellohao.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Sentence {

    /**
     * 根据URL请求json数据
     *
     * @return //parm：请求的url链接  返回的是json字符串
     */
    public static String getURLContent() {
        String str = "";
        try {
            URL url = new URL("https://v1.hitokoto.cn/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            //BufferedWriter writer=new BufferedWriter(new FileWriter("index.html"));
            String line;
            while ((line = reader.readLine()) != null) {
                str += line;
                //System.out.println(str);
            }
            reader.close();

            //writer.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
