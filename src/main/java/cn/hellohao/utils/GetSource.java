package cn.hellohao.utils;

import cn.hellohao.exception.StorageSourceInitException;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.service.impl.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/7 17:12
 */
public class GetSource {
    public static Map<ReturnImage, Integer>  storageSource(Integer type, Map<String, MultipartFile> fileMap, String userpath,
                                                           Map<String, String> filename,Integer setday){
        NOSImageupload nosImageupload = SpringContextHolder.getBean(NOSImageupload.class);
        OSSImageupload ossImageupload = SpringContextHolder.getBean(OSSImageupload.class);
        USSImageupload ussImageupload = SpringContextHolder.getBean(USSImageupload.class);
        KODOImageupload kodoImageupload = SpringContextHolder.getBean(KODOImageupload.class);
        COSImageupload cosImageupload = SpringContextHolder.getBean(COSImageupload.class);
        FTPImageupload ftpImageupload = SpringContextHolder.getBean(FTPImageupload.class);
        Map<ReturnImage, Integer> m = null;
        try {
            if(type==1){
            m = nosImageupload.Imageupload(fileMap, userpath,filename,setday);
            }else if (type==2){
                m = ossImageupload.ImageuploadOSS(fileMap, userpath,filename,setday);
            }else if(type==3){
                m = ussImageupload.ImageuploadUSS(fileMap, userpath,filename,setday);
            }else if(type==4){
                m = kodoImageupload.ImageuploadKODO(fileMap, userpath,filename,setday);
            }else if(type==5){
                m = LocUpdateImg.ImageuploadLOC(fileMap, userpath,filename,setday);
            }else if(type==6){
                m = cosImageupload.ImageuploadCOS(fileMap, userpath,filename,setday);
            }else if(type==7){
                m =  ftpImageupload.ImageuploadFTP(fileMap, userpath,filename,setday);
            }
            else{
                new StorageSourceInitException("GetSource类捕捉异常：未找到存储源");
            }
        } catch (Exception e) {
            new StorageSourceInitException("GetSource类捕捉异常：",e);
            e.printStackTrace();
        }
        return m;
    }


}
