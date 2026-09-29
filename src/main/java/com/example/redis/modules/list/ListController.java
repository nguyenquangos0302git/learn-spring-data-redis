package com.example.redis.modules.list;

import com.example.redis.common.ApiResponse;
import com.example.redis.modules.list.dto.ListRequests.ListPushRequest;
import com.example.redis.modules.list.dto.ListRequests.ListTrimRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redis/list")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @PostMapping("/lpush")
    public ApiResponse<Long> leftPush(@RequestBody ListPushRequest request) {
        Long length = listService.leftPush(request.getKey(), request.getValue());
        return ApiResponse.ok("Pushed to Head (left) of '" + request.getKey() + "', list size: " + length, length);
    }

    @PostMapping("/rpush")
    public ApiResponse<Long> rightPush(@RequestBody ListPushRequest request) {
        Long length = listService.rightPush(request.getKey(), request.getValue());
        return ApiResponse.ok("Pushed to Tail (right) of '" + request.getKey() + "', list size: " + length, length);
    }

    @PostMapping("/lpop")
    public ApiResponse<Object> leftPop(@RequestParam String key) {
        Object item = listService.leftPop(key);
        return ApiResponse.ok(item);
    }

    @PostMapping("/rpop")
    public ApiResponse<Object> rightPop(@RequestParam String key) {
        Object item = listService.rightPop(key);
        return ApiResponse.ok(item);
    }

    @GetMapping("/lrange")
    public ApiResponse<List<Object>> range(@RequestParam String key,
                                           @RequestParam(defaultValue = "0") long start,
                                           @RequestParam(defaultValue = "-1") long stop) {
        List<Object> items = listService.range(key, start, stop);
        return ApiResponse.ok(items);
    }

    @GetMapping("/llen")
    public ApiResponse<Long> size(@RequestParam String key) {
        Long length = listService.size(key);
        return ApiResponse.ok(length);
    }

    @PostMapping("/ltrim")
    public ApiResponse<String> trim(@RequestBody ListTrimRequest request) {
        listService.trim(request.getKey(), request.getStart(), request.getStop());
        return ApiResponse.ok("Trimmed list '" + request.getKey() + "' to range [" + request.getStart() + ", " + request.getStop() + "]", request.getKey());
    }

    @GetMapping("/lindex")
    public ApiResponse<Object> index(@RequestParam String key, @RequestParam long index) {
        Object item = listService.index(key, index);
        return ApiResponse.ok(item);
    }
}
