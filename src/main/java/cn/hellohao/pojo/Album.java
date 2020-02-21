package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:13
 */
public class Album {
    private String  albumkey;
    private String albumtitle;
    private String createdate;
    private String password;
    private Integer userid;
    private String username;

    public Album() {
    }

    public Album(String albumkey, String albumtitle, String createdate, String password, Integer userid, String username) {
        this.albumkey = albumkey;
        this.albumtitle = albumtitle;
        this.createdate = createdate;
        this.password = password;
        this.userid = userid;
        this.username = username;
    }

    public String getAlbumkey() {
        return albumkey;
    }

    public void setAlbumkey(String albumkey) {
        this.albumkey = albumkey;
    }

    public String getAlbumtitle() {
        return albumtitle;
    }

    public void setAlbumtitle(String albumtitle) {
        this.albumtitle = albumtitle;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
