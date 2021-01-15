package cn.mq.tbed.service;

import cn.mq.tbed.pojo.Album;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:15
 */
@Service
public interface AlbumService {

    Album selectAlbum(Album album);

    Integer addAlbum(Album album);

    Integer deleteAlbum(String albumkey);

    List<Album> selectAlbumURLList(Album album);
}
