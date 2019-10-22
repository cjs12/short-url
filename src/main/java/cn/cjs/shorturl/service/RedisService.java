package cn.cjs.shorturl.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RedisService implements InitializingBean {

    @Resource
    private RedisTemplate myRedisTemplate;

    private ValueOperations<String, String> valueOperations;

    private HashOperations<String, String, String> hashOperations;

    @Override
    public void afterPropertiesSet() throws Exception {
        valueOperations = myRedisTemplate.opsForValue();
        hashOperations = myRedisTemplate.opsForHash();
    }

    /**
     * 自增
     * @param key
     * @return
     */
    public long increment(String key) {
       return valueOperations.increment(key);
    }

    /**
     *  冲redis中取值
      * @param key
     * @param hashKey
     * @return
     */
    public String get(String key, String hashKey) {
        return hashOperations.get(key, hashKey);
    }

    /**
     * redis中插入值
     * @param key
     * @param hashKey
     * @param hashValue
     */
    public void put(String key, String hashKey, String hashValue) {
        hashOperations.put(key, hashKey, hashValue);
    }
}
