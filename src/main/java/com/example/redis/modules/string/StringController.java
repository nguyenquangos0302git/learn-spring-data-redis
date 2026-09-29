package com.example.redis.modules.string;

import com.example.redis.common.ApiResponse;
import com.example.redis.modules.string.dto.StringRequests.MsetRequest;
import com.example.redis.modules.string.dto.StringRequests.SetRequest;
import com.example.redis.modules.string.dto.StringRequests.SetexRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redis/string")
@RequiredArgsConstructor
public class StringController {

    private final StringService stringService;

    @PostMapping("/set")
    public ApiResponse<String> set(@RequestBody SetRequest request) {
        stringService.set(request.getKey(), request.getValue());
        return ApiResponse.ok("Key '" + request.getKey() + "' has been set successfully.", request.getKey());
    }

    @GetMapping("/get")
    public ApiResponse<Object> get(@RequestParam String key) {
        Object value = stringService.get(key);
        return ApiResponse.ok(value);
    }

    @PostMapping("/setex")
    public ApiResponse<String> setEx(@RequestBody SetexRequest request) {
        stringService.setEx(request.getKey(), request.getValue(), request.getSeconds());
        return ApiResponse.ok("Key '" + request.getKey() + "' set with TTL: " + request.getSeconds() + "s", request.getKey());
    }

    @PostMapping("/incr")
    public ApiResponse<Long> increment(@RequestParam String key) {
        Long result = stringService.increment(key);
        return ApiResponse.ok("Incremented key '" + key + "'", result);
    }

    @PostMapping("/decr")
    public ApiResponse<Long> decrement(@RequestParam String key) {
        Long result = stringService.decrement(key);
        return ApiResponse.ok("Decremented key '" + key + "'", result);
    }

    @PostMapping("/mset")
    public ApiResponse<String> multiSet(@RequestBody MsetRequest request) {
        stringService.multiSet(request.getData());
        return ApiResponse.ok("Set " + request.getData().size() + " keys successfully.", "OK");
    }

    @GetMapping("/mget")
    public ApiResponse<List<Object>> multiGet(@RequestParam List<String> keys) {
        List<Object> values = stringService.multiGet(keys);
        return ApiResponse.ok(values);
    }

    @PostMapping("/append")
    public ApiResponse<Integer> append(@RequestParam String key, @RequestParam String value) {
        Integer newLength = stringService.append(key, value);
        return ApiResponse.ok("Appended to key '" + key + "', new length: " + newLength, newLength);
    }

    @GetMapping("/strlen")
    public ApiResponse<Long> strlen(@RequestParam String key) {
        Long length = stringService.strlen(key);
        return ApiResponse.ok(length);
    }
}
