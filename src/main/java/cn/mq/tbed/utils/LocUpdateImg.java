package cn.mq.tbed.utils;

import cn.mq.tbed.pojo.ReturnImage;
import cn.mq.tbed.pojo.UploadConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocUpdateImg {
    public static void deleteLOCImg(String imagename){
        String filePath =File.separator + Resources.basePath + File.separator+imagename;
        File file = new File(filePath);
        file.delete();
    }

    public static Map<ReturnImage, Integer> ImageuploadLOC(Map<String, MultipartFile> fileMap, String username,
                                                           Map<String, String> fileMap2,Integer setday) throws Exception {
        String filePath =File.separator + Resources.basePath + File.separator;
        if(fileMap2==null){
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = "TOIMG"+UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                // 上传文件流。
                System.out.println("待上传的图片："+username + File.separator + uuid+times + "N." + entry.getKey());
                File dest = new File(filePath + username + File.separator+ uuid+times + "N." + entry.getKey());
                File temppath = new File(filePath + username+ File.separator );
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    MultipartFile multipartFile = entry.getValue();
                    //FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
                    InputStream fileInputStream = (InputStream)multipartFile.getInputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
                    byte[] bs = new byte[1024];
                    int len;
                    while ((len = fileInputStream.read(bs)) != -1) {
                        bos.write(bs, 0, len);
                    }
                    bos.flush();
                    bos.close();
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgname(username + "/" +uuid+times + "N." + entry.getKey());//entry.getValue().getOriginalFilename()
                    returnImage.setImgurl(username + "/" + uuid+times + "N." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                    if(setday>0) {
                        String deleimg = DateUtils.plusDay(setday);
                        DeleImg.charu(username + "/" + uuid + times + "N." + entry.getKey() + "|" + deleimg + "|" + "5");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("上传失败");
                }
            }
            return ImgUrl;
        }else{
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = "TOIMG"+UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String oldfilePath = entry.getValue();
                String newfilePath =  File.separator + Resources.basePath + File.separator + username + File.separator+ uuid+times + "N." + entry.getKey();
                String tempfilePath =  File.separator + Resources.basePath + File.separator + username+ File.separator ;
                File file = new File(oldfilePath);
                File targetFile =new File(tempfilePath);
                if(!targetFile.getParentFile().exists()) {
                    targetFile.mkdirs();
                }
                file.renameTo(new File(newfilePath));
                //采用数据流模式上传文件（节省内存）,自动创建父级目录
                ReturnImage returnImage = new ReturnImage();
                //username + "/" + uuid+times + "." + entry.getKey()
                returnImage.setImgurl(username + "/" + uuid+times + "N." + entry.getKey());
                    ImgUrl.put( returnImage, ImgUrlUtil.getFileSize2(new File(newfilePath)));
                if(setday>0) {
                    String deleimg = DateUtils.plusDay(setday);
                    DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "5");
                }
                }
            return ImgUrl;
            }
        }

    /**
     * 客户端接口
     * */
    public static Map<ReturnImage, Integer> clientLocUpdateImg(Map<String, MultipartFile> fileMap, String username, UploadConfig uploadConfig) {
        String filePath =File.separator + Resources.basePath + File.separator;
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String uuid = "TOIMG"+UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            System.out.println("待上传的图片："+username + "/" + uuid+times + "N." + entry.getKey());
           ReturnImage returnImage = new ReturnImage();
            if(entry.getValue().getSize()/1024<=uploadConfig.getFilesizeuser()*1024){
                File dest = new File(filePath + username + File.separator+ uuid+times + "N." + entry.getKey());
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    MultipartFile multipartFile = entry.getValue();
                    FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
                    byte[] bs = new byte[1024];
                    int len;
                    while ((len = fileInputStream.read(bs)) != -1) {
                        bos.write(bs, 0, len);
                    }
                    bos.flush();
                    bos.close();
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(username + "/" + uuid+times + "N." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize())/1024);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("上传失败");
                }
            }else{
                returnImage.setImgname(entry.getValue().getOriginalFilename());
                returnImage.setImgurl("文件超出系统设定大小，不得超过");
                ImgUrl.put(returnImage,-1);
            }
        }
        return ImgUrl;
    }

}
