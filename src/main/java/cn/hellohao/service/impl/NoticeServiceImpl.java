package cn.hellohao.service.impl;

import cn.hellohao.dao.NoticeMapper;
import cn.hellohao.service.NoticeService;
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
