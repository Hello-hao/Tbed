package cn.hellohao.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Images;

public interface ImgService {
    List<Images> selectimg(Images images);

    Integer deleimg(Integer id);

    Integer countimg(Integer userid);

    Images selectByPrimaryKey(Integer id);

    Integer counts(Integer userid);

    Integer setabnormal(String imgname);

    Integer deleimgname(String imgname);

    Integer deleall(Integer id);

    List<Images> gettimeimg(String time);
}
