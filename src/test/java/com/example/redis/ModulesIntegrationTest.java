package com.example.redis;

import com.example.redis.modules.hash.HashService;
import com.example.redis.modules.list.ListService;
import com.example.redis.modules.set.SetService;
import com.example.redis.modules.string.StringService;
import com.example.redis.modules.zset.ZSetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class ModulesIntegrationTest {

    @Autowired
    private StringService stringService;

    @Autowired
    private HashService hashService;

    @Autowired
    private ListService listService;

    @Autowired
    private SetService setService;

    @Autowired
    private ZSetService zSetService;

    @Autowired
    private org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        redisTemplate.delete(Arrays.asList(
                "test:string:user", "test:string:counter",
                "test:hash:user:1",
                "test:list:queue",
                "test:set:tag:java", "test:set:tag:redis",
                "test:zset:leaderboard"
        ));
    }

    @Test
    void testStringModule() {
        String key = "test:string:user";
        stringService.set(key, "Nguyen Quang");
        Assertions.assertEquals("Nguyen Quang", stringService.get(key));

        String counterKey = "test:string:counter";
        stringService.set(counterKey, 10);
        Assertions.assertEquals(11L, stringService.increment(counterKey));
        Assertions.assertEquals(10L, stringService.decrement(counterKey));
    }

    @Test
    void testHashModule() {
        String key = "test:hash:user:1";
        hashService.put(key, "name", "Quang");
        hashService.put(key, "role", "Backend");

        Assertions.assertEquals("Quang", hashService.get(key, "name"));
        Assertions.assertTrue(hashService.hasKey(key, "role"));

        Map<Object, Object> entries = hashService.entries(key);
        Assertions.assertEquals(2, entries.size());

        hashService.increment(key, "points", 5);
        Assertions.assertEquals(5, Integer.parseInt(hashService.get(key, "points").toString()));
    }

    @Test
    void testListModule() {
        String key = "test:list:queue";
        listService.rightPush(key, "item1");
        listService.rightPush(key, "item2");

        Assertions.assertEquals(2L, listService.size(key));
        List<Object> items = listService.range(key, 0, -1);
        Assertions.assertEquals(2, items.size());

        Object popped = listService.leftPop(key);
        Assertions.assertEquals("item1", popped);
    }

    @Test
    void testSetModule() {
        String tagJava = "test:set:tag:java";
        String tagRedis = "test:set:tag:redis";

        setService.add(tagJava, Arrays.asList("post1", "post2", "post3"));
        setService.add(tagRedis, Arrays.asList("post2", "post3", "post4"));

        Assertions.assertTrue(setService.isMember(tagJava, "post1"));
        Assertions.assertEquals(3L, setService.size(tagJava));

        Set<Object> common = setService.intersect(Arrays.asList(tagJava, tagRedis));
        Assertions.assertTrue(common.contains("post2"));
        Assertions.assertTrue(common.contains("post3"));
        Assertions.assertFalse(common.contains("post1"));
    }

    @Test
    void testZSetModule() {
        String key = "test:zset:leaderboard";
        zSetService.add(key, "playerA", 100.0);
        zSetService.add(key, "playerB", 300.0);
        zSetService.add(key, "playerC", 200.0);

        Assertions.assertEquals(3L, zSetService.size(key));
        Assertions.assertEquals(300.0, zSetService.score(key, "playerB"));

        // Top 1 highest score should be playerB
        Set<ZSetOperations.TypedTuple<Object>> top = zSetService.reverseRangeWithScores(key, 0, 0);
        Assertions.assertFalse(top.isEmpty());
        ZSetOperations.TypedTuple<Object> first = top.iterator().next();
        Assertions.assertEquals("playerB", first.getValue());
        Assertions.assertEquals(300.0, first.getScore());
    }
}
