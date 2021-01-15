package cn.mq.tbed.dao;

import cn.mq.tbed.pojo.Imgreview;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImgreviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Imgreview record);

    int insertSelective(Imgreview record);

    Imgreview selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Imgreview record);

    int updateByPrimaryKey(Imgreview record);
}