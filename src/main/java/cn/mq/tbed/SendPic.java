package cn.mq.tbed;


import java.io.*;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;


/**
 * @author mq
 * @description: TODO
 * @title: SendPic
 * @projectName Tbed
 * @date 2021/1/1416:13
 */
public class SendPic {
    public static void main(String[] args) throws IOException {
        String url = "http://127.0.0.1:8081/upimgss";
        File file = new File("1.jpg");
        String fileload = SendPic.fileload(url, file);
        System.out.println(fileload);
//        String body = HttpRequest.get(url).timeout(20000).form("file", baos.toByteArray(), "aaa.png").execute().body();
//        System.out.println(body);

    }

    public static String fileload(String url, File file) {
        String body = "{}";

        if (url == null || url.equals("")) {
            return "illegal";
        }
        if (!file.exists()) {
            return "file not exist";
        }

        PostMethod postMethod = new PostMethod(url);
        BufferedReader br = null;
        try {
            // FilePart：用来上传文件的类,file即要上传的文件
            FilePart fp = new FilePart("file", file);
            Part[] parts = new Part[]{fp};

            // 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
            MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
            postMethod.setRequestEntity(mre);

            HttpClient client = new HttpClient();
            // 由于要上传的文件可能比较大 , 因此在此设置最大的连接超时时间
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                body = stringBuffer.toString();
            } else {
                body = "fail";
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            // 释放连接
            postMethod.releaseConnection();
        }
        return body;
    }
}

