package com.example.demo01.rabbitMQ.conf;


import com.example.demo01.utils.Sha256Utils;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class RabbitConfig {
    @Autowired
    @Qualifier("TaskExecutor")
    TaskExecutor taskExecutor;
    public static final String CONTAINER_FACTORY_ACCESS="AccessContainerFactory";
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("确认消息到服务端时间："+System.currentTimeMillis());
                System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
                System.out.println("ConfirmCallback:     "+"确认情况："+ack);
                System.out.println("ConfirmCallback:     "+"原因："+cause);
            }
        });

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback(){
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                System.out.println("ReturnCallback:     "+"消息："+returned.getMessage());
                System.out.println("ReturnCallback:     "+"回应码："+returned.getReplyCode());
                System.out.println("ReturnCallback:     "+"回应信息："+returned.getReplyText());
                System.out.println("ReturnCallback:     "+"交换机："+returned.getExchange());
                System.out.println("ReturnCallback:     "+"路由键："+returned.getRoutingKey());
            }
        });

        return rabbitTemplate;
    }

    @Bean(CONTAINER_FACTORY_ACCESS)
    public SimpleRabbitListenerContainerFactory containerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory
    ){
        SimpleRabbitListenerContainerFactory container = new SimpleRabbitListenerContainerFactory();
        //多消费者处理统一队列的消息（多线程监听）
        container.setConcurrentConsumers(50);
//        container.setConcurrentConsumers(1);
        //最大多线程监听数量
        container.setMaxConcurrentConsumers(50);
//        container.setMaxConcurrentConsumers(1);
        configurer.configure(container,connectionFactory);
        //限流 单位时间内消费多少条记录
        container.setPrefetchCount(50);
        //使用自定义线程池来启动消费者。
//        container.setTaskExecutor(taskExecutor);
        //是否重返队列
        container.setDefaultRequeueRejected(true);
        //手动确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageConverter(new Jackson2JsonMessageConverter());
        return container;
    }

}
