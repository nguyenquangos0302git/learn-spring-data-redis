package com.example.redis.modules.set.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class SetRequests {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetAddRequest {
        private String key;
        private List<Object> members;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetRemoveRequest {
        private String key;
        private List<Object> members;
    }
}
