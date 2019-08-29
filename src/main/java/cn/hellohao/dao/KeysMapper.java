package cn.hellohao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Keys;

import java.util.List;

@Mapper
public interface KeysMapper {
    //查询密钥
    Keys selectKeys(@Param("storageType") Integer storageType);
    //修改key
    Integer updateKey(Keys key);
    List<Keys> getKeys();

}
