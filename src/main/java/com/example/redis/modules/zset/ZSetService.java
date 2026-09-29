package com.example.redis.modules.zset;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ZSetService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Boolean add(String key, Object member, double score) {
        return redisTemplate.opsForZSet().add(key, member, score);
    }

    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long stop) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, stop);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long stop) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, stop);
    }

    public Long rank(String key, Object member) {
        return redisTemplate.opsForZSet().rank(key, member);
    }

    public Long reverseRank(String key, Object member) {
        return redisTemplate.opsForZSet().reverseRank(key, member);
    }

    public Double score(String key, Object member) {
        return redisTemplate.opsForZSet().score(key, member);
    }

    public Double incrementScore(String key, Object member, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, member, delta);
    }

    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    public Long remove(String key, List<Object> members) {
        return redisTemplate.opsForZSet().remove(key, members.toArray());
    }

    public Long size(String key) {
        return redisTemplate.opsForZSet().size(key);
    }
}
