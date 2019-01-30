package cn.hellohao.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Images;

public interface ImgService {
	List<Images> selectimg(Integer id);
	Integer deleimg(Integer id);
	Integer countimg(Integer userid);
	Images selectByPrimaryKey(Integer id);
	Integer counts(Integer userid);
}
