package cn.mq.tbed.service;

import cn.mq.tbed.pojo.SysConfig;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:46
 */
@Service
public interface SysConfigService {
    SysConfig getstate();
    Integer setstate(SysConfig sysConfig);
}
