package cn.hellohao.service;


import java.util.List;

import cn.hellohao.pojo.User;
import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Images;
import org.springframework.stereotype.Service;

@Service
public interface ImgService {
    List<Images> selectimg(Images images);

    Integer insertImgData(Images images);

    Integer deleimg(Integer id);

    Integer deleimgForImgUid(String imguid);

    Integer countimg(Integer userid);

    Images selectByPrimaryKey(Integer id);

    Integer counts(Integer userid);

    Integer setImg(Images images);

    Integer deleimgname(String imgname);

    Integer deleall(Integer id);

    List<Images> gettimeimg(String time);

    Long getusermemory(Integer userid);

    Long getsourcememory(Integer source);

    Integer md5Count(Images images);

    List<Images> selectImgUrlByMD5(String md5key);

    List<Images> RecentlyUploaded(Integer userid);

    List<User> RecentlyUser();

    List<String> getyyyy(Integer userid);

    List<Images> countByM(Images images);

    Images selectImgUrlByImgUID( String imguid);

}
