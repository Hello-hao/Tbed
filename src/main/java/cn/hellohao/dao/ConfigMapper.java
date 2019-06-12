package cn.hellohao.dao;

import cn.hellohao.pojo.Config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Mapper
public interface ConfigMapper {
    Config getSourceype();
    Integer setSourceype(Config config);
}
