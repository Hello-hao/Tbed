package cn.hellohao.service.impl;

import cn.hellohao.dao.ImgMapper;
import cn.hellohao.dao.SysConfigMapper;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Msg;
import cn.hellohao.pojo.SysConfig;
import cn.hellohao.pojo.User;
import cn.hellohao.service.ImgService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImgServiceImpl implements ImgService {
    @Autowired
    private ImgMapper imgMapper;
    @Autowired
    SysConfigMapper sysConfigMapper;
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
    public Integer deleimg(Long id) {
        // TODO Auto-generated method stub
        return imgMapper.deleimg(id);
    }

    @Override
    public Integer deleimgForImgUid(String imguid) {
        return imgMapper.deleimgForImgUid(imguid);
    }

    public Images selectByPrimaryKey(Long id) {
        return imgMapper.selectByPrimaryKey(id);
    }

    @Override
    public Images selectImgByBrieflink(String brieflink) {
        return imgMapper.selectImgByBrieflink(brieflink);
    }

    @Override
    public Images selectImgByShortlink(String shortlink) {
        return imgMapper.selectImgByShortlink(shortlink);
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
    public Integer deleall(Long id) {
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
    public List<Images> selectImgUrlByMD5(Images images) {
        return imgMapper.selectImgUrlByMD5(images);
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

    @Transactional
    public Msg insertImgDataForCheck(Images images, User user, JSONObject confdata, String flilename)throws Exception {
        Msg msg = new Msg();
        JSONObject jsonObject = new JSONObject();
        SysConfig sysConfig = sysConfigMapper.getstate();
        if(sysConfig.getCheckduplicate().equals("1")){
            //查重
            Images img = new Images();
            img.setUserid(user==null?0:user.getId());
            img.setMd5key(images.getMd5key());
            List<Images> list = imgMapper.selectImgUrlByMD5(img);
            if (list.size() > 0) {
                jsonObject.put("url", list.get(0).getImgurl());
                jsonObject.put("name", flilename);
                jsonObject.put("imguid", list.get(0).getImguid());
                System.err.println(jsonObject.toJSONString());
                msg.setData(jsonObject);
                msg.setCode("000");
                return msg;
            }else{
                imgMapper.insertImgData(images);
                msg.setCode("200");
                return msg;
            }
        }else{
            //不查重
            imgMapper.insertImgData(images);
            msg.setCode("200");
        }
        return msg;
    }


}
