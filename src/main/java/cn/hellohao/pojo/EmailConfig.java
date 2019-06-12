package cn.hellohao.pojo;

public class EmailConfig {
    private Integer id ;
    private String emails;
    private String emailkey;
    private String emailurl;
    private String port;
    private String emailname;
    private Integer using ;

    public EmailConfig() {
    }

    public EmailConfig(Integer id, String emails, String emailkey, String emailurl, String port, String emailname, Integer using) {
        this.id = id;
        this.emails = emails;
        this.emailkey = emailkey;
        this.emailurl = emailurl;
        this.port = port;
        this.emailname = emailname;
        this.using = using;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getEmailkey() {
        return emailkey;
    }

    public void setEmailkey(String emailkey) {
        this.emailkey = emailkey;
    }

    public String getEmailurl() {
        return emailurl;
    }

    public void setEmailurl(String emailurl) {
        this.emailurl = emailurl;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getEmailname() {
        return emailname;
    }

    public void setEmailname(String emailname) {
        this.emailname = emailname;
    }

    public Integer getUsing() {
        return using;
    }

    public void setUsing(Integer using) {
        this.using = using;
    }
}
