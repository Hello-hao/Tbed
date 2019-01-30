package cn.hellohao.service;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.User;

public interface UserService {
	//注册
	Integer register(User user);
	//登录
	Integer login(String email,String password);
	//获取用户信息
	User getUsers(String email);
	//插入图片
	Integer insertimg(Images img);
	//修改资料
	Integer change(User user);
	//检查用户名是否重复
	Integer checkUsername(String username);
	
	Integer getUserTotal();
}
