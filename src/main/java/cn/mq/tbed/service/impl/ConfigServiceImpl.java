package cn.mq.tbed.service.impl;

import cn.mq.tbed.dao.ConfigMapper;
import cn.mq.tbed.pojo.Config;
import cn.mq.tbed.service.ConfigService;
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
