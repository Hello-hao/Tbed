package cn.hellohao.service.impl;

import cn.hellohao.dao.ConfigMapper;
import cn.hellohao.pojo.Config;
import cn.hellohao.pojo.Images;
import cn.hellohao.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public Config getSourceype() {
        return configMapper.getSourceype();
    }

    @Override
    public Integer setSourceype(Config config) {
        return configMapper.setSourceype(config);
    }
}
