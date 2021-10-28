package cn.hellohao.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import cn.hellohao.exception.StorageSourceInitException;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.utils.*;
import com.netease.cloud.services.nos.model.NOSObjectSummary;
import com.netease.cloud.services.nos.model.ObjectListing;
import com.netease.cloud.services.nos.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.Bucket;
import com.netease.cloud.services.nos.transfer.TransferManager;

import cn.hellohao.pojo.Keys;

@Service
public class NOSImageupload {
    static NosClient nosClient;
    static Keys key;

    public ReturnImage Imageupload(Map<String, File> fileMap, String username,Integer keyID){
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                nosClient.putObject(key.getBucketname(), username + "/" + ShortUID+ "." + entry.getKey(), file);
                returnImage.setImgname(username + "/" + ShortUID+ "." + entry.getKey());
                returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + entry.getKey());
                returnImage.setImgSize(entry.getValue().length());
                returnImage.setCode("200");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;

    }


    //初始化网易NOS对象存储
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ){
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ){
                // 初始化
                Credentials credentials = new BasicCredentials(k.getAccessKey(), k.getAccessSecret());
                NosClient nosClient = new NosClient(credentials);
                nosClient.setEndpoint(k.getEndpoint());
                ObjectListing objectListing = null;
                try {
                    objectListing = nosClient.listObjects(k.getBucketname());
                    ret = 1;
                    nosClient = nosClient;
                    key = k;
                }catch (Exception e){
                    System.out.println("NOS Object Is null");
                    ret = -1;
                }
            }
        }
        //throw new StorageSourceInitException("当前数据源配置不完整，请管理员前往后台配置。");
        return ret;
    }


    public Boolean delNOS(Integer keyID, String fileName) {
        boolean b =true;
        try {
            //这种方法不能删除指定文件夹下的文件
            boolean isExist = nosClient.doesObjectExist(key.getBucketname(), fileName, null);
            if (isExist) {
                nosClient.deleteObject(key.getBucketname(), fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            b =false;
        }
        return b;
    }


}
