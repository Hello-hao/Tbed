package cn.hellohao.service;


import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImgService {
    List<Images> selectimg(Images images);

    Integer insertImgData(Images images);

    Integer deleimg(Long id);

    Integer deleimgForImgUid(String imguid);

    Integer countimg(Integer userid);

    Images selectByPrimaryKey(Long id);

    Images selectImgByBrieflink(String brieflink);

    Images selectImgByShortlink(String shortlink);

    Integer counts(Integer userid);

    Integer setImg(Images images);

    Integer deleimgname(String imgname);

    Integer deleall(Long id);

    List<Images> gettimeimg(String time);

    Long getusermemory(Integer userid);

    Long getsourcememory(Integer source);

    Integer md5Count(Images images);

    List<Images> selectImgUrlByMD5(Images images);

    List<Images> RecentlyUploaded(Integer userid);

    List<User> RecentlyUser();

    List<String> getyyyy(Integer userid);

    List<Images> countByM(Images images);

    Images selectImgUrlByImgUID( String imguid);

}
