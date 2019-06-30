package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.utils.ImgUrlUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class KODOImageupload {
    static String upToken;
    static UploadManager uploadManager;
    static Keys key;

    public Map<String, Integer> ImageuploadKODO(Map<String, MultipartFile> fileMap, String username,Map<String, String> fileMap2) throws Exception {
        // 要上传文件的路径
        if(fileMap2==null){
            File file = null;
            Map<String, Integer> ImgUrl = new HashMap<>();

            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                file = changeFile(entry.getValue());
                // 上传文件流。
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
                //ossClient.putObject(key.getBucketname(), username + "/" + uuid+times + "." + entry.getKey(),file,meta);
                try {
                    Response response = uploadManager.put(file,username + "/" + uuid+times + "." + entry.getKey(),upToken);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));
                    //System.out.println(putRet.hash);
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                }
            }
            return ImgUrl;
        }else{
            Map<String, Integer> ImgUrl = new HashMap<>();

            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String imgurl = entry.getValue();
                // 上传文件流。
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
                try {
                    Response response = uploadManager.put(new File(imgurl),username + "/" + uuid+times + "." + entry.getKey(),upToken);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), ImgUrlUtil.getFileSize2(new File(imgurl)));
                    //System.out.println(putRet.hash);
                    new File(imgurl).delete();
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                }
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
        // 创建KODOClient实例。
        Configuration cfg;
        //构造一个带指定Zone对象的配置类
        if(k.getEndpoint().equals("1")){cfg = new Configuration(Zone.zone0());}
        else if(k.getEndpoint().equals("2")){cfg = new Configuration(Zone.zone1());}
        else if(k.getEndpoint().equals("3")){cfg = new Configuration(Zone.zone2());}
        else if(k.getEndpoint().equals("4")){cfg = new Configuration(Zone.zoneNa0());}
        else{cfg = new Configuration(Zone.zoneAs0());}
        uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(k.getAccessKey(), k.getAccessSecret());
        upToken = auth.uploadToken(k.getBucketname());
        key = k;
    }


}
