package cn.hellohao.service.impl;

import cn.hellohao.dao.ImgTempMapper;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.ImgTemp;
import cn.hellohao.service.ImgTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*
*/
@Service
public class ImgTempServiceImpl implements ImgTempService {

    @Autowired
    ImgTempMapper imgDataExpMapper;


    @Override
    public List<Images> selectDelImgUidList(String datatime) {
        return imgDataExpMapper.selectDelImgUidList(datatime);
    }

    @Override
    public Integer delImgAndExp(String imguid) {
        return imgDataExpMapper.delImgAndExp(imguid);
    }

    @Override
    public Integer insertImgExp(ImgTemp imgDataExp) {
        return imgDataExpMapper.insertImgExp(imgDataExp);
    }
}
