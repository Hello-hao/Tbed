package cn.mq.tbed.service;

import cn.mq.tbed.pojo.Config;
import org.springframework.stereotype.Service;

@Service
public interface ConfigService {
    Config getSourceype();
    Integer setSourceype(Config config);
}
