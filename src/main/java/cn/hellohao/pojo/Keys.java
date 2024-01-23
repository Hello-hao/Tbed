package cn.hellohao.pojo;

import lombok.Data;

@Data
public class Keys {
    private Integer id;
    private String AccessKey;
    private String AccessSecret;
    private String Endpoint;
    private String Region;
    private String Bucketname;
    private String RequestAddress;
    private Integer storageType;
    private String keyname;
    private String RootPath;//存入的目录
    private Boolean SysTransmit;

    public Boolean getSysTransmit() {
        return SysTransmit==null?false:SysTransmit;
    }
}
