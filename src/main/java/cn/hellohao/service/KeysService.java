package cn.hellohao.service;


import cn.hellohao.pojo.Keys;

public interface KeysService {
	//查询密钥
	Keys selectKeys(Integer storageType);
	//查询key类型
	Integer selectKeysType();
	//修改key
	Integer updateKey(Keys key);

}
