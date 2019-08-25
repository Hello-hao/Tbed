package cn.hellohao.pojo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class User {

    private Integer id;
    //@NotBlank(message = "用户名不能为空")
   // @Length(min = 6, max = 20, message = "用户名需要为 6 - 20 个字符")
    private String username;
    //@NotBlank(message = "密码不能为空")
    private String password;
   // @NotBlank(message = "邮箱不能为空")
    //@Email(message = "邮箱格式不正确")
    private String email;
    private String birthder;
    private Integer level;
    private String uid;
    private Integer isok;
    private  Integer memory;
    private Integer groupid;


    public User() {
        super();
    }

    public User(Integer id, String username, String password, String email, String birthder, Integer level, String uid, Integer isok, Integer memory, Integer groupid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthder = birthder;
        this.level = level;
        this.uid = uid;
        this.isok = isok;
        this.memory = memory;
        this.groupid = groupid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthder() {
        return birthder;
    }

    public void setBirthder(String birthder) {
        this.birthder = birthder;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getIsok() {
        return isok;
    }

    public void setIsok(Integer isok) {
        this.isok = isok;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }
}
