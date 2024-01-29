package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.Msg;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.TypeDict;
import com.UpYun;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@Service
public class USSImageupload {
    private static Logger logger = LoggerFactory.getLogger(USSImageupload.class);
    static UpYun upyun;
    static Keys key;

    public ReturnImage ImageuploadUSS(
            Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        FileInputStream stream = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                stream = new FileInputStream(file);
                Msg fileMiME = TypeDict.FileMiME(stream);
                meta.setHeader("content-type", fileMiME.getData().toString());
                upyun.setContentMD5(UpYun.md5(file));
                boolean result =
                        upyun.writeFile(username + "/" + ShortUIDName + "." + prefix, stream, true, null);
                if (result) {

                } else {
                    System.err.println("上传失败");
                    returnImage.setCode("400");
                }
                try {if(stream!=null){stream.close();}} catch (Exception e) { }
            }
            returnImage.setCode("200");
        } catch (Exception e) {
            try {if(stream!=null){stream.close();}} catch (Exception ex) { }
            logger.error("OSS上传发生异常：",e);
            returnImage.setCode("500");
        }
        return returnImage;
    }

    // 初始化
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if (StringUtils.isBlank(k.getAccessKey())
                || StringUtils.isBlank(k.getKeyname())
                || StringUtils.isBlank(k.getAccessSecret())
                || StringUtils.isBlank(k.getEndpoint())
                || StringUtils.isBlank(k.getBucketname())
                || StringUtils.isBlank(k.getRequestAddress())
                || k.getStorageType() == null) {
            return -1;
        }
        UpYun upObj = new UpYun(k.getBucketname(), k.getAccessKey(), k.getAccessSecret());
        List<UpYun.FolderItem> items = null;
        try {
            items = upObj.readDir("/", null);
            ret = 1;
            upyun = upObj;
            key = k;
        } catch (Exception e) {
            System.out.println("USS Object Is null");
            ret = -1;
        }
        return ret;
    }

    public Boolean delUSS(Integer keyID, Images images) {
        boolean b = true;
        try {
            boolean result = upyun.deleteFile(images.getImgname(), null);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
}
