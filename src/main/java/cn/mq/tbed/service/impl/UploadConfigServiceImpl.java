package cn.mq.tbed.service.impl;

import cn.mq.tbed.dao.UploadConfigMapper;
import cn.mq.tbed.pojo.UploadConfig;
import cn.mq.tbed.service.UploadConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadConfigServiceImpl implements UploadConfigService {
    @Autowired
    private UploadConfigMapper uploadConfigMapper;

    @Override
    public UploadConfig getUpdateConfig() {
        return uploadConfigMapper.getUpdateConfig();
    }

    @Override
    public Integer setUpdateConfig(UploadConfig uploadConfig) {
        return uploadConfigMapper.setUpdateConfig(uploadConfig);
    }
}

