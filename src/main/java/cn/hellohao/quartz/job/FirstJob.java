package cn.hellohao.quartz.job;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.service.KeysService;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.LocUpdateImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class FirstJob {

	@Autowired
	private ImgServiceImpl imgService;
	@Autowired
	private NOSImageupload nosImageupload;
	@Autowired
	private OSSImageupload ossImageupload;
	@Autowired
	private COSImageupload cosImageupload;
	@Autowired
	private KODOImageupload kodoImageupload;
	@Autowired
	private USSImageupload ussImageupload;
	@Autowired
	private S3Imageupload s3Imageupload;
	@Autowired
	private WebDAVImageupload webDAVImageupload;
	@Autowired
	private FtpServiceImpl ftpService;
	@Autowired
	ImgTempService imgTempService;
	@Autowired
	KeysService keysService;

	private static FirstJob firstJob;

	@PostConstruct
	public void init() {
		firstJob = this;
	}

	//调用的任务主体
	public void task() {
//		System.out.println("定时任务.//开始");
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currdate = format.format(new Date());
			List<Images> imagesList = imgTempService.selectDelImgUidList(currdate);
			if(imagesList.size()==0){
//				System.out.println("定时任务.//没有期限图片");
				return;
			}
			for (Images images : imagesList) {
				imgTempService.delImgAndExp(images.getImguid());
				imgService.deleimgForImgUid(images.getImguid());
				Keys keys = keysService.selectKeys(images.getSource());
				if(keys.getStorageType()==1){
					firstJob.nosImageupload.delNOS(keys.getId(),images);
				}else if (keys.getStorageType()==2){
					firstJob.ossImageupload.delOSS(keys.getId(), images);
				}else if(keys.getStorageType()==3){
					firstJob.ussImageupload.delUSS(keys.getId(), images);
				}else if(keys.getStorageType()==4){
					firstJob.kodoImageupload.delKODO(keys.getId(), images);
				}else if(keys.getStorageType()==5){
					LocUpdateImg.deleteLOCImg(images);
				}else if(keys.getStorageType()==6){
					firstJob.cosImageupload.delCOS(keys.getId(),images);
				}else if(keys.getStorageType()==7){
					firstJob.ftpService.delFTP(keys.getId(), images);
				}else if(keys.getStorageType()==8){
					firstJob.s3Imageupload.deleS3(keys.getId(), images);
				}else if(keys.getStorageType()==9){
					firstJob.webDAVImageupload.delWebDAV(keys.getId(), images);
				}else{
					System.err.println("未获取到对象存储参数，上传失败。");
				}
			}
		}catch (Exception e){
			System.err.println("任务查询期限图像发生错误");
			e.printStackTrace();
		}


	}



}
