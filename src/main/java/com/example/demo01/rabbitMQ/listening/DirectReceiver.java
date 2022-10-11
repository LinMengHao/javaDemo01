//package com.example.demo01.rabbitMQ.listening;
//
//import com.example.demo01.rabbitMQ.conf.DirectRabbitConfig;
//import com.example.demo01.rabbitMQ.conf.RabbitConfig;
//import com.example.demo01.utils.RedisUtils;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.TimeoutException;
//
////测试 消息监听
//@Slf4j
//@Component
//public class DirectReceiver {
//    @Autowired
//    RedisUtils redisUtils;
//
//
//    @RabbitListener(queues = DirectRabbitConfig.QUEUE_WORK_ACCESS,containerFactory = RabbitConfig.CONTAINER_FACTORY_ACCESS)
//    public void process(Map testMessage, Message message, Channel channel) {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("线程名称："+Thread.currentThread().getName());
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        log.info("deliveryTag：",deliveryTag);
//        String messageId = (String) testMessage.get("messageId");
//        try{
//            System.out.println(1/0);
//            log.info("DirectReceiver消费者收到消息  : " + testMessage.toString());
//            log.info("消费主题来自："+message.getMessageProperties().getConsumerQueue());
//            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
//            channel.basicAck(deliveryTag,false);
//        }catch (Exception e){
//            try {
//                int message1 = (int)redisUtils.getCacheObject(messageId);
//                if(message1>=10){
//                    channel.basicReject(deliveryTag,false);
//                    redisUtils.deleteObject(messageId);
//                }
//                redisUtils.setCacheObject(messageId,message1+1);
//                //根据逻辑判断，是否要重返队列
//                //第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝
//                channel.basicReject(deliveryTag,true);
//                log.info("消费"+deliveryTag+"消息失败，重回队列");
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }
//
//}
