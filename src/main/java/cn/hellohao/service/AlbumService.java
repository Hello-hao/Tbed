package cn.hellohao.service;

import cn.hellohao.pojo.Album;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-12-18 22:15
 */
@Service
public interface AlbumService {

    JSONArray getAlbumList(JSONArray array);

    Album selectAlbum(Album album);

    Integer addAlbum(Album album);

    Integer deleteAlbum(String albumkey);

    List<Album> selectAlbumURLList(Album album);

    Integer selectAlbumCount(Integer userid);

    Integer updateAlbum(Album album);
}
