package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
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

    public Map<String, Integer> ImageuploadOSS(Map<String, MultipartFile> fileMap, String username) throws Exception {
        // 要上传文件的路径
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
                // 上传文件流。
                ossClient.putObject(key.getBucketname(), username + "/" + uuid+times + "." + entry.getKey(),file,meta);
                // 关闭OSSClient。
                //ossClient.shutdown();
                ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));

        }
        return ImgUrl;
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

    //初始化网易NOS对象存储
    public static void Initialize(Keys k) {
        // 初始化
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI0Yd6Li0VxrUF";
        String accessKeySecret = "F2ZuDMq0IYefvVDKyfJLpRNzXldci9";
        String bucketName = "hellohao";
        // 创建OSSClient实例。
        ossClient = new OSSClient(k.getEndpoint(), k.getAccessKey(), k.getAccessSecret());
        key = k;
    }


}
