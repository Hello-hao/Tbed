package cn.hellohao.service;

import cn.hellohao.pojo.Group;
import cn.hellohao.pojo.Msg;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:29
 */
@Service
public interface GroupService {
    List<Group> grouplist(Integer usertype);
    Group idgrouplist(Integer id);
    Msg addgroup(Group group);
    Integer GetCountFroUserType(Integer usertype);
    Msg delegroup(Integer id);
    Msg setgroup(Group group);
    Group getGroupFroUserType(Integer usertype);
}
