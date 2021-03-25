package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.utils.*;
import com.UpYun;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class UFileImageupload {
    static UpYun uFile;
    static Keys key;

    public Map<ReturnImage, Integer> ImageuploadUSS(Map<String, MultipartFile> fileMap, String username,
                                                    Map<String, String> fileMap2,Integer setday) {
        if(fileMap2==null){
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");

            try {
                for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                    String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                    java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                    String times = format1.format(new Date());
                    file = SetFiles.changeFile(entry.getValue());
                    uFile.setContentMD5(UpYun.md5(file));
                    boolean result = uFile.writeFile(username + "/" + uuid+times + "." + entry.getKey(), file, true);
                    if(result){
                        ReturnImage returnImage = new ReturnImage();
                        returnImage.setImgname(username + "/" + uuid+times + "." + entry.getKey());//entry.getValue().getOriginalFilename()
                        returnImage.setImgurl(key.getRequestAddress() + "/" +username + "/" + uuid+times + "." + entry.getKey());//key.getRequestAddress() + "/" +
                        ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                        if(setday>0) {
                            String deleimg = DateUtils.plusDay(setday);
                            DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "3");
                        }
                    }else{
                        System.err.println("上传失败");
                    }
                }
            }catch(Exception e){
                ImgUrl.put(null, 500);
            }
            return ImgUrl;
        }else{
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");

           try {
               for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                   String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                   java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                   String times = format1.format(new Date());
                   String imgurl = entry.getValue();
                   uFile.setContentMD5(UpYun.md5(new File(imgurl)));
                   boolean result = uFile.writeFile(username + "/" + uuid+times + "." + entry.getKey(), new File(imgurl), true);
                   if(result){
                       ReturnImage returnImage = new ReturnImage();
                       returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                       ImgUrl.put(returnImage, ImgUrlUtil.getFileSize2(new File(imgurl)));
                       if(setday>0) {
                           String deleimg = DateUtils.plusDay(setday);
                           DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "3");
                       }
                   }else{
                       Print.warning("上传失败");
                   }
                   new File(imgurl).delete();
               }
           }catch(Exception e){
               ImgUrl.put(null, 500);
           }
            return ImgUrl;
        }

    }

//    // 转换文件方法
//    private File changeFile(MultipartFile multipartFile) throws Exception {
//        // 获取文件名
//        String fileName = multipartFile.getName();//getOriginalFilename
//        // 获取文件后缀
//        String prefix = fileName.substring(fileName.lastIndexOf("."));
//        // todo 修改临时文件文件名
//        File file = File.createTempFile(fileName, prefix);
//        // MultipartFile to File
//        multipartFile.transferTo(file);
//        return file;
//    }

    //ufile初始化
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ) {
                // 初始化
                // 创建UpYun实例。
                uFile = new UpYun(k.getBucketname(), k.getAccessKey(), k.getAccessSecret());
                List<UpYun.FolderItem> items = null;
                try {
                   items = uFile.readDir("/",null);
                    key = k;
                    ret = 1;
                } catch (Exception e) {
                    System.out.println("UFile - Waiting for configuration");
                    ret = -1;
                }
            }
        }
        return ret;
    }


    /**
     * 客户端接口
     * */
    public Map<ReturnImage, Integer> clientuploadUSS(Map<String, MultipartFile> fileMap, String username, UploadConfig uploadConfig) throws Exception {
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        //设置Header
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            file = SetFiles.changeFile_c(entry.getValue());
            // 上传文件流。
            System.out.println("客户端：待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
            ReturnImage returnImage = new ReturnImage();
            if(entry.getValue().getSize()/1024<=uploadConfig.getFilesizeuser()*1024){
                // 例2：采用数据流模式上传文件（节省内存）,自动创建父级目录
                uFile.setContentMD5(UpYun.md5(file));
                boolean result = uFile.writeFile(username + "/" + uuid+times + "." + entry.getKey(), file, true);
                if(result){
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));

                    //ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));
                }else{
                    System.err.println("上传失败");
                }
            }else{
                returnImage.setImgname(entry.getValue().getOriginalFilename());
                returnImage.setImgurl("文件超出系统设定大小，不得超过");
                ImgUrl.put(returnImage, -1);
            }
        }
        return ImgUrl;
    }


}
