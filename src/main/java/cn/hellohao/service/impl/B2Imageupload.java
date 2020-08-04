package cn.hellohao.service.impl;

import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.SetFiles;
import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;
import com.backblaze.b2.client.contentSources.B2FileContentSource;
import com.backblaze.b2.client.structures.B2FileVersion;
import com.backblaze.b2.client.structures.B2UploadFileRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class B2Imageupload {

    private static Keys key;
    private static B2StorageClient client ;

    //初始化
    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ) {
                client = B2StorageClientFactory
                        .createDefaultFactory()
                        .create( k.getAccessKey(),  k.getAccessSecret(), "backblaze-b2/4.0.0+java/1.8");
                key = k;
                ret=1;
            }
        }
        return ret;
    }

    public Map<ReturnImage, Integer> ImageuploadB2(Map<String, MultipartFile> fileMap, String username,
                                                    Map<String, String> fileMap2,Integer setday) throws Exception {

        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String head = "";
            if(entry.getKey().equals("jpg")||entry.getKey().equals("jpeg")){
                head = "image/jpeg";
            }else if(entry.getKey().equals("png")){
                head = "image/png";
            }else if(entry.getKey().equals("bmp")){
                head = "image/bmp";
            }else if(entry.getKey().equals("gif")){
                head = "image/gif";
            }else{
                //System.err.println("位置格式文件，无法定义header头。");
                head = "image/"+entry.getKey();
            }

            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);//生成一个没有-的uuid，然后取前5位
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
            String times = format1.format(new Date());
            String fileName = username + "/" + uuid + times + "." + entry.getKey(); // The name of the file you are uploading

            File file = SetFiles.changeFile(entry.getValue());
            B2FileContentSource b2FileContentSource = B2FileContentSource.build(file);
            B2UploadFileRequest request = B2UploadFileRequest
                    .builder(key.getBucketname(), fileName, head, b2FileContentSource)
                    .build();
            B2FileVersion b2FileVersion = client.uploadSmallFile(request);

            ReturnImage returnImage = new ReturnImage();
            returnImage.setImgname(b2FileVersion.getFileName());//entry.getValue().getOriginalFilename()
            returnImage.setImgurl(key.getRequestAddress()+b2FileVersion.getFileName());
            ImgUrl.put(returnImage, (int) (entry.getValue().getSize()));
        }
        return ImgUrl;
    }

}
