package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.Msg;
import cn.hellohao.service.IRedisService;
import cn.hellohao.service.ImgAndAlbumService;
import cn.hellohao.service.ImgService;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.utils.LocUpdateImg;
import cn.hellohao.utils.progress.MyProgress;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class deleImages {

  @Autowired private NOSImageupload nosImageupload;
  @Autowired private OSSImageupload ossImageupload;
  @Autowired private COSImageupload cosImageupload;
  @Autowired private KODOImageupload kodoImageupload;
  @Autowired private USSImageupload ussImageupload;
  @Autowired private S3Imageupload s3Imageupload;
  @Autowired private FtpServiceImpl ftpService;
  @Autowired private WebDAVImageupload webDAVImageupload;
  @Autowired private ImgAndAlbumService imgAndAlbumService;
  @Autowired private ImgTempService imgTempService;
  @Autowired private ImgService imgService;
  @Autowired private KeysServiceImpl keysService;
  @Autowired private IRedisService iRedisService;

  public Msg dele(boolean forceDel,String uuid, Long... imgIds) {
    Msg msg = new Msg();
    MyProgress myProgress = new MyProgress();
    myProgress.InitializeDelImg();
    List<Long> ids = new ArrayList<>();
    int successCount = 0;
    List<String> errorIds = new ArrayList<>();
    for (int i = 0; i < imgIds.length; i++) {
      // 删除图片
      boolean isDele = false;
      try {
        Images image = imgService.selectByPrimaryKey(imgIds[i]);
        if (forceDel) {
          try {
            imgAndAlbumService.deleteImgAndAlbum(image.getImgurl());
            imgTempService.delImgAndExp(image.getImguid());
            imgService.deleimg(image.getId());
            ids.add(image.getId());
            successCount++;
            System.out.println("删除成功加一个" + image.getId());
          } catch (Exception e) {
            e.printStackTrace();
            System.err.println(image.getImgname() + ":图片数据库记录时发生错误");
            errorIds.add(image.getImgurl());
          }
        }
        Keys key = keysService.selectKeys(image.getSource());
        if (key.getStorageType() == 1) {
          isDele = nosImageupload.delNOS(key.getId(), image);
        } else if (key.getStorageType() == 2) {
          isDele = ossImageupload.delOSS(key.getId(), image);
        } else if (key.getStorageType() == 3) {
          isDele = ussImageupload.delUSS(key.getId(), image);
        } else if (key.getStorageType() == 4) {
          isDele = kodoImageupload.delKODO(key.getId(), image);
        } else if (key.getStorageType() == 5) {
          isDele = LocUpdateImg.deleteLOCImg(image);
        } else if (key.getStorageType() == 6) {
          isDele = cosImageupload.delCOS(key.getId(), image);
        } else if (key.getStorageType() == 7) {
          isDele = ftpService.delFTP(key.getId(), image);
        } else if (key.getStorageType() == 8) {
          isDele = s3Imageupload.deleS3(key.getId(), image);
        }else if (key.getStorageType() == 9) {
          isDele = webDAVImageupload.delWebDAV(key.getId(), image);
        } else {
          System.err.println("未获取到对象存储参数，删除失败。");
        }
        if (!forceDel) {
          try {
            imgAndAlbumService.deleteImgAndAlbum(image.getImgurl());
            imgTempService.delImgAndExp(image.getImguid());
            imgService.deleimg(image.getId());
            ids.add(image.getId());
            successCount++;
            System.out.println("删除成功加一个" + image.getId());
          } catch (Exception e) {
            e.printStackTrace();
            System.err.println(image.getImgname() + ":图片数据库记录时发生错误");
            errorIds.add(image.getImgurl());
          }
        }

        if(uuid!=null){
          myProgress.setDelSuccessCount(successCount);
          myProgress.setDelSuccessImgList(ids);
          myProgress.setDelErrorImgListt(errorIds);
          if (imgIds.length == (i + 1)) {
            myProgress.setDelOCT(1);
          } else {
            myProgress.setDelOCT(0);
          }
          iRedisService.setTimeValue(uuid, JSONObject.toJSONString(myProgress), 24L);
        }
      } catch (Exception e) {
        System.err.println("删除的时候发生了一些异常");
        ids.add(imgIds[i]);
        successCount++;
        if(uuid!=null){
          myProgress.setDelSuccessCount(successCount);
          myProgress.setDelSuccessImgList(ids);
          myProgress.setDelErrorImgListt(errorIds);
          if (imgIds.length == (i + 1)) {
            myProgress.setDelOCT(1);
          } else {
            myProgress.setDelOCT(0);
          }
          iRedisService.setTimeValue(uuid, JSONObject.toJSONString(myProgress), 24L);
        }
        e.printStackTrace();
      }
    }
    msg.setData(ids);
    return msg;
  }

  public Msg dele2(String... imgIds) {
    Msg msg = new Msg();
    List<Long> ids = new ArrayList<>();
    for (int i = 0; i < imgIds.length; i++) {
      try {
        Images image = imgService.selectByPrimaryKey(Long.valueOf(imgIds[i]));
        Keys key = keysService.selectKeys(image.getSource());
        if (key.getStorageType() == 1) {
          nosImageupload.delNOS(key.getId(), image);
        } else if (key.getStorageType() == 2) {
          ossImageupload.delOSS(key.getId(), image);
        } else if (key.getStorageType() == 3) {
          ussImageupload.delUSS(key.getId(), image);
        } else if (key.getStorageType() == 4) {
          kodoImageupload.delKODO(key.getId(), image);
        } else if (key.getStorageType() == 5) {
          LocUpdateImg.deleteLOCImg(image);
        } else if (key.getStorageType() == 6) {
          cosImageupload.delCOS(key.getId(), image);
        } else if (key.getStorageType() == 7) {
          ftpService.delFTP(key.getId(), image);
        } else if (key.getStorageType() == 8) {
          s3Imageupload.deleS3(key.getId(), image);
        }else if (key.getStorageType() == 9) {
          webDAVImageupload.delWebDAV(key.getId(), image);
        } else {
          System.err.println("未获取到对象存储参数，删除失败。");
        }
        try {
          imgAndAlbumService.deleteImgAndAlbum(image.getImgurl());
          imgTempService.delImgAndExp(image.getImguid());
          imgService.deleimg(image.getId());
          ids.add(image.getId());
        } catch (Exception e) {
          e.printStackTrace();
          System.err.println(image.getImgname() + ":图片数据库记录时发生错误");
        }
      } catch (Exception e) {
        System.err.println("删除的时候发生了一些异常");
//        ids.add(Long.valueOf(imgIds[i]));
        e.printStackTrace();
      }
    }
    msg.setData(ids);
    return msg;
  }


}
