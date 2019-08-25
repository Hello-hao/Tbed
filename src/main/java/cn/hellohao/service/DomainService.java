package cn.hellohao.service;

import cn.hellohao.pojo.Domain;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/21 9:50
 */
@Service
public interface DomainService {
    Integer getDomain(String domain);
}
