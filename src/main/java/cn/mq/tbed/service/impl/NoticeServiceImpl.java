package cn.mq.tbed.service.impl;

import cn.mq.tbed.dao.NoticeMapper;
import cn.mq.tbed.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public String getNotice() {
        return noticeMapper.getNotice();
    }
}
