package com.example.demo01.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo01.common.R;
import com.example.demo01.common.ResponseUtil;
import com.example.demo01.conf.HttpsClientRequestFactory;
import com.example.demo01.entity.UserModel;
import com.example.demo01.entity.xmlToBean.Messages;
import com.example.demo01.entity.xmlToBean.Multimedia;
import com.example.demo01.rabbitMQ.conf.DirectRabbitConfig;
import com.example.demo01.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
@Slf4j
@RestController
@RequestMapping("test")
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

        String url = "http://" +
                ip +
                ":" +
                port +
                "/api/user/token";

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

        ResponseEntity<ResponseUtil> entity = restTemplate.postForEntity(url, map, ResponseUtil.class);
        System.out.println("msg: "+entity.getBody().getMessage());
        System.out.println("code: "+entity.getBody().getCode());
        System.out.println("token: "+entity.getBody().getToken());
        redisUtils.setCacheObject("token",entity.getBody().getToken(),24, TimeUnit.HOURS);
        String token = redisUtils.getCacheObject("token");
        System.out.println(token);
        return entity.getBody().getToken();
    }

    //https请求
    private static final RestTemplate httpsTemplate=new RestTemplate(new HttpsClientRequestFactory());
    @RequestMapping("testHttps")
    public String testHttps() throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        ResponseEntity<String> response = httpsTemplate.exchange("https://www.baidu.com",
                HttpMethod.POST, null, String.class);
        return new String(response.getBody().getBytes(StandardCharsets.UTF_8));
    }

//    测试rabbitmq
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("accessDirect")
    public String accessDirect(){
        String messageData="Hello world";
        String time= DateUtil.getGMTDate();
        Map<String,Object>map=new HashMap<>();

        map.put("messageData",messageData);
        map.put("time",time);
        for (int i = 0; i < 1; i++) {
            redisUtils.setCacheObject("Thread"+i,1);
            map.put("messageId","Thread"+i);
            rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_WORK_ACCESS,DirectRabbitConfig.ROUTING_WORK_ACCESS,map);
        }
        return "ok";
    }

    @Autowired
    AsyncUtils asyncUtils;
    @RequestMapping("accessDirect1")
    public String accessDirect1(HttpServletRequest request) throws IOException {
        long l = System.currentTimeMillis();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,String>map=new HashMap<>();
        while  (headerNames.hasMoreElements())              //读取请求消息头
        {
            String name = headerNames.nextElement();
            String str=name +  ":"  + request.getHeader(name);
            System.out.println(str);
            map.put(name,request.getHeader(name));
        }
        BufferedReader reader=null;
        try {
            reader = request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            map.put("body",s.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long l1 = System.currentTimeMillis();
        System.out.println("耗时：" +(l1-l)+"毫秒");
        return "ok";
    }
    //消息推送到server，但是在server里找不到交换机
    @GetMapping("/TestMessageAck")
    public String TestMessageAck() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: non-existent-exchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", map);
        return "ok";
    }
    //消息推送到server，找到交换机了，但是没找到队列
    @GetMapping("/TestMessageAck2")
    public String TestMessageAck2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: lonelyDirectExchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("lonelyDirectExchange", "AccessDirectRouting", map);
        return "ok";
    }

    @PostMapping (value = "xml", consumes = { MediaType.APPLICATION_XML_VALUE },
            produces = MediaType.APPLICATION_XML_VALUE)
    public String test(@RequestBody Messages messages, HttpServletRequest request){
        log.info(request.getHeader("Address"));
        messages.setMainAddress(request.getHeader("Address"));
        log.info("请求xml内容： ", messages);
        log.info("主叫地址"+ messages.getMainAddress());
        if(null== messages.getMainAddress()){
            log.info("状态报告");
        }
        return "ok";
    }

    @PostMapping (value = "multimedia", consumes = { MediaType.APPLICATION_XML_VALUE },
            produces = MediaType.APPLICATION_XML_VALUE)
    public String file(@RequestBody Multimedia multimedia, HttpServletRequest request){
        log.info(request.getHeader("Address"));
        log.info("请求xml内容： ",multimedia.toString());
        return "ok";
    }

    public Map requestToMap(HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,String>map=new HashMap<>();
        while  (headerNames.hasMoreElements())              //读取请求消息头
        {
            String name = headerNames.nextElement();
            String str=name +  ":"  + request.getHeader(name);
            System.out.println(str);
            map.put(name,request.getHeader(name));
        }
        BufferedReader reader=null;
        try {
            reader = request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            map.put("body",s.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

}
