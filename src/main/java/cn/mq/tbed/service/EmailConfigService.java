package cn.mq.tbed.service;

import cn.mq.tbed.pojo.EmailConfig;
import org.springframework.stereotype.Service;

@Service
public interface EmailConfigService {
    EmailConfig getemail();
    Integer updateemail(EmailConfig emailConfig);
}
