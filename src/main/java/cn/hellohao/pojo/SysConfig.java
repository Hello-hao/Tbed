package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:27
 */
public class SysConfig {
    private Integer id;
    private Integer register;

    public SysConfig() {
    }

    public SysConfig(Integer id, Integer register) {
        this.id = id;
        this.register = register;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRegister() {
        return register;
    }

    public void setRegister(Integer register) {
        this.register = register;
    }
}
