package cn.hellohao.pojo;

/**
 * By Hellohao
 * @TableName ImgTemp
 */
public class ImgTemp {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String imguid;


    /**
     * 
     */
    private String deltime;

    public ImgTemp() {
    }

    public ImgTemp(Integer id, String imguid, String deltime) {
        this.id = id;
        this.imguid = imguid;
        this.deltime = deltime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImguid() {
        return imguid;
    }

    public void setImguid(String imguid) {
        this.imguid = imguid;
    }

    public String getDeltime() {
        return deltime;
    }

    public void setDeltime(String deltime) {
        this.deltime = deltime;
    }


    @Override
    public String toString() {
        return "ImgTemp{" +
                "id=" + id +
                ", imguid='" + imguid + '\'' +
                ", deltime='" + deltime + '\'' +
                '}';
    }
}