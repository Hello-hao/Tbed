package cn.hellohao.pojo;

public class Config {
    private Integer id;
    private Integer sourcekey;
    private Integer emails;
    private String webname;
    private String explain;
    private String logos;
    private String footed;
    private String links;
    private String notice;
    private String baidu;
    private String domain;
    private  String background1;
    private  String background2;


    public Config() {
    }

    public Config(Integer id, Integer sourcekey, Integer emails, String webname, String explain, String logos, String footed, String links, String notice, String baidu, String domain, String background1, String background2) {
        this.id = id;
        this.sourcekey = sourcekey;
        this.emails = emails;
        this.webname = webname;
        this.explain = explain;
        this.logos = logos;
        this.footed = footed;
        this.links = links;
        this.notice = notice;
        this.baidu = baidu;
        this.domain = domain;
        this.background1 = background1;
        this.background2 = background2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourcekey() {
        return sourcekey;
    }

    public void setSourcekey(Integer sourcekey) {
        this.sourcekey = sourcekey;
    }

    public Integer getEmails() {
        return emails;
    }

    public void setEmails(Integer emails) {
        this.emails = emails;
    }

    public String getWebname() {
        return webname;
    }

    public void setWebname(String webname) {
        this.webname = webname;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getLogos() {
        return logos;
    }

    public void setLogos(String logos) {
        this.logos = logos;
    }

    public String getFooted() {
        return footed;
    }

    public void setFooted(String footed) {
        this.footed = footed;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getBaidu() {
        return baidu;
    }

    public void setBaidu(String baidu) {
        this.baidu = baidu;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBackground1() {
        return background1;
    }

    public void setBackground1(String background1) {
        this.background1 = background1;
    }

    public String getBackground2() {
        return background2;
    }

    public void setBackground2(String background2) {
        this.background2 = background2;
    }
}
