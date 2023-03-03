package cn.hellohao.service.impl;

import cn.hellohao.dao.ConfdataMapper;
import cn.hellohao.pojo.Confdata;
import cn.hellohao.service.ConfdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfdataServiceImpl implements ConfdataService {
    @Autowired
    private ConfdataMapper confdataMapper;

    @Override
    public Confdata selectConfdata(String key) {
        return confdataMapper.selectConfdata(key);
    }

    @Override
    public Integer updateConfdata(Confdata confdata) {
        return confdataMapper.updateConfdata(confdata);
    }
}
