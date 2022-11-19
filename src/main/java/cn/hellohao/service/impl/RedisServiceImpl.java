package cn.hellohao.service.impl;

import cn.hellohao.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {
  @Autowired private StringRedisTemplate stringRedisTemplate;
  @Autowired private RedisTemplate redisTemplate;

  @Override
  public void setValue(String key, Map<String, Object> value) {
    ValueOperations<String, Object> vo = redisTemplate.opsForValue();
    vo.set(key, value);
    redisTemplate.expire(key, 1, TimeUnit.HOURS); // 这里指的是1小时后失效
  }

  @Override
  public Object getValue(String key) {
    ValueOperations<String, String> vo = redisTemplate.opsForValue();
    return vo.get(key);
  }

  @Override
  public void setValue(String key, String value) {
    ValueOperations<String, Object> vo = redisTemplate.opsForValue();
    vo.set(key, value);
    redisTemplate.expire(key, 3, TimeUnit.MINUTES); // 这里指的是1小时后失效 时HOURS  分 MINUTES
  }

  @Override
  public void setTimeValue(String key, Object value, Long time) {
    ValueOperations<String, Object> vo = redisTemplate.opsForValue();
    vo.set(key, value);
    redisTemplate.expire(key, time, TimeUnit.HOURS);
  }

  @Override
  public Object getMapValue(String key) {
    ValueOperations<String, String> vo = redisTemplate.opsForValue();
    return vo.get(key);
  }

  @Override
  public Boolean removeValue(String key) {
    return redisTemplate.delete(key);
  }
}
