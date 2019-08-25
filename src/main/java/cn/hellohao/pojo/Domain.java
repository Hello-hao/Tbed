package cn.hellohao.pojo;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/21 9:43
 */
public class Domain {
    private Integer id;
    private String domain;
    private String code;

    public Domain() {
    }

    public Domain(Integer id, String domain, String code) {
        this.id = id;
        this.domain = domain;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
