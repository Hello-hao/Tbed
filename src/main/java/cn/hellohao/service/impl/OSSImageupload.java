package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.utils.*;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OSSImageupload {
    static String BarrelName;
    static OSSClient ossClient;
    static Keys key;

    public Map<ReturnImage, Integer> ImageuploadOSS(Map<String, MultipartFile> fileMap, String username,
                                                    Map<String, String> fileMap2,Integer setday) throws Exception {
        if(fileMap2==null){
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                file = SetFiles.changeFile(entry.getValue());
                String head = "";
                if(entry.getKey().equals("jpg")||entry.getKey().equals("jpeg")){
                    head = "image/jpeg";
                }else if(entry.getKey().equals("png")){
                    head = "image/png";
                }else if(entry.getKey().equals("bmp")){
                    head = "image/bmp";
                }else if(entry.getKey().equals("gif")){
                    head = "image/gif";
                }else{
                    System.err.println("位置格式文件，无法定义header头。");
                }
                meta.setHeader("Content-Type", head);//image/jpeg
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
                ossClient.putObject(key.getBucketname(), username + "/" + uuid+times + "." + entry.getKey(),file,meta);
                ReturnImage returnImage = new ReturnImage();
                returnImage.setImgname(entry.getValue().getOriginalFilename());
                returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                if(setday>0) {
                    String deleimg = DateUtils.plusDay(setday);
                    DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "2");
                }
            }
            return ImgUrl;
        }else{
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");
            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String imgurl = entry.getValue();
                String head = "";
                if(entry.getKey().equals("jpg")||entry.getKey().equals("jpeg")){
                    head = "image/jpeg";
                }else if(entry.getKey().equals("png")){
                    head = "image/png";
                }else if(entry.getKey().equals("bmp")){
                    head = "image/bmp";
                }else if(entry.getKey().equals("gif")){
                    head = "image/gif";
                }else{
                    System.err.println("位置格式文件，无法定义header头。");
                }
                meta.setHeader("Content-Type", head);//image/jpeg
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
                ossClient.putObject(key.getBucketname(), username + "/" + uuid+times + "." + entry.getKey(),new File(imgurl),meta);
                ReturnImage returnImage = new ReturnImage();
                returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                ImgUrl.put(returnImage, ImgUrlUtil.getFileSize2(new File(imgurl)));
                new File(imgurl).delete();
                if(setday>0) {
                    String deleimg = DateUtils.plusDay(setday);
                    DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "2");
                }
            }
            return ImgUrl;
        }
    }

    //初始化
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ) {
                ossClient = new OSSClient(k.getEndpoint(), k.getAccessKey(), k.getAccessSecret());
                key = k;
                ret=1;
            }
        }
        return ret;
    }


    /**
     * 客户端接口
     * */
    public Map<ReturnImage, Integer> clientuploadOSS(Map<String, MultipartFile> fileMap, String username, UploadConfig uploadConfig) throws Exception {
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                file = SetFiles.changeFile(entry.getValue());
                String head = "";
                if(entry.getKey().equals("jpg")||entry.getKey().equals("jpeg")){
                    head = "image/jpeg";
                }else if(entry.getKey().equals("png")){
                    head = "image/png";
                }else if(entry.getKey().equals("bmp")){
                    head = "image/bmp";
                }else if(entry.getKey().equals("gif")){
                    head = "image/gif";
                }else{
                    System.err.println("位置格式文件，无法定义header头。");
                }
                meta.setHeader("Content-Type", head);//image/jpeg
                ReturnImage returnImage = new ReturnImage();
                if(entry.getValue().getSize()/1024<=uploadConfig.getFilesizeuser()*1024){
                    ossClient.putObject(key.getBucketname(), username + "/" + uuid+times + "." + entry.getKey(),file,meta);
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                }else{
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl("文件超出系统设定大小，不得超过");
                    ImgUrl.put(returnImage, -1);
                }
            }
            return ImgUrl;

    }

}
