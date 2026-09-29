package com.example.redis.modules.hash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

public class HashRequests {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HSetRequest {
        private String key;
        private String field;
        private Object value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HMSetRequest {
        private String key;
        private Map<String, Object> data;
    }
}
