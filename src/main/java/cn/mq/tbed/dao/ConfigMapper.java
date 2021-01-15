package cn.mq.tbed.dao;

import cn.mq.tbed.pojo.Config;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigMapper {
    Config getSourceype();
    Integer setSourceype(Config config);
}
