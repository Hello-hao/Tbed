package cn.hellohao.dao;

import cn.hellohao.pojo.Code;
import cn.hellohao.pojo.Keys;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeMapper {
    //查询扩容码
    List<Code> selectCode(@Param("code") String code);
    Code selectCodekey(@Param("code") String code);
    //Integer selectCodekey(@Param("value") String value);
    //添加
    Integer addCode(Code code);
    //删除扩容码
    Integer deleteCode(@Param("code") String code);

}
