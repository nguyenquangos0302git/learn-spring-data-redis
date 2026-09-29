package com.example.redis.modules.zset;

import com.example.redis.common.ApiResponse;
import com.example.redis.modules.zset.dto.ZSetRequests.ZSetAddRequest;
import com.example.redis.modules.zset.dto.ZSetRequests.ZSetIncrScoreRequest;
import com.example.redis.modules.zset.dto.ZSetRequests.ZSetRemoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/redis/zset")
@RequiredArgsConstructor
public class ZSetController {

    private final ZSetService zSetService;

    @PostMapping("/zadd")
    public ApiResponse<Boolean> add(@RequestBody ZSetAddRequest request) {
        Boolean isNew = zSetService.add(request.getKey(), request.getMember(), request.getScore());
        return ApiResponse.ok("Added/Updated member '" + request.getMember() + "' with score " + request.getScore(), isNew);
    }

    @GetMapping("/zrange")
    public ApiResponse<Set<ZSetOperations.TypedTuple<Object>>> range(
            @RequestParam String key,
            @RequestParam(defaultValue = "0") long start,
            @RequestParam(defaultValue = "-1") long stop) {
        Set<ZSetOperations.TypedTuple<Object>> results = zSetService.rangeWithScores(key, start, stop);
        return ApiResponse.ok(results);
    }

    @GetMapping("/zrevrange")
    public ApiResponse<Set<ZSetOperations.TypedTuple<Object>>> reverseRange(
            @RequestParam String key,
            @RequestParam(defaultValue = "0") long start,
            @RequestParam(defaultValue = "-1") long stop) {
        Set<ZSetOperations.TypedTuple<Object>> results = zSetService.reverseRangeWithScores(key, start, stop);
        return ApiResponse.ok(results);
    }

    @GetMapping("/zrank")
    public ApiResponse<Long> rank(@RequestParam String key, @RequestParam String member) {
        Long rank = zSetService.rank(key, member);
        return ApiResponse.ok(rank);
    }

    @GetMapping("/zrevrank")
    public ApiResponse<Long> reverseRank(@RequestParam String key, @RequestParam String member) {
        Long rank = zSetService.reverseRank(key, member);
        return ApiResponse.ok(rank);
    }

    @GetMapping("/zscore")
    public ApiResponse<Double> score(@RequestParam String key, @RequestParam String member) {
        Double score = zSetService.score(key, member);
        return ApiResponse.ok(score);
    }

    @PostMapping("/zincrby")
    public ApiResponse<Double> incrementScore(@RequestBody ZSetIncrScoreRequest request) {
        Double newScore = zSetService.incrementScore(request.getKey(), request.getMember(), request.getDelta());
        return ApiResponse.ok("New score for '" + request.getMember() + "': " + newScore, newScore);
    }

    @GetMapping("/zrangebyscore")
    public ApiResponse<Set<ZSetOperations.TypedTuple<Object>>> rangeByScore(
            @RequestParam String key,
            @RequestParam double min,
            @RequestParam double max) {
        Set<ZSetOperations.TypedTuple<Object>> results = zSetService.rangeByScoreWithScores(key, min, max);
        return ApiResponse.ok(results);
    }

    @DeleteMapping("/zrem")
    public ApiResponse<Long> remove(@RequestBody ZSetRemoveRequest request) {
        Long removedCount = zSetService.remove(request.getKey(), request.getMembers());
        return ApiResponse.ok("Removed " + removedCount + " members from zset '" + request.getKey() + "'", removedCount);
    }

    @GetMapping("/zcard")
    public ApiResponse<Long> size(@RequestParam String key) {
        Long total = zSetService.size(key);
        return ApiResponse.ok(total);
    }
}
