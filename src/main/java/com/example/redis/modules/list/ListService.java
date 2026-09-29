package com.example.redis.modules.list;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> range(String key, long start, long stop) {
        return redisTemplate.opsForList().range(key, start, stop);
    }

    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public void trim(String key, long start, long stop) {
        redisTemplate.opsForList().trim(key, start, stop);
    }

    public Object index(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }
}
