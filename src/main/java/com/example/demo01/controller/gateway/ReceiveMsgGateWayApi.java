package com.example.demo01.controller.gateway;

import com.example.demo01.common.R;
import com.example.demo01.entity.xmlToBean.Messages;
import com.example.demo01.utils.AsyncUtils;
import com.example.demo01.utils.RedisUtils;
import com.example.demo01.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;

/**
 * CSP消息回调（上行消息和状态报告）
 */
@Slf4j
@RestController
@RequestMapping("xiuzhi/bj_mobile")
public class ReceiveMsgGateWayApi {
    @Autowired
    AsyncUtils asyncUtils;
    @Autowired
    RedisUtils redisUtils;

    @PostMapping(value = "ChatBot/MsgMO",consumes = { MediaType.APPLICATION_XML_VALUE },
            produces = MediaType.APPLICATION_XML_VALUE)
    public R receiveMsgGateWayApi(@RequestBody Messages messages, HttpServletRequest request){
        log.info("线程名称"+Thread.currentThread().getName());
        long l = System.currentTimeMillis();
        String address = request.getHeader("Address");
        String uuid32 = UUIDUtil.getUUID32();
        messages.setId(uuid32);
        //记录重试，不超过3次,一个小时自动删除
        redisUtils.setCacheObject(uuid32,1, Duration.ofHours(1));
        if(StringUtils.hasText(address)){
            //设置主叫地址
            messages.setMainAddress(address);
            log.info("主叫地址： "+address);
        }
        asyncUtils.sendMessageToMQ(messages);
        long l1 = System.currentTimeMillis();
        log.info("接受到响应耗时：" +(l1-l)+"毫秒");
        return R.ok();
    }
}
