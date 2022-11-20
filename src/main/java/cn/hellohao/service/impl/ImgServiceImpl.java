package cn.hellohao.service.impl;

import cn.hellohao.dao.ImgMapper;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.User;
import cn.hellohao.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImgServiceImpl implements ImgService {
    @Autowired
    private ImgMapper imgMapper;

    @Override
    public List<Images> selectimg(Images images) {
        // TODO Auto-generated method stub
        return imgMapper.selectimg(images);
    }

    @Override
    public Integer insertImgData(Images images) {
        return imgMapper.insertImgData(images);
    }

    @Override
    public Integer deleimg(Integer id) {
        // TODO Auto-generated method stub
        return imgMapper.deleimg(id);
    }

    @Override
    public Integer deleimgForImgUid(String imguid) {
        return imgMapper.deleimgForImgUid(imguid);
    }

    public Images selectByPrimaryKey(Integer id) {
        return imgMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer counts(Integer userid) {
        // TODO Auto-generated method stub
        return imgMapper.counts(userid);
    }

    @Override
    public Integer countimg(Integer userid) {
        // TODO Auto-generated method stub
        return imgMapper.countimg(userid);
    }

    @Override
    public Integer setImg(Images images) {
        return imgMapper.setImg(images);
    }

    @Override
    public Integer deleimgname(String imgname) {
        return imgMapper.deleimgname(imgname);
    }

    @Override
    public Integer deleall(Integer id) {
        return imgMapper.deleall(id);
    }

    @Override
    public List<Images> gettimeimg(String time) {
        return imgMapper.gettimeimg(time);
    }

    @Override
    public Long getusermemory(Integer userid) {
        return imgMapper.getusermemory(userid);
    }

    @Override
    public Long getsourcememory(Integer source) {
        return imgMapper.getsourcememory(source);
    }

    @Override
    public Integer md5Count(Images images) {
        return imgMapper.md5Count(images);
    }

    @Override
    public List<Images> selectImgUrlByMD5(String md5key) {
        return imgMapper.selectImgUrlByMD5(md5key);
    }

    @Override
    public List<Images> RecentlyUploaded(Integer userid) {
        return imgMapper.RecentlyUploaded(userid);
    }

    @Override
    public List<User> RecentlyUser() {
        return imgMapper.RecentlyUser();
    }


    public List<String> getyyyy(Integer userid){
        return imgMapper.getyyyy(userid);
    }

    @Override
    public List<Images> countByM(Images images) {
        return imgMapper.countByM(images);
    }

    @Override
    public Images selectImgUrlByImgUID(String imguid) {
        return imgMapper.selectImgUrlByImgUID(imguid);
    }

}
