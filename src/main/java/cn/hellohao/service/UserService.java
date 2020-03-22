package cn.hellohao.service;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    User getUsersByApiKey(String apikey);
    /**
     *
     * @param userid
     * @return 新的Apikey
     */
    String resetApikey(Integer userid);

    /**
     * apikey是否存在
     *
     * @param apikey
     * @return
     */
    Boolean isApiKeyExist(String apikey);
    /**
     * 创建一个新的Apikey
     *
     * @return
     */
    String createApikey();
    User getUsersByApikey(String apikey);

    //注册
    Integer register(User user);

    //登录
    Integer login(String email, String password,String uid);

    //获取用户信息
    User getUsers(String email);

    //插入图片
    Integer insertimg(Images img);

    //修改资料
    Integer change(User user);

    //检查用户名是否重复
    Integer checkUsername(String username);

    Integer getUserTotal();

    List<User> getuserlist(User user);

    Integer deleuser(Integer id);

    //查询用户名或者邮箱是否存在
    Integer countusername(String username);

    Integer countmail(String email);

    Integer uiduser(String uid);

    User getUsersMail(String uid);
    Integer setisok (User user);
    Integer setmemory(User user);
    User getUsersid(Integer id);
    List<User> getuserlistforgroupid(Integer groupid);
}
