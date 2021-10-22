package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021-10-22 11:35
 */
public class ReturnImage {
    private String uid;
    private String code;
    private String imgurl;
    private String imgname;
    private Long imgSize;

    public ReturnImage() {
    }

    public ReturnImage(String uid,String code, String imgurl, String imgname, Long imgSize) {
        this.uid = uid;
        this.code = code;
        this.imgurl = imgurl;
        this.imgname = imgname;
        this.imgSize = imgSize;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public Long getImgSize() {
        return imgSize;
    }

    public void setImgSize(Long imgSize) {
        this.imgSize = imgSize;
    }

}
