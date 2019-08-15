package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:27
 */
public class SysConfig {
    private Integer id;
    private String register;

    public SysConfig() {
    }

    public SysConfig(Integer id, String register) {
        this.id = id;
        this.register = register;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
