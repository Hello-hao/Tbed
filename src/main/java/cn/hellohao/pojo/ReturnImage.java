package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-22 11:35
 */
public class ReturnImage {
    private String imgurl;
    private String imgname;

    public ReturnImage() {
    }

    public ReturnImage(String imgurl, String imgname) {
        this.imgurl = imgurl;
        this.imgname = imgname;
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
}
