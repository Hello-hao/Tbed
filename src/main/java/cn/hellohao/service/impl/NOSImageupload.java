package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.SetText;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.ObjectListing;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

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


    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ){
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ){
                // 初始化
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
            }
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
