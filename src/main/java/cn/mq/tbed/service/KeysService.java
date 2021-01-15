package cn.mq.tbed.service;


import cn.mq.tbed.pojo.Keys;

import java.util.List;

public interface KeysService {
    //查询密钥
    Keys selectKeys(Integer storageType);

    //修改key
    Integer updateKey(Keys key);
    List<Keys> getKeys();
}
