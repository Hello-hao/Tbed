package cn.hellohao.pojo;

public class Keys {
    private Integer id;
    private String AccessKey;
    private String AccessSecret;
    private String Endpoint;
    private String Bucketname;
    private String RequestAddress;
    private Integer storageType;

    public Keys() {
        super();
    }

    public Keys(Integer id, String accessKey, String accessSecret, String endpoint, String bucketname,
                String requestAddress, Integer storageType) {
        super();
        this.id = id;
        AccessKey = accessKey;
        AccessSecret = accessSecret;
        Endpoint = endpoint;
        Bucketname = bucketname;
        RequestAddress = requestAddress;
        this.storageType = storageType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessKey() {
        return AccessKey;
    }

    public void setAccessKey(String accessKey) {
        AccessKey = accessKey;
    }

    public String getAccessSecret() {
        return AccessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        AccessSecret = accessSecret;
    }

    public String getEndpoint() {
        return Endpoint;
    }

    public void setEndpoint(String endpoint) {
        Endpoint = endpoint;
    }

    public String getBucketname() {
        return Bucketname;
    }

    public void setBucketname(String bucketname) {
        Bucketname = bucketname;
    }

    public String getRequestAddress() {
        return RequestAddress;
    }

    public void setRequestAddress(String requestAddress) {
        RequestAddress = requestAddress;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }


}
