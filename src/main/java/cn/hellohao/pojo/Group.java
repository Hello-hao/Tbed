package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 15:59
 */
public class Group {
    private Integer id;
    private String groupname;
    private Integer keyid;

    public Group() {
    }

    public Group(Integer id, String groupname, Integer keyid) {
        this.id = id;
        this.groupname = groupname;
        this.keyid = keyid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Integer getKeyid() {
        return keyid;
    }

    public void setKeyid(Integer keyid) {
        this.keyid = keyid;
    }
}
