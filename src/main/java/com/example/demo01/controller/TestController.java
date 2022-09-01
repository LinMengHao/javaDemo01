package com.example.demo01.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo01.common.R;
import com.example.demo01.common.ResponseUtil;
import com.example.demo01.entity.UserModel;
import com.example.demo01.utils.RedisUtils;
import com.example.demo01.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/user")
public class TestController {
    @RequestMapping("test")
    public String test(){
        return "Holle World";
    }
    @RequestMapping("token")
    public R token(){
        return R.ok().token("lmh");
    }




    /*
        测试 发送参数和签名给移动 移动审核获取token 返回给我们
     */
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserModel userModel;
    @Autowired
    RedisUtils redisUtils;
    @RequestMapping("getToken")
    public String getToken(){
        String reqTime=String.valueOf(System.currentTimeMillis());
        String apiKey = userModel.getApiKey();
        String apiSecrect = userModel.getApiSecrect();
        String ip = userModel.getIp();
        String port = userModel.getPort();

        StringBuilder url=new StringBuilder();
        url.append("http://");
        url.append(ip);
        url.append(":");
        url.append(port);
        url.append("/api/user/token");

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("apiKey",apiKey);
        jsonObject.put("apiSecrect",apiSecrect);
        jsonObject.put("reqTime",reqTime);
        //按规则生成签名
        String sign = TokenUtils.getSign(jsonObject);
        //参数
        MultiValueMap map=new LinkedMultiValueMap();
        map.add("apiKey",userModel.getApiKey());
        map.add("apiSecrect",userModel.getApiSecrect());
        map.add("reqTime",reqTime);
        map.add("sign",sign);

        ResponseEntity<ResponseUtil> entity = restTemplate.postForEntity(url.toString(), map, ResponseUtil.class);
        System.out.println("msg: "+entity.getBody().getMessage());
        System.out.println("code: "+entity.getBody().getCode());
        System.out.println("token: "+entity.getBody().getToken());
        redisUtils.setCacheObject("token",entity.getBody().getToken(),24, TimeUnit.HOURS);
        String token = (String)redisUtils.getCacheObject("token");
        System.out.println(token);
        return entity.getBody().getToken();
    }
}
