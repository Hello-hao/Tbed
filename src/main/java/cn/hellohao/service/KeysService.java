package cn.hellohao.service;


import cn.hellohao.pojo.Keys;

import java.util.List;

public interface KeysService {
    //查询密钥
    Keys selectKeys(Integer storageType);

    //修改key
    Integer updateKey(Keys key);
    List<Keys> getKeys();
}
