package cn.hellohao.service;

import cn.hellohao.pojo.UploadConfig;
import org.springframework.stereotype.Service;

@Service
public interface UploadConfigService {

    UploadConfig getUpdateConfig();

    Integer setUpdateConfig(UploadConfig updateConfig);
}
