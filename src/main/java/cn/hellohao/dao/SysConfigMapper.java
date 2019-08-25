package cn.hellohao.dao;

import cn.hellohao.pojo.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:33
 */
@Mapper
public interface SysConfigMapper {
    SysConfig getstate();
    Integer setstate(SysConfig sysConfig);
}
