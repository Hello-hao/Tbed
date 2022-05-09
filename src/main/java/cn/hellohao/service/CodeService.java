package cn.hellohao.service;

import cn.hellohao.pojo.Code;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:20
 */
@Service
public interface CodeService {
    List<Code> selectCode(String value);
    Code selectCodekey(String code);
    Integer addCode(Code code);
    Integer deleteCode(String code);

}
