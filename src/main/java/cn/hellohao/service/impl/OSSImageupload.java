package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.*;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class OSSImageupload {
    static OSSClient ossClient;
    static Keys key;

    public ReturnImage ImageuploadOSS(Map<String, File> fileMap, String username,Integer keyID){
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                file = entry.getValue();
                System.out.println("待上传的图片："+username + "/" + ShortUID + "." + entry.getKey());
                ossClient.putObject(key.getBucketname(), username + "/" + ShortUID + "." + entry.getKey(),file);
                returnImage.setImgname(username + "/" + ShortUID + "." + entry.getKey());//entry.getValue().getOriginalFilename()
                returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + entry.getKey());
                returnImage.setImgSize(file.length());
                returnImage.setCode("200");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;

    }

    public static Integer Initialize(Keys k) {
        int ret = -1;
        ObjectListing objectListing = null;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getAccessKey()!=null && k.getEndpoint()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getAccessKey().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ) {
                OSS ossClient = new OSSClientBuilder().build(k.getEndpoint(), k.getAccessKey(), k.getAccessSecret());
                try {
                    objectListing = ossClient.listObjects(k.getBucketname());
                    ret=1;
                    ossClient = ossClient;
                    key = k;
                } catch (Exception e) {
                    System.out.println("OSS Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public boolean delOSS(Integer keyID, String fileName){
        boolean b =true;
        try {
            ossClient.deleteObject(key.getBucketname(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }


}
