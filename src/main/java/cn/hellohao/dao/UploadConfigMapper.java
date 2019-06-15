package cn.hellohao.dao;

import cn.hellohao.pojo.UploadConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadConfigMapper {
    UploadConfig getUpdateConfig();
    Integer setUpdateConfig(UploadConfig uploadConfig);
}
