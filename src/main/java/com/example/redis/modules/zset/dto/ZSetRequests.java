package com.example.redis.modules.zset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ZSetRequests {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZSetAddRequest {
        private String key;
        private Object member;
        private double score;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZSetRemoveRequest {
        private String key;
        private List<Object> members;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZSetIncrScoreRequest {
        private String key;
        private Object member;
        private double delta;
    }
}
