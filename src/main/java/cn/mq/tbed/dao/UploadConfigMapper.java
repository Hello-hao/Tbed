package cn.mq.tbed.dao;

import cn.mq.tbed.pojo.UploadConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadConfigMapper {
    UploadConfig getUpdateConfig();
    Integer setUpdateConfig(UploadConfig uploadConfig);
}
