package cn.hellohao.utils;

import jdk.nashorn.internal.objects.annotations.Where;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/10/11 17:26
 */
public class demo {
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param string
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String string,Integer i) {
        String intFlag = String.valueOf((int)(Math.random() * 1000000));
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(string);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        System.out.println("已成功执行"+i+"次");
        return result;
    }


    //文件上传
    public static String uploadFileToOSS(MultipartFile multfile,String isurl) {
        CloseableHttpClient client = null;
        String result = null;
        try {
            client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(isurl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //addTextBody(String,String)该方法可以传入参数，例如请求的地址需要接受一个id
            //builder.addTextBody("id", "1");
           // builder.addTextBody("arg", "value");
            //addBinaryBody()该方法传入二进制内容，可以传入InputStream，File, 参数三是传入的类型，参数四是文件名称
            builder.addBinaryBody("file", multfile.getInputStream(), ContentType.MULTIPART_FORM_DATA, "1.png");
            httpPost.setEntity(builder.build());
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 取回服务器端的响应结果
                 result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args)throws Exception {
        File file = new File("D:/22.png");
//        for(int i=1;i<99999999;i++){
//            String intFlag = String.valueOf((int)(Math.random() * 1000000));
//            //sendPost("http://shenhhh.net/index.php?m=User&a=signup","phone=13232"+intFlag+"&password=asd"+intFlag+"",i);
//            //String ret = sendPost("https://test.demo-1s.com/register","name=jbb"+intFlag+"&email=jbb"+intFlag+"@jbb.com&password=jbb"+intFlag,i);
//
//            String ret = sendPost("https://www.png8.com/upload/localhost","file="+file,i);
//            System.out.println("账号：goto"+intFlag+"-- 密码：baa"+intFlag+"-- 邮箱：jbb"+intFlag+"@jbb.com  -- 返回值："+ret);
//        }



        //文件上传
        //while (true){
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

            String a =uploadFileToOSS(multipartFile ,"https://tu.sunpma.com/upload/localhost");
            System.out.println("");
            Print.Normal("【"+a+"】");
            System.out.println("");

        }

   // }

}
