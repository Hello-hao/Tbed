package cn.hellohao.dao;

import cn.hellohao.pojo.Confdata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ConfdataMapper {
    Confdata selectConfdata(@Param("key") String key);
    Integer updateConfdata(Confdata confdata);
}
