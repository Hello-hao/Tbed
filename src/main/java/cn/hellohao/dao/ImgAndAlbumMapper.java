package cn.hellohao.dao;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.ImgAndAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:15
 */
@Mapper
public interface ImgAndAlbumMapper {
    Integer addImgAndAlbum(ImgAndAlbum imgAndAlbum);

    List<ImgAndAlbum> getAlbumForImgname(@Param("imgname") String imgname);

    Integer deleteImgAndAlbum(@Param("imgname") String imgname);

    Integer deleteImgAndAlbumForKey(@Param("albumkey") String albumkey);

    List<Images> selectImgForAlbumkey(@Param("albumkey") String albumkey);
}
