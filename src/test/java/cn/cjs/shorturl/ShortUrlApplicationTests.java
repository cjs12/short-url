package cn.cjs.shorturl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ShortUrlApplicationTests {

    @Qualifier("myStringRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void reidTest() {
        redisTemplate.opsForValue().increment("test20191018", 1);
    }
}
