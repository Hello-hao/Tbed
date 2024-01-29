package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@Service
public class KODOImageupload {
    private static Logger logger = LoggerFactory.getLogger(KODOImageupload.class);
    static String upToken;
    static BucketManager bucketManager;
    static Keys key;

    public ReturnImage ImageuploadKODO(
            Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(key.getAccessKey(), key.getAccessSecret());
        String upToken = auth.uploadToken(key.getBucketname(), null, 7200, null);
        File file = null;
        FileInputStream stream = null;
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                stream = new FileInputStream(file);
                try {
                    Response response = uploadManager.put(stream,username + "/" + ShortUIDName + "." + prefix, upToken, null, null);
                    DefaultPutRet putRet =
                            new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                } catch (QiniuException ex) {
                    returnImage.setCode("400");
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                    }
                }
                try {if(stream!=null){stream.close();}} catch (Exception ex) { }
            }
            returnImage.setCode("200");
        } catch (Exception e) {
            try {if(stream!=null){stream.close();}} catch (Exception ex) { }
            logger.error("KODO发生异常：",e);
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
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(k.getAccessKey(), k.getAccessSecret());
        String upToken = auth.uploadToken(k.getBucketname(), null, 7200, null);
        BucketManager BM = new BucketManager(auth, cfg);
        BucketManager.FileListIterator fileListIterator = null;
        try {
            fileListIterator = BM.createFileListIterator(k.getBucketname(), "", 1, "/");
            FileInfo[] items = fileListIterator.next();
            if (items != null) {
                ret = 1;
                bucketManager = BM;
                key = k;
            }
        } catch (Exception e) {
            System.out.println("KODO Object Is null");
            ret = -1;
        }
        return ret;
    }

    public Boolean delKODO(Integer keyID, Images images) {
        boolean b = true;
        try {
            bucketManager.delete(key.getBucketname(), images.getImgname());
        } catch (Exception ex) {
            b = false;
        }
        return b;
    }
}
