package cn.hellohao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.Bucket;
import com.netease.cloud.services.nos.model.CannedAccessControlList;
import com.netease.cloud.services.nos.transfer.TransferManager;

import cn.hellohao.dao.ImgMapper;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.service.ImgService;

@Service
public class ImgServiceImpl implements ImgService {
	@Autowired
	private ImgMapper imgMapper;

	@Override
	public List<Images> selectimg(Integer userid) {
		// TODO Auto-generated method stub
		return imgMapper.selectimg(userid);
	}

	@Override
	public Integer deleimg(Integer id) {
		// TODO Auto-generated method stub
		return imgMapper.deleimg(id);
	}


	public Images selectByPrimaryKey(Integer id) {
		return imgMapper.selectByPrimaryKey(id);
	}

	//删除对象存储的图片文件
	public void delect(Keys key,String fileName) {
		// 初始化
		Credentials credentials = new BasicCredentials(key.getAccessKey(), key.getAccessSecret());
		NosClient nosClient = new NosClient(credentials);
		nosClient.setEndpoint(key.getEndpoint());
		// 初始化TransferManager
		TransferManager transferManager = new TransferManager(nosClient);

		//列举桶
		ArrayList bucketList = new ArrayList();
		String tname = "";
		 for (Bucket bucket : nosClient.listBuckets()) {
		    bucketList.add(bucket.getName());
		 }
		 for (Object object : bucketList) {
			System.out.println("桶名："+object.toString());
			tname = object.toString();
			 //查看桶的ACL
			 CannedAccessControlList acl = nosClient.getBucketAcl(object.toString());
			// bucket权限
			System.out.println("这个桶的权限是："+acl.toString());
		}	
		 //这是删除文件的方法
		 boolean isExist = nosClient.doesObjectExist(tname,fileName,null);
		 System.out.println("文件是否存在："+isExist);
		 
		 nosClient.deleteObject(tname,fileName);

	}

	@Override
	public Integer counts(Integer userid) {
		// TODO Auto-generated method stub
		return imgMapper.counts(userid);
	}

	@Override
	public Integer countimg(Integer userid) {
		// TODO Auto-generated method stub
		return imgMapper.countimg(userid);
	}

}
