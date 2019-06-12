package cn.hellohao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hellohao.dao.UserMapper;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.User;
import cn.hellohao.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer register(User user) {
        // TODO Auto-generated method stub

        return userMapper.register(user);
    }

    @Override
    public Integer login(String email, String password) {
        // TODO Auto-generated method stub
        return userMapper.login(email, password);
    }

    @Override
    public User getUsers(String email) {
        // TODO Auto-generated method stub
        return userMapper.getUsers(email);
    }

    @Override
    public Integer insertimg(Images img) {
        // TODO Auto-generated method stub
        return userMapper.insertimg(img);
    }

    @Override
    public Integer change(User user) {
        // TODO Auto-generated method stub
        return userMapper.change(user);
    }

    @Override
    public Integer checkUsername(String username) {
        // TODO Auto-generated method stub
        return userMapper.checkUsername(username);
    }

    @Override
    public Integer getUserTotal() {
        // TODO Auto-generated method stub
        return userMapper.getUserTotal();
    }

    @Override
    public Integer deleuser(Integer id) {
        return userMapper.deleuser(id);
    }

    @Override
    public Integer countusername(String username) {
        return userMapper.countusername(username);
    }

    @Override
    public Integer countmail(String email) {
        return userMapper.countmail(email);
    }

    @Override
    public List<User> getuserlist() {
        return userMapper.getuserlist();
    }

    @Override
    public Integer uiduser(String uid) {
        return userMapper.uiduser(uid);
    }

    @Override
    public User getUsersMail(String uid) {
        return userMapper.getUsersMail(uid);
    }
}
