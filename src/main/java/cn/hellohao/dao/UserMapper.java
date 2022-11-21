package cn.hellohao.dao;

import cn.hellohao.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    Integer register(User user);
    Integer login(@Param("email") String email, @Param("password") String password,@Param("uid") String uid);
    User getUsers(User user);
    User loginByToken (@Param("token") String token);
    Integer change(User user);
    Integer changeUser(User user);
    Integer checkUsername(@Param("username") String username);
    Integer getUserTotal();
    List<User> getuserlist(String username);
    Integer deleuser(@Param("id") Integer id);
    Integer countusername(@Param("username") String username);
    Integer countmail(@Param("email") String email);
    Integer uiduser(@Param("uid") String uid);
    User getUsersMail(@Param("uid") String uid);
    Integer setisok (User user);
    Integer setmemory(User user);
    User getUsersid(@Param("id") Integer id);
    List<User> getuserlistforgroupid(@Param("groupid") Integer groupid);

}
