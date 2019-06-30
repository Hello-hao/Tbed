package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.utils.ImgUrlUtil;
import com.UpYun;
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
public class USSImageupload {
    static UpYun upyun;
    static Keys key;

    public Map<String, Integer> ImageuploadUSS(Map<String, MultipartFile> fileMap, String username,Map<String, String> fileMap2) throws Exception {
        if(fileMap2==null){
            File file = null;
            Map<String, Integer> ImgUrl = new HashMap<>();
            //设置Header
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                file = changeFile(entry.getValue());


                // 上传文件流。
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());

                // 例2：采用数据流模式上传文件（节省内存）,自动创建父级目录
                upyun.setContentMD5(UpYun.md5(file));
                boolean result = upyun.writeFile(username + "/" + uuid+times + "." + entry.getKey(), file, true);
                if(result){
                    ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));
                }else{
                    System.err.println("上传失败");
                }

            }
            return ImgUrl;
        }else{
            Map<String, Integer> ImgUrl = new HashMap<>();
            //设置Header
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");
            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String imgurl = entry.getValue();


                // 上传文件流。
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());

                // 例2：采用数据流模式上传文件（节省内存）,自动创建父级目录
                upyun.setContentMD5(UpYun.md5(new File(imgurl)));
                boolean result = upyun.writeFile(username + "/" + uuid+times + "." + entry.getKey(), new File(imgurl), true);
                if(result){
                    ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), ImgUrlUtil.getFileSize2(new File(imgurl)));
                }else{
                    System.err.println("上传失败");
                }
                new File(imgurl).delete();
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
        // MultipartFile to File
        multipartFile.transferTo(file);
        return file;
    }

    //初始化
    public static void Initialize(Keys k) {
        // 初始化
        // 创建UpYun实例。
        upyun = new UpYun(k.getBucketname(), k.getAccessKey(), k.getAccessSecret());
        key = k;
    }


}
