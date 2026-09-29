package com.example.redis.modules.set;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SetService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long add(String key, List<Object> members) {
        return redisTemplate.opsForSet().add(key, members.toArray());
    }

    public Long remove(String key, List<Object> members) {
        return redisTemplate.opsForSet().remove(key, members.toArray());
    }

    public Boolean isMember(String key, Object member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Set<Object> intersect(Collection<String> keys) {
        return redisTemplate.opsForSet().intersect(keys);
    }

    public Set<Object> union(Collection<String> keys) {
        return redisTemplate.opsForSet().union(keys);
    }

    public Set<Object> difference(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }
}
