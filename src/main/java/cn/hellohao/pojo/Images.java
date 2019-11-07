package cn.hellohao.pojo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public class Images {
    // 默认的时间字符串格式

    //id, imgname, imgurl, userid
    private Integer id;
    private String imgname;
    private String imgurl;
    private Integer userid;
    private Integer sizes = 0;
    private String abnormal;
    private Integer source;
    private Integer imgtype;
    private String updatetime;
    private String username;
    private Integer storageType;
    private String starttime;
    private String stoptime;


    public Images() {
        super();
    }

    public Images(Integer id, String imgname, String imgurl, Integer userid, Integer sizes, String abnormal, Integer source, Integer imgtype, String updatetime, String username, Integer storageType, String starttime, String stoptime) {
        this.id = id;
        this.imgname = imgname;
        this.imgurl = imgurl;
        this.userid = userid;
        this.sizes = sizes;
        this.abnormal = abnormal;
        this.source = source;
        this.imgtype = imgtype;
        this.updatetime = updatetime;
        this.username = username;
        this.storageType = storageType;
        this.starttime = starttime;
        this.stoptime = stoptime;
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

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getImgtype() {
        return imgtype;
    }

    public void setImgtype(Integer imgtype) {
        this.imgtype = imgtype;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }
}
	
		

