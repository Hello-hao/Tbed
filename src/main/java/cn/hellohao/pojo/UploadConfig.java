package cn.hellohao.pojo;

public class UploadConfig {
    private String suffix;
    private Integer filesizetourists;
    private Integer filesizeuser;
    private Integer imgcounttourists;
    private Integer imgcountuser;
    private Integer urltype;

    public UploadConfig() {
    }

    public UploadConfig(String suffix, Integer filesizetourists, Integer filesizeuser, Integer imgcounttourists, Integer imgcountuser, Integer urltype) {
        this.suffix = suffix;
        this.filesizetourists = filesizetourists;
        this.filesizeuser = filesizeuser;
        this.imgcounttourists = imgcounttourists;
        this.imgcountuser = imgcountuser;
        this.urltype = urltype;
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
}
