package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:27
 */
public class SysConfig {
    private Integer id;
    private Integer register;
    private String checkduplicate;

    public SysConfig() {
    }

    public SysConfig(Integer id, Integer register, String checkduplicate) {
        this.id = id;
        this.register = register;
        this.checkduplicate = checkduplicate;
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

    public String getCheckduplicate() {
        return checkduplicate;
    }

    public void setCheckduplicate(String checkduplicate) {
        this.checkduplicate = checkduplicate;
    }


}
