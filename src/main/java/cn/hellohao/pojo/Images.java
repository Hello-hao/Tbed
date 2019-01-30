package cn.hellohao.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Images {
    // 默认的时间字符串格式
    
//id, imgname, imgurl, userid
	private Integer id;
	private String imgname;
	private String imgurl;
	private Integer userid;
	private Integer sizes=0;
	private Date updatetime;
	private Integer storageType;
	
	
	public Images() {
		super();
	}


	public Images(Integer id, String imgname, String imgurl, Integer userid, Integer sizes, Date updatetime,
			Integer storageType) {
		super();
		this.id = id;
		this.imgname = imgname;
		this.imgurl = imgurl;
		this.userid = userid;
		this.sizes = sizes;
		this.updatetime = updatetime;
		this.storageType = storageType;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getImgname() {
		return imgname;
	}


	public void setImgname(String imgname) {
		this.imgname = imgname;
	}


	public String getImgurl() {
		return imgurl;
	}


	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}


	public Integer getUserid() {
		return userid;
	}


	public void setUserid(Integer userid) {
		this.userid = userid;
	}


	public Integer getSizes() {
		return sizes;
	}


	public void setSizes(Integer sizes) {
		this.sizes = sizes;
	}


	public Date getUpdatetime() {
		return  updatetime;
	}


	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}


	public Integer getStorageType() {
		
		return storageType;
	}


	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	}
	
		

