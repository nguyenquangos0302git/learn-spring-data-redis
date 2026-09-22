package com.example.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class LearnSpringDataRedisApplicationTests {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(redisTemplate);
		redisTemplate.opsForValue().set("test:setup", "Hello Redis!");
		Object value = redisTemplate.opsForValue().get("test:setup");
		Assertions.assertEquals("Hello Redis!", value);
	}

}
