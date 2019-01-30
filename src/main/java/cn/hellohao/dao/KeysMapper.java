package cn.hellohao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.hellohao.pojo.Keys;

@Mapper
public interface KeysMapper {
	//查询密钥
	Keys selectKeys(@Param("storageType") Integer storageType);
	//查询key类型
	Integer selectKeysType();
	//修改key
	Integer updateKey(Keys key);

}
