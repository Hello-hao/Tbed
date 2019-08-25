package cn.hellohao.service;

import cn.hellohao.pojo.UserGroup;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 14:12
 */
@Service
public interface UserGroupService {
    UserGroup useridgetusergroup(Integer userid);
    UserGroup idgetusergroup(Integer id);
    Integer addusergroup(UserGroup userGroup);
    Integer updateusergroup(UserGroup userGroup);
    Integer updateusergroupdefault(Integer groupid);
    Integer deleusergroup(Integer userid);
}
