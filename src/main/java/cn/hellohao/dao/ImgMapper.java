package cn.hellohao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Images;

@Mapper
public interface ImgMapper {

	List<Images> selectimg(@Param("userid") Integer userid);
	Integer countimg(@Param("userid") Integer userid);
	Integer deleimg(@Param("id") Integer id);
	Images selectByPrimaryKey(@Param("id") Integer id);
	Integer counts(@Param("userid") Integer userid);
}
