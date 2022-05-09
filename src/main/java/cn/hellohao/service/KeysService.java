package cn.hellohao.service;


import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.Msg;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KeysService {
    Keys selectKeys(Integer id);

    List<Keys> getStorageName();

    List<Keys> getStorage();

    Msg updateKey(Keys key);

    List<Keys> getKeys();
}
