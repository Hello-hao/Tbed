package cn.hellohao.pojo;

public class AppClient {

    private String id;
    private String isuse;
    private String packurl;
    private String appname;
    private String applogo;
    private String appupdate;

    public AppClient(){}

    public AppClient(String id, String isuse, String packurl, String appname, String applogo, String appupdate) {
        this.id = id;
        this.isuse = isuse;
        this.packurl = packurl;
        this.appname = appname;
        this.applogo = applogo;
        this.appupdate = appupdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsuse() {
        return isuse;
    }

    public void setIsuse(String isuse) {
        this.isuse = isuse;
    }

    public String getPackurl() {
        return packurl;
    }

    public void setPackurl(String packurl) {
        this.packurl = packurl;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getApplogo() {
        return applogo;
    }

    public void setApplogo(String applogo) {
        this.applogo = applogo;
    }

    public String getAppupdate() {
        return appupdate;
    }

    public void setAppupdate(String isupdate) {
        this.appupdate = isupdate;
    }
}
