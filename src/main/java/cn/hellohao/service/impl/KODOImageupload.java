package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class KODOImageupload {
    static String upToken;
    static BucketManager bucketManager;
    static Keys key;

    public ReturnImage ImageuploadKODO(
            Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        Configuration cfg;
        if (key.getEndpoint().equals("1")) {
            cfg = new Configuration(Zone.zone0());
        } else if (key.getEndpoint().equals("2")) {
            cfg = new Configuration(Zone.zone1());
        } else if (key.getEndpoint().equals("3")) {
            cfg = new Configuration(Zone.zone2());
        } else if (key.getEndpoint().equals("4")) {
            cfg = new Configuration(Zone.zoneNa0());
        } else {
            cfg = new Configuration(Zone.zoneAs0());
        }
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(key.getAccessKey(), key.getAccessSecret());
        String upToken = auth.uploadToken(key.getBucketname(), null, 7200, null);
        File file = null;
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                try {
                    Response response =
                            uploadManager.put(
                                    file, username + "/" + ShortUIDName + "." + prefix, upToken);
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
        if (org.apache.commons.lang3.StringUtils.isBlank(k.getAccessKey())
                || org.apache.commons.lang3.StringUtils.isBlank(k.getAccessSecret())
                || org.apache.commons.lang3.StringUtils.isBlank(k.getEndpoint())
                || org.apache.commons.lang3.StringUtils.isBlank(k.getBucketname())
                || StringUtils.isBlank(k.getRequestAddress())) {
            return -1;
        }
        Configuration cfg;
        if (k.getEndpoint().equals("1")) {
            cfg = new Configuration(Zone.zone0());
        } else if (k.getEndpoint().equals("2")) {
            cfg = new Configuration(Zone.zone1());
        } else if (k.getEndpoint().equals("3")) {
            cfg = new Configuration(Zone.zone2());
        } else if (k.getEndpoint().equals("4")) {
            cfg = new Configuration(Zone.zoneNa0());
        } else {
            cfg = new Configuration(Zone.zoneAs0());
        }
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(k.getAccessKey(), k.getAccessSecret());
        String upToken =
                auth.uploadToken(
                        k.getBucketname(),
                        null,
                        7200,
                        null); // auth.uploadToken(k.getBucketname());
        BucketManager bmObj = new BucketManager(auth, cfg);
        BucketManager.FileListIterator fileListIterator = null;
        try {
            fileListIterator = bmObj.createFileListIterator(k.getBucketname(), "", 1, "/");
            FileInfo[] items = fileListIterator.next();
            if (items != null) {
                ret = 1;
                bucketManager = bmObj;
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
