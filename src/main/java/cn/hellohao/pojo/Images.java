package cn.hellohao.pojo;

public class Images {
    // 默认的时间字符串格式

    //id, imgname, imgurl, userid
    private Integer id;
    private String imgname;
    private String imgurl;
    private Integer userid;
    private String sizes;
    private String abnormal;
    private Integer source;
    private Integer imgtype;
    private String updatetime;
    private String username;
    private Integer storageType;
    private String starttime;
    private String stoptime;
    private String explains;
    private String md5key;
    private String notes;
    private String useridlist;
    private String imguid;
    private String format;
    private String about;
    private Integer great;
    private String violation;
    private String albumtitle;
    //@Length(min = 0, max = 10, message = "画廊密码不能超过10个字符")
    private String password;
    private Integer selecttype;
    private Long countNum;
    private Integer monthNum;
    private String yyyy;
    private String[] classifuidlist; //类别uid集合
    private String classificationuid; //类别uid集合
    private String idname;
    private Integer isRepeat;//如果没有开启查重的时候他是0，开启为1

    public Images() {
        super();
    }

    public Images(String imgurl, String sizes, String abnormal, String updatetime, String username, String md5key, String imguid) {
        this.imgurl = imgurl;
        this.sizes = sizes;
        this.abnormal = abnormal;
        this.updatetime = updatetime;
        this.username = username;
        this.md5key = md5key;
        this.imguid = imguid;
    }

    public Images(Integer id, String imgname, String imgurl, Integer userid, String sizes, String abnormal, Integer source,
                  Integer imgtype, String updatetime, String username, Integer storageType, String starttime, String stoptime,
                  String explains, String md5key, String notes, String useridlist, String imguid, String albumtitle,
                  String password, Integer selecttype,Long countNum,Integer monthNum,String yyyy,
                  String format,String about,Integer great,String[] classifuidlist,String classificationuid,String violation,
                  String idname,Integer isRepeat) {
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
        this.explains = explains;
        this.md5key = md5key;
        this.notes = notes;
        this.useridlist = useridlist;
        this.imguid = imguid;
        this.albumtitle = albumtitle;
        this.password = password;
        this.selecttype = selecttype;
        this.countNum = countNum;
        this.monthNum = monthNum;
        this.yyyy = yyyy;
        this.format = format;
        this.about = about;
        this.great = great;
        this.classifuidlist = classifuidlist;
        this.classificationuid = classificationuid;
        this.violation = violation;
        this.idname = idname;
        this.isRepeat = isRepeat;

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

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
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

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getMd5key() {
        return md5key;
    }

    public void setMd5key(String md5key) {
        this.md5key = md5key;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUseridlist() {
        return useridlist;
    }

    public void setUseridlist(String useridlist) {
        this.useridlist = useridlist;
    }

    public String getImguid() {
        return imguid;
    }

    public void setImguid(String imguid) {
        this.imguid = imguid;
    }


    public String getAlbumtitle() {
        return albumtitle;
    }

    public void setAlbumtitle(String albumtitle) {
        this.albumtitle = albumtitle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSelecttype() {
        return selecttype;
    }

    public void setSelecttype(Integer selecttype) {
        this.selecttype = selecttype;
    }

    public Long getCountNum() {
        return countNum;
    }

    public void setCountNum(Long countNum) {
        this.countNum = countNum;
    }

    public Integer getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(Integer monthNum) {
        this.monthNum = monthNum;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getGreat() {
        return great;
    }

    public void setGreat(Integer great) {
        this.great = great;
    }

    public String[] getClassifuidlist() {
        return classifuidlist;
    }

    public void setClassifuidlist(String[] classifuidlist ) {
        this.classifuidlist = classifuidlist;
    }

    public String getClassificationuid() {
        return classificationuid;
    }

    public void setClassificationuid(String classificationuid) {
        this.classificationuid = classificationuid;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getIdname() {
        return idname;
    }

    public void setIdname(String idname) {
        this.idname = idname;
    }

    public Integer getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(Integer isRepeat) {
        this.isRepeat = isRepeat;
    }
}
	
		

