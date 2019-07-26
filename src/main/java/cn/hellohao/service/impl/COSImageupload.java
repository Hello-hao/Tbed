package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.ImgUrlUtil;
import cn.hellohao.utils.Print;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.ObjectMetadata;
import com.netease.cloud.services.nos.transfer.TransferManager;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
public class COSImageupload {
    static String BarrelName;
    static COSClient cosClient;
    static Keys key;

    public Map<ReturnImage, Integer> ImageuploadCOS(Map<String, MultipartFile> fileMap, String username,Map<String, String> fileMap2) throws Exception {
        // 要上传文件的路径
        if(fileMap2==null){
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                file = changeFile(entry.getValue());
                try {
                    // 指定要上传到的存储桶
                    String bucketName = BarrelName;
                    // 指定要上传到 COS 上对象键
                    String userkey =username + "/" + uuid+times + "." + entry.getKey();
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, userkey, file);
                    PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(key.getRequestAddress() + "/" + userkey);
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));

                } catch (CosServiceException serverException) {
                    serverException.printStackTrace();
                } catch (CosClientException clientException) {
                    clientException.printStackTrace();
                }
            }
            return ImgUrl;
        }else{
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            //设置Header
            ObjectMetadata meta = new ObjectMetadata();
            meta.setHeader("Content-Disposition", "inline");
            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String imgurl = entry.getValue();
                File file = new File(imgurl);
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    // 指定要上传到 COS 上对象键
                    String userkey =username + "/" + uuid+times + "." + entry.getKey();
                    PutObjectRequest putObjectRequest = new PutObjectRequest(BarrelName, userkey, file);
                    PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgurl(key.getRequestAddress() + "/" + userkey);

                    ImgUrl.put(returnImage, ImgUrlUtil.getFileSize2(new File(imgurl)));
                    boolean bb= new File(imgurl).getAbsoluteFile().delete();
                    Print.Normal("删除情况"+bb);
                } catch (Exception e) {
                    System.out.println("上传报错:" + e.getMessage());
                }
                if(fileInputStream!=null){
                    fileInputStream.close();
                    Print.Normal("流已经关闭");
                }
            }
            cosClient.shutdown();
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

    //初始化网易NOS对象存储
    public static void Initialize(Keys k) {
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
// 1 初始化用户身份信息（secretId, secretKey）。
            String secretId = k.getAccessKey();
            String secretKey = k.getAccessSecret();
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
            Region region = new Region(k.getEndpoint());
            ClientConfig clientConfig = new ClientConfig(region);
// 3 生成 cos 客户端。
            cosClient = new COSClient(cred, clientConfig);
            System.out.println("==="+cosClient);
//查询桶
            try {
                List<Bucket> buckets = cosClient.listBuckets();
                for (Bucket bucket : buckets) {
                    if (bucket.getName().equals(k.getBucketname())) {
                        Print.Normal("当前桶名称：" + bucket.getName());
                        BarrelName = bucket.getName();
                    }
                }
            } catch (CosServiceException serverException) {
                serverException.printStackTrace();
            } catch (CosClientException clientException) {
                clientException.printStackTrace();
            }
            key = k;
        }
    }
    /**
     * 客户端接口
     * */
    public Map<String, Integer> clientuploadCOS(Map<String, MultipartFile> fileMap, String username) throws Exception {

        File file = null;
        Map<String, Integer> ImgUrl = new HashMap<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            file = changeFile(entry.getValue());
            try {
                // 指定要上传到 COS 上对象键
                String userkey =username + "/" + uuid+times + "." + entry.getKey();
                PutObjectRequest putObjectRequest = new PutObjectRequest(BarrelName, userkey, file);
                PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                ImgUrl.put(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey(), (int) (entry.getValue().getSize()));

            } catch (Exception e) {
                System.out.println("上传报错:" + e.getMessage());
            }
        }
        return ImgUrl;

    }


}
