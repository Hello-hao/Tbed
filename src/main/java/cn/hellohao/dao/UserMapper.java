package cn.hellohao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.User;

@Mapper
public interface UserMapper {
	//注册
	Integer register(User user);
	//登录
	Integer login(@Param("email") String email,@Param("password") String password);
	//获取用户信息
	User getUsers(@Param("email") String email);
	//插入图片
	Integer insertimg(Images img);
	//修改资料
	Integer change(User user);
	//检查用户名是否重复
	Integer checkUsername(@Param("username") String username);
	Integer getUserTotal();
}
