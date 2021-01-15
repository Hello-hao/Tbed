package cn.mq.tbed.service.impl;

import cn.mq.tbed.dao.ImgAndAlbumMapper;
import cn.mq.tbed.pojo.Images;
import cn.mq.tbed.pojo.ImgAndAlbum;
import cn.mq.tbed.service.ImgAndAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tiansh
 * @version 1.0
 * @date 2019/12/19 15:40
 */
@Service
public class ImgAndAlbumServiceImpl implements ImgAndAlbumService {
    @Autowired
    ImgAndAlbumMapper imgAndAlbumMapper;
    @Override
    public Integer addImgAndAlbum(ImgAndAlbum imgAndAlbum) {
        return imgAndAlbumMapper.addImgAndAlbum(imgAndAlbum);
    }

    @Override
    public List<ImgAndAlbum> getAlbumForImgname(String imgname) {
        return imgAndAlbumMapper.getAlbumForImgname(imgname);
    }

    @Override
    public Integer deleteImgAndAlbum(String imgname) {
        return imgAndAlbumMapper.deleteImgAndAlbum(imgname);
    }

    @Override
    public Integer deleteImgAndAlbumForKey(String albumkey) {
        return imgAndAlbumMapper.deleteImgAndAlbumForKey(albumkey);
    }

    @Override
    public List<Images> selectImgForAlbumkey(String albumkey) {
        return imgAndAlbumMapper.selectImgForAlbumkey(albumkey);
    }
}
