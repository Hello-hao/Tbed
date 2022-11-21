package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.Msg;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.SetText;
import cn.hellohao.utils.TypeDict;
import com.UpYun;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class UFileImageupload {
    static UpYun uFile;
    static Keys key;

    /*
    * 废弃方法待补位
    * */

    public ReturnImage ImageuploadUFile(
            Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                Msg fileMiME = TypeDict.FileMiME(file);
                meta.setHeader("content-type", fileMiME.getData().toString());
                uFile.setContentMD5(UpYun.md5(file));
                boolean result =
                        uFile.writeFile(
                                username + "/" + ShortUID + "." + entry.getKey(), file, true);
                if (result) {
                    returnImage.setImgname(username + "/" + ShortUID + "." + entry.getKey());
                    returnImage.setImgurl(
                            key.getRequestAddress()
                                    + "/"
                                    + username
                                    + "/"
                                    + ShortUID
                                    + "."
                                    + entry.getKey());
                    returnImage.setImgSize(entry.getValue().length());
                    returnImage.setCode("200");
                } else {
                    returnImage.setCode("400");
                    System.err.println("上传失败");
                }
            }
        } catch (Exception e) {
            returnImage.setCode("500");
        }
        returnImage.setCode("500");
        return returnImage;
    }

    public static Integer Initialize(Keys k) {
        int ret = -1;
        if (k.getAccessSecret() != null
                && k.getAccessKey() != null
                && k.getBucketname() != null
                && k.getRequestAddress() != null) {
            if (!k.getAccessSecret().equals("")
                    && !k.getAccessKey().equals("")
                    && !k.getBucketname().equals("")
                    && !k.getRequestAddress().equals("")) {
                UpYun ufObj = new UpYun(k.getBucketname(), k.getAccessKey(), k.getAccessSecret());
                List<UpYun.FolderItem> items = null;
                try {
                    items = ufObj.readDir("/", null);
                    ret = 1;
                    uFile = ufObj;
                    key = k;
                } catch (Exception e) {
                    System.out.println("UFile Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public Boolean delUFile(Integer keyID, Images images) {
        boolean b = true;
        try {
            boolean result = uFile.deleteFile(images.getImgname(), null);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
}
