package cn.hellohao.utils;

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



    public static Map<String, Integer> ImageuploadLOC(Map<String, MultipartFile> fileMap, String username, Map<String, String> fileMap2) throws Exception {
        String filePath =File.separator + "HellohaoData" + File.separator;
        if(fileMap2==null){
            File file = null;
            Map<String, Integer> ImgUrl = new HashMap<>();
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
                    ImgUrl.put(username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("上传失败");
                }
            }
            return ImgUrl;
        }else{
            Map<String, Integer> ImgUrl = new HashMap<>();

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
               // boolean result = upyun.writeFile(username + "/" + uuid+times + "." + entry.getKey(), new File(imgurl), true);
                    ImgUrl.put( username + "/" + uuid+times + "." + entry.getKey(), ImgUrlUtil.getFileSize2(new File(newfilePath)));
                }
                //new File(imgurl).delete();
            return ImgUrl;
            }
        }

}
