package cn.hellohao.service.impl;

import cn.hellohao.dao.AppClientMapper;
import cn.hellohao.pojo.AppClient;
import cn.hellohao.service.AppClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppClientServiceImpl implements AppClientService {

    @Autowired
    AppClientMapper appClientMapper;


    @Override
    public AppClient getAppClientData(String id) {
        return appClientMapper.getAppClientData(id);
    }

    @Override
    public Integer editAppClientData(AppClient appClient) {
        return appClientMapper.editAppClientData(appClient);
    }
}
