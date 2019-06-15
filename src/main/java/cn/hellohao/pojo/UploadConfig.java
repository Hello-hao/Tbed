package cn.hellohao.pojo;

public class UploadConfig {
    private String suffix;
    private Integer filesizetourists;
    private Integer filesizeuser;
    private Integer imgcounttourists;
    private Integer imgcountuser;

    public UploadConfig() {
    }

    public UploadConfig(String suffix, Integer filesizetourists, Integer filesizeuser, Integer imgcounttourists, Integer imgcountuser) {
        this.suffix = suffix;
        this.filesizetourists = filesizetourists;
        this.filesizeuser = filesizeuser;
        this.imgcounttourists = imgcounttourists;
        this.imgcountuser = imgcountuser;
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
}
