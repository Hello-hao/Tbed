package cn.hellohao.pojo;

public class AppClient {

    private String id;
    private String isuse;
    private String winpackurl;
    private String macpackurl;
    private String appname;
    private String applogo;
    private String appupdate;


    public AppClient(){}

    public AppClient(String id, String isuse, String winpackurl, String macpackurl, String appname, String applogo, String appupdate) {
        this.id = id;
        this.isuse = isuse;
        this.winpackurl = winpackurl;
        this.macpackurl = macpackurl;
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

    public String getWinpackurl() {
        return winpackurl;
    }

    public void setWinpackurl(String winpackurl) {
        this.winpackurl = winpackurl;
    }

    public String getMacpackurl() {
        return macpackurl;
    }

    public void setMacpackurl(String macpackurl) {
        this.macpackurl = macpackurl;
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
