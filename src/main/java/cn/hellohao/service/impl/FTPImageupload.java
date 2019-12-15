package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.utils.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FTPImageupload {
    static FTPClient ftpClient1 ;
    static Keys key;

    public Map<ReturnImage, Integer> ImageuploadFTP(Map<String, MultipartFile> fileMap, String username,
                                                    Map<String, String> fileMap2,Integer setday) throws Exception {
        String[] host = key.getEndpoint().split("\\:");
        String h = host[0];
        Integer p = Integer.parseInt(host[1]);
        //创建FTP客户端，所有的操作都基于FTPClinet
        FTPUtils ftps = new FTPUtils(h, p, key.getAccessKey(), key.getAccessSecret());
        boolean flag = ftps.open();
        if(fileMap2==null){
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            ftps.mkDir(File.separator+username);
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                file = changeFile(entry.getValue());
                String userkey =username + File.separator+ uuid+times + "." + entry.getKey();
                if (flag) {
                    ftps.upload(file, File.separator+userkey, "");
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(key.getRequestAddress() + File.separator+ userkey);
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                    if(setday>0) {
                        String deleimg = DateUtils.plusDay(setday);
                        DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "7");
                    }
                    ftps.close();
                }

                Print.Normal("要上传的文件路径："+File.separator+userkey);
            }
            return ImgUrl;
        }else{
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String imgurl = entry.getValue();
                File file = new File(imgurl);
                String userkey =username + File.separator+ uuid+times + "." + entry.getKey();
                ftps.mkDir(File.separator+username);
                if (flag) {
                    ftps.upload(file, File.separator+userkey, "");
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgurl(key.getRequestAddress() + File.separator+ userkey);
                    ImgUrl.put(returnImage, ImgUrlUtil.getFileSize2(new File(imgurl)));
                    if(setday>0) {
                        String deleimg = DateUtils.plusDay(setday);
                        DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "7");
                    }
                    ftps.close();
                }
                boolean bb= new File(imgurl).getAbsoluteFile().delete();
                Print.Normal("删除情况"+bb);
            }
            return ImgUrl;
        }
    }

    // 转换文件方法
    private File changeFile(MultipartFile multipartFile) throws Exception {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // todo 修改临时文件文件名
        File file = File.createTempFile(fileName, prefix);
        multipartFile.transferTo(file);
        return file;
    }

    //初始化FTP对象存储
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null && k.getRequestAddress()!=null ) {
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("") && !k.getRequestAddress().equals("") ) {
                FTPClient ftp = new FTPClient();
                int flag = k.getEndpoint().indexOf(":");
                if(flag>0){
                    String[] host = k.getEndpoint().split("\\:");
                    String h = host[0];
                    Integer p = Integer.parseInt(host[1]);
                    try {
                        if(!ftp.isConnected()){
                            ftp.connect(h,p);
                        }
                        ftp.login(k.getAccessKey(), k.getAccessSecret());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //获取服务器返回的状态码
                    int reply = ftp.getReplyCode();
                    /*
                     * 判断是否连接成功
                     * 所有以2开头的代码是正完成响应。
                     * FTP服务器将在最终发送一个肯定的完成响应成功完成命令。
                     */
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        try {
                            ftp.disconnect();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        ret = -1;
                    }
                    ftpClient1 = ftp;
                    key = k;
                    ret = 1;
            }
            }
            }
        return ret;
    }
    /**
     * 客户端接口
     * */
    public Map<ReturnImage, Integer> clientuploadFTP(Map<String, MultipartFile> fileMap, String username, UploadConfig uploadConfig) throws Exception {
        String[] host = key.getEndpoint().split("\\:");
        String h = host[0];
        Integer p = Integer.parseInt(host[1]);
        //创建FTP客户端，所有的操作都基于FTPClinet
        FTPUtils ftps = new FTPUtils(h, p, key.getAccessKey(), key.getAccessSecret());
        boolean flag = ftps.open();
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            file = changeFile(entry.getValue());
            String userkey =username + File.separator+ uuid+times + "." + entry.getKey();
            ftps.mkDir(File.separator+username);
            if (flag) {
                ReturnImage returnImage = new ReturnImage();
                if(entry.getValue().getSize()/1024<=uploadConfig.getFilesizeuser()*1024){
                    ftps.upload(file, File.separator+userkey, "");

                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(key.getRequestAddress() + File.separator+ userkey);
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                    ftps.close();
                }else{
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl("文件超出系统设定大小，不得超过");
                    ImgUrl.put(returnImage, -1);
                    ftps.close();
                }
            }
        }
        return ImgUrl;
    }

    public static void fuzhi(String p1,String p2){
        String a=p1;//定义要进行复制的文件路径
        try{
            File afile=new File(a);
            File bfile=new File(p2);//定义一个复制后的文件路径
            bfile.createNewFile();//新建文件
            FileInputStream c=new FileInputStream(afile);//定义FileInputStream对象
            FileOutputStream d=new FileOutputStream(bfile);//定义FileOutputStream对象
            byte[] date=new byte[512];//定义一个byte数组
            int i=0;
            while((i=c.read(date))>0){//判断有没有读取到文件末尾
                d.write(date);//写数据
            }
            c.close();//关闭流
            d.close();//关闭流
            System.out.println("文件复制成功");}
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
