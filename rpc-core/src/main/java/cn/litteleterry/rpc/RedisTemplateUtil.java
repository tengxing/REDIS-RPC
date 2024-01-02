package cn.litteleterry.rpc;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisTemplateUtil {
    private static RedisTemplate<String, String> redisTemplate = null;
    public static synchronized RedisTemplate<String, String> getRedisTemplate() {
        return (RedisTemplate<String, String>)ApplicationContextAwareHolder.getBean("stringRedisTemplate");
    }
}
