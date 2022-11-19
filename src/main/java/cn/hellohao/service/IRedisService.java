package cn.hellohao.service;

import java.util.Map;

public interface IRedisService {

  // 加入元素
  void setValue(String key, Map<String, Object> value);
  // 加入元素
  void setValue(String key, String value);
  // 加入元素
  void setTimeValue(String key, Object value,Long time);
  // 获取元素
  Object getMapValue(String key);
  // 获取元素
  Object getValue(String key);
    // 删除某个值
  Boolean removeValue(String key);
}
