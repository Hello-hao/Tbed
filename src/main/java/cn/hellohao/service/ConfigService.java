package cn.hellohao.service;

import cn.hellohao.pojo.Config;
import org.springframework.stereotype.Service;

@Service
public interface ConfigService {
    Config getSourceype();
    Integer setSourceype(Config config);
}
