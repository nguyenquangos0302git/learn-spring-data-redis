package com.example.redis.modules.string.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

public class StringRequests {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetRequest {
        private String key;
        private Object value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetexRequest {
        private String key;
        private Object value;
        private long seconds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsetRequest {
        private Map<String, Object> data;
    }
}
