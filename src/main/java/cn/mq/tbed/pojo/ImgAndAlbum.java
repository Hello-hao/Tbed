package cn.mq.tbed.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:12
 */
public class ImgAndAlbum {
    private String imgname;
    private String albumkey;

    public ImgAndAlbum() {
    }

    public ImgAndAlbum(String imgname, String albumkey) {
        this.imgname = imgname;
        this.albumkey = albumkey;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getAlbumkey() {
        return albumkey;
    }

    public void setAlbumkey(String albumkey) {
        this.albumkey = albumkey;
    }
}
