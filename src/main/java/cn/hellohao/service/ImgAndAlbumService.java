package cn.hellohao.service;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.ImgAndAlbum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:29
 */
@Service
public interface ImgAndAlbumService {
    Integer addImgAndAlbum(ImgAndAlbum imgAndAlbum);

    List<ImgAndAlbum> getAlbumForImgname(String imgname);

    Integer deleteImgAndAlbum(String imgname);

    Integer deleteImgAndAlbumForKey(String albumkey);

    List<Images> selectImgForAlbumkey(String albumkey);
}
