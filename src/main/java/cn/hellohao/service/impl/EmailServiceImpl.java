package cn.hellohao.service.impl;

import cn.hellohao.dao.EmailConfigMapper;
import cn.hellohao.pojo.EmailConfig;
import cn.hellohao.service.EmailConfigService;
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
