package cn.mq.tbed.service.impl;

import cn.mq.tbed.dao.EmailConfigMapper;
import cn.mq.tbed.pojo.EmailConfig;
import cn.mq.tbed.service.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailConfigService {
    @Autowired
    EmailConfigMapper emailConfigMapper;
    @Override
    public EmailConfig getemail() {
        return emailConfigMapper.getemail();
    }

    @Override
    public Integer updateemail(EmailConfig emailConfig) {
        return emailConfigMapper.updateemail(emailConfig);
    }
}
