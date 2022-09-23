package com.example.demo01.utils;

import com.example.demo01.entity.xmlToBean.Messages;
import com.example.demo01.rabbitMQ.conf.DirectRabbitConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

//异步请求管理工具
@Component
public class AsyncUtils {
    //打印数据到特定日志文件
    private static final Logger logger=LogManager.getLogger("shortMessageRollingFile");
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Async("asyncPoolTaskExecutor")
    public void sendMessageToMQ(Messages messages) {
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_WORK_ACCESS,DirectRabbitConfig.ROUTING_WORK_ACCESS, messages);
    }


}
