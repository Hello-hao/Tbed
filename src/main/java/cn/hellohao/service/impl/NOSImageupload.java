package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.SetText;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.ObjectListing;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class NOSImageupload {
    static NosClient nosClient;
    static Keys key;

    public ReturnImage Imageupload(Map<Map<String, String>, File> fileMap, String username,Integer keyID){
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                nosClient.putObject(
                        key.getBucketname(), username + "/" + ShortUIDName + "." + prefix, file);
            }
            returnImage.setCode("200");
        } catch (Exception e) {
            e.printStackTrace();
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
        Credentials credentials = new BasicCredentials(k.getAccessKey(), k.getAccessSecret());
        NosClient nosObj = new NosClient(credentials);
        nosObj.setEndpoint(k.getEndpoint());
        ObjectListing objectListing = null;
        try {
            objectListing = nosObj.listObjects(k.getBucketname());
            ret = 1;
            nosClient = nosObj;
            key = k;
        }catch (Exception e){
            System.out.println("NOS Object Is null");
            ret = -1;
        }
        return ret;
    }

    public Boolean delNOS(Integer keyID, Images images) {
        boolean b =true;
        try {
            nosClient.deleteObject(key.getBucketname(), images.getImgname());
        } catch (Exception e) {
            e.printStackTrace();
            b =false;
        }
        return b;
    }


}
