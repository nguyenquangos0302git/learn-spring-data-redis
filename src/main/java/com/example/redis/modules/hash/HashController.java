package com.example.redis.modules.hash;

import com.example.redis.common.ApiResponse;
import com.example.redis.modules.hash.dto.HashRequests.HMSetRequest;
import com.example.redis.modules.hash.dto.HashRequests.HSetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/redis/hash")
@RequiredArgsConstructor
public class HashController {

    private final HashService hashService;

    @PostMapping("/hset")
    public ApiResponse<String> put(@RequestBody HSetRequest request) {
        hashService.put(request.getKey(), request.getField(), request.getValue());
        return ApiResponse.ok("Set field '" + request.getField() + "' in hash key '" + request.getKey() + "' successfully.", request.getKey());
    }

    @GetMapping("/hget")
    public ApiResponse<Object> get(@RequestParam String key, @RequestParam String field) {
        Object value = hashService.get(key, field);
        return ApiResponse.ok(value);
    }

    @GetMapping("/hgetall")
    public ApiResponse<Map<Object, Object>> entries(@RequestParam String key) {
        Map<Object, Object> all = hashService.entries(key);
        return ApiResponse.ok(all);
    }

    @PostMapping("/hmset")
    public ApiResponse<String> putAll(@RequestBody HMSetRequest request) {
        hashService.putAll(request.getKey(), request.getData());
        return ApiResponse.ok("Saved " + request.getData().size() + " fields to hash '" + request.getKey() + "'", request.getKey());
    }

    @GetMapping("/hmget")
    public ApiResponse<List<Object>> multiGet(@RequestParam String key, @RequestParam List<String> fields) {
        List<Object> values = hashService.multiGet(key, new ArrayList<>(fields));
        return ApiResponse.ok(values);
    }

    @DeleteMapping("/hdel")
    public ApiResponse<Long> delete(@RequestParam String key, @RequestParam List<String> fields) {
        Long removedCount = hashService.delete(key, fields.toArray());
        return ApiResponse.ok("Removed " + removedCount + " fields from key '" + key + "'", removedCount);
    }

    @GetMapping("/hexists")
    public ApiResponse<Boolean> hasKey(@RequestParam String key, @RequestParam String field) {
        Boolean exists = hashService.hasKey(key, field);
        return ApiResponse.ok(exists);
    }

    @GetMapping("/hlen")
    public ApiResponse<Long> size(@RequestParam String key) {
        Long totalFields = hashService.size(key);
        return ApiResponse.ok(totalFields);
    }

    @PostMapping("/hincrby")
    public ApiResponse<Long> increment(@RequestParam String key, @RequestParam String field, @RequestParam(defaultValue = "1") long delta) {
        Long result = hashService.increment(key, field, delta);
        return ApiResponse.ok("Incremented field '" + field + "' by " + delta, result);
    }
}
