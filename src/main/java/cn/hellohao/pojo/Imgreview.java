package cn.hellohao.pojo;

public class Imgreview {
    private Integer id;

    private String appId;

    private String apiKey;

    private String secretKey;

    private Integer using;

    private Integer count;

    public Imgreview() {
    }

    public Imgreview(Integer id, String appId, String apiKey, String secretKey, Integer using, Integer count) {
        this.id = id;
        this.appId = appId;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.using = using;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getUsing() {
        return using;
    }

    public void setUsing(Integer using) {
        this.using = using;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}