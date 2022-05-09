package cn.hellohao.service;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.ImgTemp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*
*/
@Service
public interface ImgTempService {
    List<Images> selectDelImgUidList(String datatime);

    Integer delImgAndExp(String imguid);

    Integer insertImgExp(ImgTemp imgDataExp);
}
