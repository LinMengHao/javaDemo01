package com.example.demo01;

import com.example.demo01.entity.xmlToBean.Messages;
import com.example.demo01.entity.xmlToBean.Multimedia;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

	@Test
	void test(){
		String s="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<file xmlns=\"urn:gsma:params:xml:ns:rcs:rcs:fthttp\" \n" +
				"xmlns:x=\"urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext\">\t\n" +
				"<file-info type=\"thumbnail\">\n" +
				"<file-size>[thumbnail size in bytes]</file-size>\n" +
				"<content-type>[MIME-type for thumbnail]</content-type>\n" +
				"<data url = \"[HTTP URL for the thumbnail]\"  until = \"[validity of the thumbnail]\"/>\n" +
				"\n" +
				"</file-info>\n" +
				"<file-info type=\"file\" file-disposition=\"[file-disposition]\">\n" +
				"<file-size>[file size in bytes]</file-size>\n" +
				"<file-name>[original file name]</file-name>\n" +
				"<content-type>[MIME-t ype for file]</content-type>\n" +
				"<data url = \"[HTTP URL for the file]\"  until = \"[validity of the file]\"/>\n" +
				"</file-info>\n" +
				"</file>";
		XmlMapper xmlMapper=new XmlMapper();
		xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			Multimedia multimedia=xmlMapper.readValue(s, Multimedia.class);
			System.out.println(multimedia.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}


}
