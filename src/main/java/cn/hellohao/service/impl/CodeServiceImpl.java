package cn.hellohao.service.impl;

import cn.hellohao.dao.CodeMapper;
import cn.hellohao.pojo.Code;
import cn.hellohao.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:21
 */
@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeMapper codeMapper;


    @Override
    public List<Code> selectCode(String value) {
        return codeMapper.selectCode(value);
    }

    @Override
    public Code selectCodekey(String code) {
        return codeMapper.selectCodekey(code);
    }

    @Override
    public Integer addCode(Code code) {
        return codeMapper.addCode(code);
    }

    @Override
    public Integer deleteCode(String code) {
        return codeMapper.deleteCode(code);
    }
}
