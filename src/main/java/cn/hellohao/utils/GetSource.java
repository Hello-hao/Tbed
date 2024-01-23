package cn.hellohao.utils;

import cn.hellohao.exception.StorageSourceInitException;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/7 17:12
 */
@Service
public class GetSource {

    @Autowired
    NOSImageupload nosImageupload;
    @Autowired
    OSSImageupload ossImageupload;
    @Autowired
    USSImageupload ussImageupload;
    @Autowired
    KODOImageupload kodoImageupload;
    @Autowired
    COSImageupload cosImageupload;
    @Autowired
    FtpServiceImpl ftpService;
    @Autowired
    S3Imageupload s3Imageupload;
    @Autowired
    WebDAVImageupload webDAVImageupload;
    public ReturnImage storageSource(Integer type, Map<Map<String, String>, File> fileMap, String userpath,Integer keyID){
        ReturnImage returnImage = null;
        try {
            if(type==1){
                returnImage = nosImageupload.Imageupload(fileMap, userpath,keyID);
            }else if (type==2){
                returnImage = ossImageupload.ImageuploadOSS(fileMap, userpath,keyID);
            }else if(type==3 ){
                returnImage = ussImageupload.ImageuploadUSS(fileMap, userpath,keyID);
            }else if(type==4){
                returnImage = kodoImageupload.ImageuploadKODO(fileMap, userpath,keyID);
            }else if(type==5){
                returnImage = LocUpdateImg.ImageuploadLOC(fileMap, userpath,keyID);
            }else if(type==6){
                returnImage = cosImageupload.ImageuploadCOS(fileMap, userpath,keyID);;
            }else if(type==7){
                returnImage =  ftpService.FtpUploadFile(fileMap, userpath,keyID);
            }else if(type==8){
                returnImage =  s3Imageupload.ImageuploadS3(fileMap, userpath,keyID);
            }else if(type==9){
                returnImage =  webDAVImageupload.ImageuploadWebDAV(fileMap, userpath,keyID);
            }
            else{
                new StorageSourceInitException("GetSource类捕捉异常：未找到存储源");
            }
        } catch (Exception e) {
            new StorageSourceInitException("GetSource类捕捉异常：",e);
            e.printStackTrace();
        }
        return returnImage;
    }


}
