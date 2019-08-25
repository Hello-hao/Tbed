package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:01
 */
public class UserGroup {
    private Integer id;
    private Integer userid;
    private Integer groupid;

    public UserGroup() {
    }

    public UserGroup(Integer id, Integer userid, Integer groupid) {
        this.id = id;
        this.userid = userid;
        this.groupid = groupid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }
}
