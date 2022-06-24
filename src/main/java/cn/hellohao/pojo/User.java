package cn.hellohao.pojo;


public class User {

    private Integer id;
    //@NotBlank(message = "用户名不能为空")
   // @Length(min = 6, max = 20, message = "用户名需要为 6 - 20 个字符")
    private String username;
    private String password;
    private String email;
    private String birthder;
    private Integer level;
    private String uid;
    private Integer isok;
    private  String memory;
    private Integer groupid;
    private String groupname;
    private String token;

    private Long counts;//用户img counts


    public User() {
        super();
    }

    public User(Integer id, String username, String password, String email, String birthder, Integer level, String uid,
                Integer isok, String memory, Integer groupid,String groupname,Long counts,String token) {
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
        this.groupname = groupname;
        this.counts = counts;
        this.token = token;
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

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Long getCounts() {
        return counts;
    }

    public void setCounts(Long counts) {
        this.counts = counts;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
