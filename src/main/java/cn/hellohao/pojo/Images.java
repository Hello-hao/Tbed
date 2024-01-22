package cn.hellohao.pojo;

import lombok.Data;

@Data
public class Images {
    //id, imgname, imgurl, userid
    private Long id;
    private String imgname;
    private String imgurl;
    private Integer userid;
    private String sizes;
    private String abnormal;
    private Integer source;
    private Integer imgtype;
    private String updatetime;
    private String username;
    private Integer storageType;
    private String starttime;
    private String stoptime;
    private String explains;
    private String md5key;
    private String notes;
    private String useridlist;
    private String imguid;
    private String format;
    private String about;
    private Integer great;
    private String violation;
    private String albumtitle;
    //@Length(min = 0, max = 10, message = "画廊密码不能超过10个字符")
    private String password;
    private Integer selecttype;
    private Long countNum;
    private Integer monthNum;
    private String yyyy;
    private String[] classifuidlist; //类别uid集合
    private String classificationuid; //类别uid集合
    private String idname;
    private Integer isRepeat;//如果没有开启查重的时候他是0，开启为1
    private String searchname;
    private String brieflink;
    private String shortlink;
    private String order;
}
	
		

