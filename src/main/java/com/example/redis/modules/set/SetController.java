package com.example.redis.modules.set;

import com.example.redis.common.ApiResponse;
import com.example.redis.modules.set.dto.SetRequests.SetAddRequest;
import com.example.redis.modules.set.dto.SetRequests.SetRemoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/redis/set")
@RequiredArgsConstructor
public class SetController {

    private final SetService setService;

    @PostMapping("/sadd")
    public ApiResponse<Long> add(@RequestBody SetAddRequest request) {
        Long addedCount = setService.add(request.getKey(), request.getMembers());
        return ApiResponse.ok("Added " + addedCount + " members to set '" + request.getKey() + "'", addedCount);
    }

    @DeleteMapping("/srem")
    public ApiResponse<Long> remove(@RequestBody SetRemoveRequest request) {
        Long removedCount = setService.remove(request.getKey(), request.getMembers());
        return ApiResponse.ok("Removed " + removedCount + " members from set '" + request.getKey() + "'", removedCount);
    }

    @GetMapping("/sismember")
    public ApiResponse<Boolean> isMember(@RequestParam String key, @RequestParam String member) {
        Boolean exists = setService.isMember(key, member);
        return ApiResponse.ok(exists);
    }

    @GetMapping("/smembers")
    public ApiResponse<Set<Object>> members(@RequestParam String key) {
        Set<Object> allMembers = setService.members(key);
        return ApiResponse.ok(allMembers);
    }

    @GetMapping("/scard")
    public ApiResponse<Long> size(@RequestParam String key) {
        Long total = setService.size(key);
        return ApiResponse.ok(total);
    }

    @GetMapping("/sinter")
    public ApiResponse<Set<Object>> intersect(@RequestParam List<String> keys) {
        Set<Object> common = setService.intersect(keys);
        return ApiResponse.ok(common);
    }

    @GetMapping("/sunion")
    public ApiResponse<Set<Object>> union(@RequestParam List<String> keys) {
        Set<Object> combined = setService.union(keys);
        return ApiResponse.ok(combined);
    }

    @GetMapping("/sdiff")
    public ApiResponse<Set<Object>> difference(@RequestParam String key, @RequestParam String otherKey) {
        Set<Object> diff = setService.difference(key, Collections.singletonList(otherKey));
        return ApiResponse.ok(diff);
    }
}
