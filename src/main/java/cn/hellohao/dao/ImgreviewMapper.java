package cn.hellohao.dao;

import cn.hellohao.pojo.Imgreview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImgreviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Imgreview record);

    int insertSelective(Imgreview record);

    Imgreview selectByPrimaryKey(@Param("id") Integer id);

    Imgreview selectByusing(@Param("using") Integer using);

    int updateByPrimaryKeySelective(Imgreview record);

    int updateByPrimaryKey(Imgreview record);
}