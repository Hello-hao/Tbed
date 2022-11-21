package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.Msg;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.TypeDict;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class OSSImageupload {
    static OSS ossClient;
    static Keys key;

    public ReturnImage ImageuploadOSS(
            Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                Msg fileMiME = TypeDict.FileMiME(file);
                meta.setHeader("content-type", fileMiME.getData().toString());
                PutObjectRequest putObjectRequest =
                        new PutObjectRequest(
                                key.getBucketname(),
                                username + "/" + ShortUIDName + "." + prefix,
                                file,
                                meta);
                ossClient.putObject(putObjectRequest);
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
        ObjectListing objectListing = null;
        if (StringUtils.isBlank(k.getAccessKey())
                || StringUtils.isBlank(k.getAccessSecret())
                || StringUtils.isBlank(k.getEndpoint())
                || StringUtils.isBlank(k.getBucketname())
                || StringUtils.isBlank(k.getRequestAddress())) {
            return -1;
        }
        OSS ossObj =
                new OSSClientBuilder()
                        .build(k.getEndpoint(), k.getAccessKey(), k.getAccessSecret());
        try {
            objectListing = ossObj.listObjects(k.getBucketname());
            ret = 1;
            ossClient = ossObj;
            key = k;
        } catch (Exception e) {
            System.out.println("OSS Object Is null");
            ret = -1;
        }

        return ret;
    }

    public boolean delOSS(Integer keyID, Images images) {
        boolean b = true;
        try {
            ossClient.deleteObject(key.getBucketname(), images.getImgname());
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
}
