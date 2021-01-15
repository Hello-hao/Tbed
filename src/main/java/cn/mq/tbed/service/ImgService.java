package cn.mq.tbed.service;


import java.util.List;

import cn.mq.tbed.pojo.Images;

public interface ImgService {
    List<Images> selectimg(Images images);

    Integer deleimg(Integer id);

    Integer countimg(Integer userid);

    Images selectByPrimaryKey(Integer id);

    Integer counts(Integer userid);

    Integer setImg(Images images);

    Integer deleimgname(String imgname);

    Integer deleall(Integer id);

    List<Images> gettimeimg(String time);

    Integer getusermemory(Integer userid);

    Integer md5Count(String md5key);

    Images selectImgUrlByMD5(String md5key);

}
