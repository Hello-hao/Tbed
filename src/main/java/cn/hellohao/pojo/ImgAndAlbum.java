package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:12
 */
public class ImgAndAlbum {
    private String imgname;
    private String albumkey;
    private String notes;

    public ImgAndAlbum() {
    }

    public ImgAndAlbum(String imgname, String albumkey, String notes) {
        this.imgname = imgname;
        this.albumkey = albumkey;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
