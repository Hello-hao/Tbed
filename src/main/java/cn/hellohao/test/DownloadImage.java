package cn.hellohao.test;

import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;  
  
  
public class DownloadImage {  
  
    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
        // TODO Auto-generated method stub  
         download("http://ui.51bi.com/opt/siteimg/images/fanbei0923/Mid_07.jpg", "51bi.gif","c:\\image\\");  
    }  
      
    public static void download(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);
        System.out.println("==="+sf.length());
       if(!sf.exists()){  
           sf.mkdirs();  
       }

        DownloadImage d = new DownloadImage();

        System.out.println(d.isImage(sf));
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        is.close();  
    }
        private MimetypesFileTypeMap mtftp;

        public DownloadImage(){
            mtftp = new MimetypesFileTypeMap();
            /* 不添加下面的类型会造成误判 详见：http://stackoverflow.com/questions/4855627/java-mimetypesfiletypemap-always-returning-application-octet-stream-on-android-e*/
            mtftp.addMimeTypes("image png tif jpg jpeg bmp");
        }
        public  boolean isImage(File file){
            String mimetype= mtftp.getContentType(file);
            String type = mimetype.split("/")[0];
            return type.equals("image");
        }



  
}  