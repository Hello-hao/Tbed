package cn.mq.tbed.service;

import cn.mq.tbed.pojo.UploadConfig;
import org.springframework.stereotype.Service;

@Service
public interface UploadConfigService {
    UploadConfig getUpdateConfig();
    Integer setUpdateConfig(UploadConfig updateConfig);
}
