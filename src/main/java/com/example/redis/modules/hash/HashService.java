package com.example.redis.modules.hash;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HashService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void put(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object get(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> entries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void putAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public List<Object> multiGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    public Long delete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    public Boolean hasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    public Long size(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    public Long increment(String key, String field, long delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }
}
