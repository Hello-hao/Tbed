package cn.hellohao.service;

import cn.hellohao.pojo.Confdata;
import org.springframework.stereotype.Service;

@Service
public interface ConfdataService {
    Confdata selectConfdata(String key);
    Integer updateConfdata(Confdata confdata);
}
