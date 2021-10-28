package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.utils.*;
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
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
public class COSImageupload {
    static COSClient cosClient;
    static Keys key;

    public ReturnImage ImageuploadCOS(Map<String, File> fileMap, String username,Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                try {
                    String bucketName = key.getBucketname();
                    String userkey =username + "/" + ShortUID + "." + entry.getKey();
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, userkey, file);
                    PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                    returnImage.setImgname(userkey);
                    returnImage.setImgurl(key.getRequestAddress() + "/" + userkey);
                    returnImage.setImgSize(entry.getValue().length());
                    returnImage.setCode("200");
                } catch (CosServiceException serverException) {
                    serverException.printStackTrace();
                } catch (CosClientException clientException) {
                    clientException.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            returnImage.setCode("200");
        }
        return returnImage;
    }


    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
            if (!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress().equals("")) {
                String secretId = k.getAccessKey();
                String secretKey = k.getAccessSecret();
                COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
                Region region = new Region(k.getEndpoint());
                ClientConfig clientConfig = new ClientConfig(region);
                COSClient cosClient = new COSClient(cred, clientConfig);
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
                listObjectsRequest.setBucketName(k.getBucketname());
                listObjectsRequest.setDelimiter("/");
                listObjectsRequest.setMaxKeys(1);
                ObjectListing objectListing = null;
                try {
                    objectListing = cosClient.listObjects(listObjectsRequest);
                    ret = 1;
                    cosClient = cosClient;
                    key = k;
                } catch (Exception e) {
                    System.out.println("COS Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public Boolean delCOS(Integer keyID, String fileName) {
        boolean b = true;
        try {
            cosClient.deleteObject(key.getBucketname(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b ;
    }




}
