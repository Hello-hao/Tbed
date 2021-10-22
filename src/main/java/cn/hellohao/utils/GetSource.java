package cn.hellohao.utils;

import cn.hellohao.exception.StorageSourceInitException;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.service.impl.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/7 17:12
 */
public class GetSource {
    public static ReturnImage  storageSource(Integer type, Map<String, File> fileMap, String userpath,Integer keyID){
        NOSImageupload nosImageupload = SpringContextHolder.getBean(NOSImageupload.class);
        OSSImageupload ossImageupload = SpringContextHolder.getBean(OSSImageupload.class);
        USSImageupload ussImageupload = SpringContextHolder.getBean(USSImageupload.class);
        KODOImageupload kodoImageupload = SpringContextHolder.getBean(KODOImageupload.class);
        COSImageupload cosImageupload = SpringContextHolder.getBean(COSImageupload.class);
        FTPImageupload ftpImageupload = SpringContextHolder.getBean(FTPImageupload.class);
        UFileImageupload uFileImageupload = SpringContextHolder.getBean(UFileImageupload.class);
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
                returnImage =  ftpImageupload.ImageuploadFTP(fileMap, userpath,keyID);
            }else if(type==8){
                returnImage =  uFileImageupload.ImageuploadUFile(fileMap, userpath,keyID);
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
