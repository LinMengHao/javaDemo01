package com.example.demo01;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class Demo01ApplicationTests {

	@Autowired
	RedisTemplate redisTemplate;
	@Test
	void contextLoads() {
		redisTemplate.opsForValue().set("name", "张三");
		redisTemplate.opsForValue().set("k3","v3");
//		System.out.println(redisTemplate.opsForValue().get("k3"));
//		redisTemplate.expire("k3", 60, TimeUnit.SECONDS);
//		System.out.println(redisTemplate.getExpire("k3"));
		//获取所有的key
		Set<String> keys = redisTemplate.keys("****");
		System.out.println(keys);
	}

}
