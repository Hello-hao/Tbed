package cn.mq.tbed.dao;

import cn.mq.tbed.pojo.EmailConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailConfigMapper {
    EmailConfig getemail();
    Integer updateemail(EmailConfig emailConfig);
}
