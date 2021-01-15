package cn.mq.tbed.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

    String getNotice();
}
