package cn.hellohao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Images;

@Mapper
public interface ImgMapper {

    List<Images> selectimg(Images images);

    Integer countimg(@Param("userid") Integer userid);

    Integer deleimg(@Param("id") Integer id);

    Images selectByPrimaryKey(@Param("id") Integer id);

    Integer counts(@Param("userid") Integer userid);

    Integer setImg(Images images);

    Integer deleimgname(@Param("imgname") String imgname);
    Integer deleall(@Param("id") Integer id);

    List<Images> gettimeimg(@Param("time") String time);

    Integer getusermemory(@Param("userid") Integer userid);

    Integer md5Count(@Param("md5key") String md5key);

    Images selectImgUrlByMD5(@Param("md5key") String md5key);


}
