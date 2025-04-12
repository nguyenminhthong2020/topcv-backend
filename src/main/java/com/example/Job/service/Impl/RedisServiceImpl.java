package com.example.Job.service.Impl;

import com.example.Job.config.RedisConfig;
import com.example.Job.service.IRedisService;
import jakarta.transaction.Transactional;
import org.eclipse.angus.mail.imap.protocol.BODY;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> void set(String key, T value, Long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);

    }

    @Override
    public <T> void set(String key, T value) {

        redisTemplate.opsForValue().set(key, value, RedisConfig.defaultTTL);
    }

    @Override
    public <T> void set(String key, T value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if(value == null) return null;

        try{
            return clazz.cast(value);
        }catch (ClassCastException e){
            throw new ClassCastException("Failed to cast value to class: " + clazz.getName() + " for key: " + key);
        }

    }

    @Override
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public Set<String> listKeys(String pattern) {
        return Set.of();
    }

    @Override
    public void addToList(String key, String value) {

    }

    @Override
    public List<String> getList(String key) {
        return List.of();
    }

    @Override
    public void setExpiration(String key, long timeout, TimeUnit unit) {

    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
