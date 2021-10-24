package cn.hellohao.service;


import cn.hellohao.pojo.Keys;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KeysService {
    //查询密钥
    Keys selectKeys(Integer id);
    List<Keys> getStorageName();
    //修改key
    Integer updateKey(Keys key);
    List<Keys> getKeys();
}
