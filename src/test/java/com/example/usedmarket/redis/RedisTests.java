package com.example.usedmarket.redis;

import com.example.usedmarket.web.config.auth.RedisSessionConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = RedisSessionConfig.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisConnectionTest() {
        String key = "a";
        String data = "1";

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, data);

        String result = valueOperations.get(key);
        assertEquals(data, result);
    }

    @Test
    public void redisSessionTest() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
     }
}