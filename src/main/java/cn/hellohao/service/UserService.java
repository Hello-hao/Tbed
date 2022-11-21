package cn.hellohao.service;

import cn.hellohao.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Integer register(User user);

    Integer login(String email, String password,String uid);

    User loginByToken (String token);

    User getUsers(User user);

    Integer change(User user);

    Integer changeUser(User user);

    Integer checkUsername(String username);

    Integer getUserTotal();

    List<User> getuserlist(String username);

    Integer deleuser(Integer id);

    Integer countusername(String username);

    Integer countmail(String email);

    Integer uiduser(String uid);

    User getUsersMail(String uid);

    Integer setisok (User user);

    Integer setmemory(User user);

    User getUsersid(Integer id);

    List<User> getuserlistforgroupid(Integer groupid);
}
