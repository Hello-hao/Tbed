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
    //查询扩容码
    List<Code> selectCode(String value);
    Code selectCodekey(String code);
    //添加
    Integer addCode(Code code);
    //删除扩容码
    Integer deleteCode(String code);

}
