package cn.hellohao.pojo;

public class UploadConfig {
    private String suffix;
    private String filesizetourists;
    private String filesizeuser;
    private Integer imgcounttourists;
    private Integer imgcountuser;
    private Integer urltype;
    private Integer isupdate;
    private Integer api;
    private String visitormemory;
    private String usermemory;
    private String blacklist;
    private Integer userclose;


    public UploadConfig() {

    }

    public UploadConfig(String suffix, String filesizetourists, String filesizeuser, Integer imgcounttourists,
                        Integer imgcountuser, Integer urltype, Integer isupdate, Integer api, String visitormemory,
                        String usermemory, String blacklist,Integer userclose) {
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
        this.userclose = userclose;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFilesizetourists() {
        return filesizetourists;
    }

    public void setFilesizetourists(String filesizetourists) {
        this.filesizetourists = filesizetourists;
    }

    public String getFilesizeuser() {
        return filesizeuser;
    }

    public void setFilesizeuser(String filesizeuser) {
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

    public String getVisitormemory() {
        return visitormemory;
    }

    public void setVisitormemory(String visitormemory) {
        this.visitormemory = visitormemory;
    }

    public String getUsermemory() {
        return usermemory;
    }

    public void setUsermemory(String usermemory) {
        this.usermemory = usermemory;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public Integer getUserclose() {
        return userclose;
    }

    public void setUserclose(Integer userclose) {
        this.userclose = userclose;
    }
}
