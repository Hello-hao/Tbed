package cn.hellohao.utils;

import cn.hellohao.TbedApplication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/*
操作网络url图片工具类
* */
public class ImgUrlUtil {

    //获取文件类型
//    public static void main(String[] args) throws Exception {
//        FileInputStream is = new FileInputStream("D:\\22222\\");
//        byte[] b = new byte[3];
//        is.read(b, 0, b.length);
//        String xxx = bytesToHexString(b);
//        xxx = xxx.toUpperCase();
//        System.out.println("头文件是：" + xxx);
//        String ooo = TypeDict.checkType(xxx);
//        System.out.println("后缀名是：" + ooo);
//
//
//        //Long ll = FileType.getFileLength("http://img.ph.126.");
//        // Print.Normal("文件大小="+ll);
//    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 获取网络文件大小
     */
    public static long getFileLength(String downloadUrl) throws IOException {
        if(downloadUrl == null || "".equals(downloadUrl) ||downloadUrl.length()<=7){
            return 0L ;
        }
        URL url = new URL(downloadUrl);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows 7; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36 YNoteCef/5.8.0.1 (Windows)");
            return (long) conn.getContentLength();
        } catch (IOException e) {
            return 0L;
        } finally {
            conn.disconnect();
        }
    }


    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */

    /**
     * 调用方式
     *         try{
     *             downLoadFromUrl("https://hellohao.oss-cn-beijing.aliyuncs.com/Hellohao/8bb3a0627022829.png",
     *                     "22222",TbedApplication.class.getClassLoader().getResource("static/files").getPath());
     *         }catch (Exception e) {
     * //            // TODO: handle exception
     *         }
     * */

//    public static void main(String[] args) throws Exception {
////        ImgUrlUtil.downLoadFromUrl("http://pic13.nipic.com/20110409/7119492_114440620000_2.jpg",
////                "123321", "/temp/temporary");
//Print.Normal(ImgUrlUtil.getFileLength("http://pic41.nipic.com/20140508/18609517_112216473140_2.jpg"));
//        //File file=;  //  /Users是路径名
//        //new File(TbedApplication.class.getClassLoader().getResource("static/files").getPath()+"/d30d794d0de74a4f9fab3e4dbb8ea4cb").delete();
//    }

    public static boolean  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        //File saveDir = new File(savePath);
        //this.getServletContext().getRealPath("/WEB-INF/jrxml/hgc.png"
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }
        System.out.println("info:"+url+" download success");
        return true;
    }
    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**

     * 根据java.io.*的流获取文件大小

     * @param file

     */

    public static Integer getFileSize2(File file){
        Integer imgsize = 0;
        FileInputStream fis = null;

        try {

            if(file.exists() && file.isFile()){

                String fileName = file.getName();

                fis = new FileInputStream(file);
                imgsize=fis.available()/1024;
                System.out.println("文件"+fileName+"的大小是："+fis.available()/1024+"K\n");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }finally{

            if(null!=fis){

                try {

                    fis.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }
return imgsize;
    }


}
