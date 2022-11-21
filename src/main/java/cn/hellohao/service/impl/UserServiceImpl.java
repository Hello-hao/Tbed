package cn.hellohao.service.impl;

import cn.hellohao.dao.CodeMapper;
import cn.hellohao.dao.UserMapper;
import cn.hellohao.exception.CodeException;
import cn.hellohao.pojo.User;
import cn.hellohao.service.UserService;
import cn.hellohao.utils.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CodeMapper codeMapper;
    @Override
    public Integer register(User user) {
        // TODO Auto-generated method stub
        return userMapper.register(user);
    }

    @Override
    public Integer login(String email, String password,String uid) {
        // TODO Auto-generated method stub
        return userMapper.login(email, password,uid);
    }

    @Override
    public User loginByToken(String token) {
        return userMapper.loginByToken(token);
    }

    @Override
    public User getUsers(User user) {
        // TODO Auto-generated method stub
        return userMapper.getUsers(user);
    }


    @Override
    public Integer change(User user) {
        // TODO Auto-generated method stub
        return userMapper.change(user);
    }


    @Override
    public Integer changeUser(User user) {
        return userMapper.changeUser(user);
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
    public List<User> getuserlist(String username) {
        return userMapper.getuserlist(username);
    }

    @Override
    public Integer uiduser(String uid) {
        return userMapper.uiduser(uid);
    }

    @Override
    public User getUsersMail(String uid) {
        return userMapper.getUsersMail(uid);
    }

    @Override
    public Integer setisok(User user) {
        return userMapper.setisok(user);
    }

    @Override
    public Integer setmemory(User user) {
        return userMapper.setmemory(user);
    }

    @Override
    public User getUsersid(Integer id) {
        return userMapper.getUsersid(id);
    }

    @Override
    public List<User> getuserlistforgroupid(Integer groupid) {
        return userMapper.getuserlistforgroupid(groupid);
    }

//    @Transactional//默认遇到throw new RuntimeException(“…”);会回滚
//    public Integer usersetmemory(User user,String codestring) {
//        Integer ret = userMapper.setmemory(user);
//        if(ret<=0){
//            Print.warning("用户空间没有设置成功。回滚");
//            throw new CodeException("用户之没有设置成功。");
//        }else{
//            ret = codeMapper.deleteCode(codestring);
//        }
//        return ret;
//    }

    @Transactional//默认遇到throw new RuntimeException(“…”);会回滚
    public Integer usersetmemory(User user, String codestring) {
        Integer ret = userMapper.changeUser(user);
        if(ret<=0){
            Print.warning("用户空间没有设置成功。回滚");
            throw new CodeException("用户没有设置成功。");
        }else{
            ret = codeMapper.deleteCode(codestring);
        }
        return ret;
    }


}
