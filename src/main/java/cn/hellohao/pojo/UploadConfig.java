package cn.hellohao.pojo;

public class UploadConfig {
    private String suffix;
    private Integer filesizetourists;
    private Integer filesizeuser;
    private Integer imgcounttourists;
    private Integer imgcountuser;
    private Integer urltype;
    private Integer isupdate;
    private Integer api;
    private Integer visitormemory;
    private Integer usermemory;
    private String blacklist;


    public UploadConfig() {
    }

    public UploadConfig(String suffix, Integer filesizetourists, Integer filesizeuser, Integer imgcounttourists, Integer imgcountuser, Integer urltype, Integer isupdate, Integer api, Integer visitormemory, Integer usermemory, String blacklist) {
        this.suffix = suffix;
        this.filesizetourists = filesizetourists;
        this.filesizeuser = filesizeuser;
        this.imgcounttourists = imgcounttourists;
        this.imgcountuser = imgcountuser;
        this.urltype = urltype;
        this.isupdate = isupdate;
        this.api = api;
        this.visitormemory = visitormemory;
        this.usermemory = usermemory;
        this.blacklist = blacklist;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getFilesizetourists() {
        return filesizetourists;
    }

    public void setFilesizetourists(Integer filesizetourists) {
        this.filesizetourists = filesizetourists;
    }

    public Integer getFilesizeuser() {
        return filesizeuser;
    }

    public void setFilesizeuser(Integer filesizeuser) {
        this.filesizeuser = filesizeuser;
    }

    public Integer getImgcounttourists() {
        return imgcounttourists;
    }

    public void setImgcounttourists(Integer imgcounttourists) {
        this.imgcounttourists = imgcounttourists;
    }

    public Integer getImgcountuser() {
        return imgcountuser;
    }

    public void setImgcountuser(Integer imgcountuser) {
        this.imgcountuser = imgcountuser;
    }

    public Integer getUrltype() {
        return urltype;
    }

    public void setUrltype(Integer urltype) {
        this.urltype = urltype;
    }

    public Integer getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(Integer isupdate) {
        this.isupdate = isupdate;
    }

    public Integer getApi() {
        return api;
    }

    public void setApi(Integer api) {
        this.api = api;
    }

    public Integer getVisitormemory() {
        return visitormemory;
    }

    public void setVisitormemory(Integer visitormemory) {
        this.visitormemory = visitormemory;
    }

    public Integer getUsermemory() {
        return usermemory;
    }

    public void setUsermemory(Integer usermemory) {
        this.usermemory = usermemory;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }
}
