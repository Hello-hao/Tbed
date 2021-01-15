package cn.mq.tbed.service.impl;

import cn.mq.tbed.service.KeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mq.tbed.dao.KeysMapper;
import cn.mq.tbed.pojo.Keys;

import java.util.List;

@Service
public class KeysServiceImpl implements KeysService {

    @Autowired
    private KeysMapper keysMapper;

    @Override
    public Keys selectKeys(Integer storageType) {
        // TODO Auto-generated method stub
        return keysMapper.selectKeys(storageType);
    }

    @Override
    public Integer updateKey(Keys key) {
        // TODO Auto-generated method stub
        return keysMapper.updateKey(key);
    }

    @Override
    public List<Keys> getKeys() {
        return null;
    }

}
