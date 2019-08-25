package cn.hellohao.service.impl;

import cn.hellohao.dao.UserGroupMapper;
import cn.hellohao.pojo.UserGroup;
import cn.hellohao.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/20 14:13
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {
    @Autowired
    private UserGroupMapper userGroupMapper;

    @Override
    public UserGroup useridgetusergroup(Integer userid) {
        return userGroupMapper.useridgetusergroup(userid);
    }

    @Override
    public UserGroup idgetusergroup(Integer id) {
        return userGroupMapper.idgetusergroup(id);
    }

    @Override
    public Integer addusergroup(UserGroup userGroup) {
        return userGroupMapper.addusergroup(userGroup);
    }

    @Override
    public Integer updateusergroup(UserGroup userGroup) {
        return userGroupMapper.updateusergroup(userGroup);
    }

    @Override
    public Integer updateusergroupdefault(Integer groupid) {
        return userGroupMapper.updateusergroupdefault(groupid);
    }

    @Override
    public Integer deleusergroup(Integer userid) {
        return userGroupMapper.deleusergroup(userid);
    }
}
