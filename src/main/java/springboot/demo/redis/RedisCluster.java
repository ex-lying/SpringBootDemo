package springboot.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tool.util.ExceptionUtil;
import tool.util.LogUtil;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCluster {
    private static final LogUtil log = new LogUtil(RedisCluster.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public String getStr(String key) {
        Object result = null;

        try {
            result = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn(String.format("redis get value api error, key:%s, error:%s",
                    key,
                    ExceptionUtil.StackTracetoString(e)));
        }

        return result == null ? null : result.toString();
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setExpireSeconds(String key, long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public void addSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

}
