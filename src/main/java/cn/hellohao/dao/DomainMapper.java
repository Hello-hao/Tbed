package cn.hellohao.dao;

import cn.hellohao.pojo.Domain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/21 9:46
 */
@Mapper
public interface DomainMapper {
    Integer getDomain(@Param("domain") String domain);

}
