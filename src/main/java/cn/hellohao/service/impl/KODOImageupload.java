package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.utils.DateUtils;
import cn.hellohao.utils.DeleImg;
import cn.hellohao.utils.ImgUrlUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class KODOImageupload {
    static String upToken;
    static UploadManager uploadManager;
    static Keys key;

    public Map<ReturnImage, Integer> ImageuploadKODO(Map<String, MultipartFile> fileMap, String username,
                                                     Map<String, String> fileMap2,Integer setday) throws Exception {
        if(fileMap2==null){
            File file = null;
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();

            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                file = changeFile(entry.getValue());
                try {
                    Response response = uploadManager.put(file,username + "/" + uuid+times + "." + entry.getKey(),upToken);
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                    if(setday>0) {
                        String deleimg = DateUtils.plusDay(setday);
                        DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "4");
                    }
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                    }
                }
            }
            return ImgUrl;
        }else{
            Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
            for (Map.Entry<String, String> entry : fileMap2.entrySet()) {
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
                String times = format1.format(new Date());
                String imgurl = entry.getValue();
                System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
                try {
                    Response response = uploadManager.put(new File(imgurl),username + "/" + uuid+times + "." + entry.getKey(),upToken);
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put(returnImage, ImgUrlUtil.getFileSize2(new File(imgurl)));
                    if(setday>0) {
                        String deleimg = DateUtils.plusDay(setday);
                        DeleImg.charu(username + "/" + uuid + times + "." + entry.getKey() + "|" + deleimg + "|" + "4");
                    }
                    new File(imgurl).delete();
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                    }
                }
            }
            return ImgUrl;
        }
    }

    // 转换文件方法
    private File changeFile(MultipartFile multipartFile) throws Exception {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // todo 修改临时文件文件名
        File file = File.createTempFile(fileName, prefix);
        multipartFile.transferTo(file);
        return file;
    }

    //初始化
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if (k.getEndpoint() != null && k.getAccessSecret() != null && k.getEndpoint() != null
                && k.getBucketname() != null && k.getRequestAddress() != null) {
            if (!k.getEndpoint().equals("") && !k.getAccessSecret() .equals("") && !k.getEndpoint() .equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress() .equals("")) {
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
                uploadManager = new UploadManager(cfg);
                Auth auth = Auth.create(k.getAccessKey(), k.getAccessSecret());
                upToken = auth.uploadToken(k.getBucketname());
                key = k;
                ret = 1;
            }
        }
        return ret;
    }

    /**
     * 客户端接口
     * */
    public Map<ReturnImage, Integer> clientuploadKODO(Map<String, MultipartFile> fileMap, String username, UploadConfig uploadConfig) throws Exception {
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            file = changeFile(entry.getValue());
            System.out.println("待上传的图片："+username + "/" + uuid+times + "." + entry.getKey());
            try {
                ReturnImage returnImage = new ReturnImage();
                if(entry.getValue().getSize()/1024<=uploadConfig.getFilesizeuser()*1024){
                    Response response = uploadManager.put(file,username + "/" + uuid+times + "." + entry.getKey(),upToken);
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl(key.getRequestAddress() + "/" + username + "/" + uuid+times + "." + entry.getKey());
                    ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
                }else{
                    returnImage.setImgname(entry.getValue().getOriginalFilename());
                    returnImage.setImgurl("文件超出系统设定大小，不得超过");
                    ImgUrl.put(returnImage, -1);
                }
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                }
            }
        }
        return ImgUrl;
    }


}
