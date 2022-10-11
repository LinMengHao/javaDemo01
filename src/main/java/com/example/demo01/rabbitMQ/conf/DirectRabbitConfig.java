package com.example.demo01.rabbitMQ.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//直连交换机类型配置
@Configuration
public class DirectRabbitConfig {
    //上行共用一个交换机，下行用一个交换机
    //状态通知
    public static final String QUEUE_WORK_ACCESS="AccessDirectQueue";
    public static final String EXCHANGE_WORK_ACCESS="AccessDirectExchange";
    public static final String ROUTING_WORK_ACCESS="AccessDirectRouting";

    //视频审核
    public static final String QUEUE_WORK_AUDIT="AuditDirectQueue";
    public static final String ROUTING_WORK_AUDIT="AuditDirectRouting";

    //上行消息
    public static final String QUEUE_WORK_NOTIFY="NotifyDirectQueue";
    public static final String ROUTING_WORK_NOTIFY="NotifyDirectRouting";

    //上行
    public static final String QUEUE_WORK_SEND="SendDirectQueue";
    public static final String EXCHANGE_WORK_SEND="SendDirectExchange";
    public static final String ROUTING_WORK_SEND="SendDirectRouting";


    //队列 起名：AccessDirectQueue(对接移动的接入层)
    @Bean
    public Queue AccessDirectQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(QUEUE_WORK_ACCESS,true);
    }

    //Direct交换机 起名：AccessDirectExchange
    @Bean
    DirectExchange AccessDirectExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange(EXCHANGE_WORK_ACCESS,true,false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：AccessDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(AccessDirectQueue()).to(AccessDirectExchange()).with(ROUTING_WORK_ACCESS);
    }

    @Bean
    Queue AuditDirectQueue(){
        return new Queue(QUEUE_WORK_AUDIT,true);
    }
    @Bean
    Binding bindingAuditDirect(){
        return BindingBuilder.bind(AuditDirectQueue()).to(AccessDirectExchange()).with(ROUTING_WORK_AUDIT);
    }

    @Bean
    Queue NotifyDirectQueue(){
        return new Queue(QUEUE_WORK_NOTIFY,true);
    }
    @Bean
    Binding bindingNotifyDirect(){
        return BindingBuilder.bind(NotifyDirectQueue()).to(AccessDirectExchange()).with(ROUTING_WORK_NOTIFY);
    }

    @Bean
    Queue sendDirectQueue(){
        return new Queue(QUEUE_WORK_SEND,true);
    }

    @Bean
    DirectExchange sendDirectExchange(){
        return new DirectExchange(EXCHANGE_WORK_SEND,true,false);
    }

    @Bean
    Binding bindingSendDirect(){
        return BindingBuilder.bind(sendDirectQueue()).to(sendDirectExchange()).with(ROUTING_WORK_SEND);
    }


    //test
    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }
}
