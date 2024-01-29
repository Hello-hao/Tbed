package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@Service
public class COSImageupload {
    private static Logger logger = LoggerFactory.getLogger(COSImageupload.class);
    static COSClient cosClient;
    static Keys key;

    public ReturnImage ImageuploadCOS(
            Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        FileInputStream stream = null;
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                stream = new FileInputStream(file);
                try {
                    String bucketName = key.getBucketname();
                    String userkey = username + "/" + ShortUIDName + "." + prefix;
                    PutObjectRequest putObjectRequest =
                            new PutObjectRequest(bucketName, userkey, stream,null);
                    PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                } catch (CosServiceException serverException) {
                    returnImage.setCode("400");
                    serverException.printStackTrace();
                } catch (CosClientException clientException) {
                    returnImage.setCode("400");
                    clientException.printStackTrace();
                }
                try {if(stream!=null){stream.close();}} catch (Exception ex) { }
            }
            returnImage.setCode("200");
        } catch (Exception e) {
            try {if(stream!=null){stream.close();}} catch (Exception ex) { }
            logger.error("COS协议上传时发生异常：",e);
            returnImage.setCode("500");
        }
        return returnImage;
    }

    public static Integer Initialize(Keys k) {
        int ret = -1;
        if (StringUtils.isBlank(k.getAccessKey())
                || StringUtils.isBlank(k.getAccessSecret())
                || StringUtils.isBlank(k.getEndpoint())
                || StringUtils.isBlank(k.getBucketname())
                || StringUtils.isBlank(k.getRequestAddress())) {
            return -1;
        }
        String secretId = k.getAccessKey();
        String secretKey = k.getAccessSecret();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(k.getEndpoint());
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cos = new COSClient(cred, clientConfig);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(k.getBucketname());
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setMaxKeys(1);
        ObjectListing objectListing = null;
        try {
            objectListing = cos.listObjects(listObjectsRequest);
            ret = 1;
            cosClient = cos;
            key = k;
        } catch (Exception e) {
            System.out.println("COS Object Is null");
            ret = -1;
        }
        return ret;
    }

    public Boolean delCOS(Integer keyID, Images images) {
        boolean b = true;
        try {
            cosClient.deleteObject(key.getBucketname(), images.getImgname());
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
}
