package cn.hellohao.service.impl;

import cn.hellohao.config.GlobalConstant;
import cn.hellohao.pojo.*;
import cn.hellohao.utils.*;
import com.UpYun;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.upyun.UpException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class USSImageupload {
    static UpYun upyun;
    static Keys key;

    public ReturnImage ImageuploadUSS(Map<String, File> fileMap, String username,Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                Msg fileMiME = TypeDict.FileMiME(file);
                meta.setHeader("content-type", fileMiME.getData().toString());
                upyun.setContentMD5(UpYun.md5(file));
                boolean result = upyun.writeFile(username + "/" + ShortUID + "." + entry.getKey(), file, true);
                if(result){
                    returnImage.setImgname(username + "/" + ShortUID + "." + entry.getKey());
                    returnImage.setImgurl(key.getRequestAddress() + "/" +username + "/" + ShortUID + "." + entry.getKey());
                    returnImage.setImgSize(entry.getValue().length());
                    returnImage.setCode("200");
                }else{
                    returnImage.setCode("400");
                    System.err.println("上传失败");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;

    }

    //初始化
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getStorageType()!=null && k.getAccessKey() != null && k.getAccessSecret() != null && k.getBucketname() != null
                && k.getRequestAddress() !=null ) {
            if(!k.getStorageType().equals("") && !k.getAccessKey().equals("") && !k.getAccessSecret().equals("") && !k.getBucketname().equals("")
                    && !k.getRequestAddress().equals("") ) {
                // 初始化
                // 创建UpYun实例。
                UpYun upObj= new UpYun(k.getBucketname(), k.getAccessKey(), k.getAccessSecret());
                List<UpYun.FolderItem> items = null;
                try {
                    items = upObj.readDir("/",null);
                    ret = 1;
                    upyun = upObj;
                    key = k;
                } catch (Exception e) {
                    System.out.println("USS Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public Boolean delUSS(Integer keyID, Images images) {
        boolean b = true;
        try {
            boolean result = upyun.deleteFile(images.getImgname(), null);
        } catch (Exception e) {
            e.printStackTrace();
            b=false;
        }
        return b;
    }


}
