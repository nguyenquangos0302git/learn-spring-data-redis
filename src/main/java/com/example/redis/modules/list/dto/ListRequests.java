package com.example.redis.modules.list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ListRequests {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListPushRequest {
        private String key;
        private Object value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListTrimRequest {
        private String key;
        private long start;
        private long stop;
    }
}
