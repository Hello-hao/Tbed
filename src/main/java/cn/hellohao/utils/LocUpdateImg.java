package cn.hellohao.utils;

import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.pojo.UploadConfig;
import com.google.gson.Gson;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocUpdateImg {
    public static void deleteLOCImg(String imagename){
        String filePath =File.separator + "HellohaoData" + File.separator+imagename;
        File file = new File(filePath);
        file.delete();
    }



    public static Map<ReturnImage, Integer> ImageuploadLOC(Map<String, MultipartFile> fileMap, String username, Map<String, String> fileMap2) throws Exception {
        String filePath =File.separator + "HellohaoData" + File.separator;
        if(fileMap2==null){
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                //file = SetFiles.changeFile(entry.getValue());
                // 上传文件流。
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
                File dest = new File(filePath + username + File.separator+ uuid+times + "." + entry.getKey());
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    entry.getValue().transferTo(dest);
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("上传失败");
                }
            }
            return ImgUrl;
        }else{
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();

            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String oldfilePath = entry.getValue();


                String newfilePath =  File.separator + "HellohaoData" + File.separator + username + File.separator+ uuid+times + "." + entry.getKey();//
                File file = new File(oldfilePath);
                File targetFile =new File(newfilePath);
                if(!targetFile.getParentFile().exists()) {
                    targetFile.mkdirs();
                }
                file.renameTo(new File(newfilePath));//只移动，源目录不存在文件
                // 例2：采用数据流模式上传文件（节省内存）,自动创建父级目录
                //upyun.setContentMD5(UpYun.md5(new File(imgurl)));
                ReturnImage returnImage = new ReturnImage();
                returnImage.setImgurl(username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put( returnImage, ImgUrlUtil.getFileSize2(new File(newfilePath)));
                }
                //new File(imgurl).delete();
            return ImgUrl;
            }
        }

    /**
     * 客户端接口
     * */
    public static Map<ReturnImage, Integer> clientuploadFTP(Map<String, MultipartFile> fileMap, String username, UploadConfig uploadConfig) {
        String filePath =File.separator + "HellohaoData" + File.separator;
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            //file = SetFiles.changeFile(entry.getValue());
            // 上传文件流。
            System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
            ReturnImage returnImage = new ReturnImage();
            if(entry.getValue().getSize()/1024<=uploadConfig.getFilesizeuser()*1024){
                File dest = new File(filePath + username + File.separator+ uuid+times + "." + entry.getKey());
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    entry.getValue().transferTo(dest);
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                    //ImgUrl.put(username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("上传失败");
                }
            }else{
                //ImgUrl.put("文件超出系统设定大小，不得超过"+uploadConfig.getFilesizeuser()+"M", -1);
                returnImage.setImgname(entry.getValue().getOriginalFilename());
                returnImage.setImgurl("文件超出系统设定大小，不得超过");
                ImgUrl.put(returnImage,-1);
            }
        }
        return ImgUrl;
    }

}
