package cn.mq.tbed.service;

import cn.mq.tbed.pojo.Images;
import cn.mq.tbed.pojo.ImgAndAlbum;
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
