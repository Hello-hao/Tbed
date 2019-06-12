package cn.hellohao.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.Bucket;
import com.netease.cloud.services.nos.model.GeneratePresignedUrlRequest;
import com.netease.cloud.services.nos.transfer.TransferManager;

import cn.hellohao.pojo.Keys;

@Service
public class NOSImageupload {
    //直接读取properties文件的值
//	@Value("${AccessKey}")
//	private String AccessKey;
//	@Value("${AccessSecret}")
//	private String AccessSecret;
//	@Value("${Endpoint}")
//	private String Endpoint;
//	@Value("${Bucketname}")
//	private String Bucketname;
//	@Value("${RequestAddress}")
//	private String RequestAddress;
    static String BarrelName;
    static NosClient nosClient;
    static Keys key;

    public Map<String, Integer> Imageupload(Map<String, MultipartFile> fileMap, String username) throws Exception {
//		String Catalog = "";//默认游客目录
//		if(username.equals("tourist")){
//			Catalog = "tourist";
//		}else{
//			Catalog = username;
//		}
        // 要上传文件的路径
        File file = null;
        Map<String, Integer> ImgUrl = new HashMap<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            file = changeFile(entry.getValue());
            try {
                nosClient.putObject(BarrelName, username + "/" + uuid+times + "." + entry.getKey(), file);
                ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));
            } catch (Exception e) {
                System.out.println("上传报错:" + e.getMessage());
            }
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
        Credentials credentials = new BasicCredentials(k.getAccessKey(), k.getAccessSecret());
        nosClient = new NosClient(credentials);
        nosClient.setEndpoint(k.getEndpoint());
        // 初始化TransferManager
        TransferManager transferManager = new TransferManager(nosClient);
        // 列举桶
        ArrayList bucketList = new ArrayList();
        for (Bucket bucket : nosClient.listBuckets()) {
            bucketList.add(bucket.getName());
        }
        for (Object object : bucketList) {
            if (object.toString().equals(k.getBucketname())) {
                BarrelName = object.toString();
            }
        }
        key = k;
    }


}
