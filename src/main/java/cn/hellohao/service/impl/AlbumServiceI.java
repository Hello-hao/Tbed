package cn.hellohao.service.impl;

import cn.hellohao.dao.AlbumMapper;
import cn.hellohao.dao.ConfigMapper;
import cn.hellohao.dao.ImgAndAlbumMapper;
import cn.hellohao.exception.CodeException;
import cn.hellohao.pojo.Album;
import cn.hellohao.pojo.ImgAndAlbum;
import cn.hellohao.pojo.Msg;
import cn.hellohao.service.AlbumService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:30
 */
@Service
public class AlbumServiceI implements AlbumService {
    @Autowired
    AlbumMapper albumMapper;
    @Autowired
    ImgAndAlbumMapper andAlbumMapper;
    @Autowired
    ConfigMapper configMapper;

    @Override
    public Album selectAlbum(Album album) {
        return albumMapper.selectAlbum(album);
    }

    @Override
    public Integer addAlbum(Album album) {
        return albumMapper.addAlbum(album);
    }

    @Transactional
    public Integer addAlbumForImgAndAlbumMapper(ImgAndAlbum imgAndAlbum) {
        Integer tem = 0;
        Integer r2 = andAlbumMapper.addImgAndAlbum(imgAndAlbum);
        if(r2>0){
            tem = 1;
        }else{
            throw new CodeException("插入画廊数据失败，回滚");
        }
        return tem;
    }

    @Override
    public Integer deleteAlbum(String albumkey) {
        return albumMapper.deleteAlbum(albumkey);
    }

    @Override
    public List<Album> selectAlbumURLList(Album album) {
        return albumMapper.selectAlbumURLList(album);
    }

    @Transactional
    public Integer delete(String albumkey) {
        Integer ret1 = albumMapper.deleteAlbum(albumkey);
        if(ret1>0){
            ret1 = andAlbumMapper.deleteImgAndAlbumForKey(albumkey);
        }else{
            throw new CodeException("删除画廊失败。");
        }
        return ret1;
    }

    @Transactional
    public Integer deleteAll(String[] albumkeyArr) {
        Integer ret1 = 0 ;
        for (String s : albumkeyArr) {
            ret1 = albumMapper.deleteAlbum(s);
            if(ret1>0){
                ret1 = andAlbumMapper.deleteImgAndAlbumForKey(s);
            }else{
                throw new CodeException("删除画廊失败。");
            }
        }
        return ret1;
    }
}
