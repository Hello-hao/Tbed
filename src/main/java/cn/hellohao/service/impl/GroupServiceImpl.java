package cn.hellohao.service.impl;

import cn.hellohao.dao.GroupMapper;
import cn.hellohao.dao.UserMapper;
import cn.hellohao.exception.CodeException;
import cn.hellohao.pojo.Group;
import cn.hellohao.pojo.Msg;
import cn.hellohao.pojo.User;
import cn.hellohao.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:30
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Group> grouplist(Integer usertype) {
        return groupMapper.grouplist(usertype);
    }

    @Override
    public Group idgrouplist(Integer id) {
        return groupMapper.idgrouplist(id);
    }

    @Override
    public Msg addgroup(Group group) {
        final Msg msg = new Msg();
        if(group.getUsertype()!=0){
            Integer count = groupMapper.GetCountFroUserType(group.getUsertype());
            if(count==0){
                groupMapper.addgroup(group);
                msg.setInfo("添加成功");
            }else{
                msg.setCode("110401");
                msg.setInfo("分配的该用户组已存在。请勿重复分配。");
            }
        }else{
            groupMapper.addgroup(group);
            msg.setInfo("添加成功");
        }
        return msg;
    }

    @Override
    public Integer GetCountFroUserType(Integer usertype) {
        return groupMapper.GetCountFroUserType(usertype);
    }

    @Override
    @Transactional//默认遇到throw new RuntimeException(“…”);会回滚
    public Msg delegroup(Integer id) {
        Msg msg = new Msg();
        Integer ret = 0;
        ret = groupMapper.delegroup(id);
        if(ret>0){
            List<User> userList = userMapper.getuserlistforgroupid(id);
            for (User user : userList) {
                User u = new User();
                u.setGroupid(1);
                u.setUid(user.getUid());
                userMapper.change(u);
            }
            msg.setInfo("删除成功");
        }else{
            msg.setCode("500");
            msg.setInfo("删除成功");
            throw new CodeException("用户之没有设置成功。");
        }
        return msg;
    }

    @Override
    public Msg setgroup(Group group) {
        Msg msg = new Msg();
        if(group.getUsertype()!=0){
            Group groupFroUserType = groupMapper.getGroupFroUserType(group.getUsertype());
            if(groupFroUserType!=null){
                if(groupFroUserType.getUsertype().equals(group.getUsertype())){
                    if(groupFroUserType.getId().equals(group.getId())){
                        groupMapper.setgroup(group);
                        msg.setInfo("修改成功");
                    }else{
                        msg.setCode("110401");
                        msg.setInfo("分配的该用户组已存在。请勿重复分配。");
                    }
                }else{
                    if(groupMapper.GetCountFroUserType(group.getUsertype())>0){
                        msg.setCode("110401");
                        msg.setInfo("分配的该用户组已存在。请勿重复分配。");
                    }else{
                        groupMapper.setgroup(group);
                        msg.setInfo("修改成功");
                    }
                }
            }else{
                groupMapper.setgroup(group);
                msg.setInfo("修改成功");
            }
        }else{
            groupMapper.setgroup(group);
            msg.setInfo("修改成功");
        }
        return msg;
    }

    @Override
    public Group getGroupFroUserType(Integer usertype) {
        return groupMapper.getGroupFroUserType(usertype);
    }

}
