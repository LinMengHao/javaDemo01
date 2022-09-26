package com.example.demo01.rabbitMQ.listening;

import com.example.demo01.common.MsgType;
import com.example.demo01.common.R;
import com.example.demo01.entity.msgModel.TextMsgModel;
import com.example.demo01.entity.xmlToBean.Messages;
import com.example.demo01.entity.xmlToBean.Multimedia;
import com.example.demo01.rabbitMQ.conf.DirectRabbitConfig;
import com.example.demo01.rabbitMQ.conf.RabbitConfig;
import com.example.demo01.service.IReceiveService;
import com.example.demo01.service.ISendService;
import com.example.demo01.utils.RedisUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

//上行消息和状态通知 消息监听
@Slf4j
@Component
public class MsgCustomerListen {
    //打印数据到特定日志文件
    private static final Logger logger= LogManager.getLogger("shortMessageRollingFile");
    @Autowired
    RedisUtils redisUtils;

    @Autowired
    IReceiveService receiveService;

    @Autowired
    ISendService sendService;

    //状态通知，上行，撤回通知消息上行消费
    @RabbitListener(queues = DirectRabbitConfig.QUEUE_WORK_ACCESS,containerFactory = RabbitConfig.CONTAINER_FACTORY_ACCESS)
    public void process( Message message, Channel channel) {
        log.info("线程名称："+Thread.currentThread().getName());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        byte[] body = message.getBody();
        String id=null;
        try{
            ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(body));
            Messages msg = (Messages) ois.readObject();
            log.info("deliveryTag：",deliveryTag);
            log.info("DirectReceiver消费者收到消息  : " + msg.toString());
            log.info("消费主题来自："+message.getMessageProperties().getConsumerQueue());
            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            id = msg.getId();
            //划分场景：状态上报，上行消息，消息撤回通知
            if(msg.getDeliveryInfos()!=null&&msg.getDeliveryInfos().size()>0){
                //状态上报...
                //结合deliveryStatus 和 description判断成功与否
                R r = receiveService.notifyStatus(msg);
                if("200".equals(r.getCode())){
                    channel.basicAck(deliveryTag,false);
                }else {
                    //重发
                    fallback(id,deliveryTag,3,channel);
                }
            }else if(msg.getInboundMessage()!=null&&msg.getDeliveryInfos()==null){
                //终端主动上行消息...
                //根据contentType 判断是什么类型信息
                R r = receiveService.receiveMsg(msg);
                if("200".equals(r.getCode())){
                    channel.basicAck(deliveryTag,false);
                }else {
                    //重发
                    fallback(id,deliveryTag,3,channel);
                }
            }else if(msg.getDeliveryInfos().size()<=0&&msg.getInboundMessage()==null){
                //消息撤回...
                R r = receiveService.withdrawNotify(msg);
                if("200".equals(r.getCode())){
                    channel.basicAck(deliveryTag,false);
                }else {
                    //重发
                    fallback(id,deliveryTag,3,channel);
                }
            }
        }catch (Exception e){
            fallback(id,deliveryTag,3,channel);
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = DirectRabbitConfig.QUEUE_WORK_AUDIT,containerFactory = RabbitConfig.CONTAINER_FACTORY_ACCESS)
    public void audit(Message message, Channel channel){
        log.info("线程名称："+Thread.currentThread().getName());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        byte[] body = message.getBody();
        String id=null;
        try{
            ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(body));
            Multimedia msg = (Multimedia) ois.readObject();
            log.info("deliveryTag：",deliveryTag);
            log.info("DirectReceiver消费者收到消息  : " + msg.toString());
            log.info("消费主题来自："+message.getMessageProperties().getConsumerQueue());
            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            id = msg.getId();
            //划分场景：状态上报，上行消息，消息撤回通知
            R r = receiveService.auditFile(msg);
            if("200".equals(r.getCode())){
                channel.basicAck(deliveryTag,false);
            }else {
                //重发
                fallback(id,deliveryTag,3,channel);
            }
        }catch (Exception e){
            fallback(id,deliveryTag,3,channel);
            e.printStackTrace();
        }
    }

    //消息下行
    @RabbitListener(queues = DirectRabbitConfig.QUEUE_WORK_AUDIT,containerFactory = RabbitConfig.CONTAINER_FACTORY_ACCESS)
    public void send(Message message, Channel channel){
        log.info("线程名称："+Thread.currentThread().getName());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        byte[] body = message.getBody();
        String id=null;
        try{
            ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(body));
            TextMsgModel msg = (TextMsgModel) ois.readObject();
            log.info("deliveryTag：",deliveryTag);
            log.info("DirectReceiver消费者收到消息  : " + msg.toString());
            log.info("消费主题来自："+message.getMessageProperties().getConsumerQueue());
            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            id = msg.getId();
            //划分场景：文本，卡片，媒体...
            String type = msg.getType();
            //TODO 写法暂定，无法判断各个消息字段是否可以复用
            R r=null;
            if(StringUtils.hasText(type)){
                if(MsgType.TXT.equals(type)){
                    //纯文本消息
                    r=sendService.sendTxtMsg(msg);
                }else if(MsgType.CARD.equals(type)){
                    //卡片消息
                    r=sendService.sendCardMsg(msg);
                }else if(MsgType.FILE.equals(type)){
                    //文件消息
                    r=sendService.sendFileMsg(msg);
                }else if(MsgType.MENU.equals(type)){
                    //菜单消息
                    r=sendService.sendMenuMsg(msg);
                }else if(MsgType.CONTRIBUTION.equals(type)){
                    //交互下行
                    r=sendService.sendContributionMsg(msg);
                }else if(MsgType.UP1.equals(type)){
                    //回落消息
                    r=sendService.sendUpMsg(msg);
                }
            }else {
                //默认处理
                r = sendService.sendMsg(msg);
            }
            if("200".equals(r.getCode())){
                channel.basicAck(deliveryTag,false);
            }else {
                //重发
                fallback(id,deliveryTag,3,channel);
            }
        }catch (Exception e){
            fallback(id,deliveryTag,3,channel);
            e.printStackTrace();
        }
    }

    //拒绝重发策略
    public void fallback(String id,long deliveryTag, int count,Channel channel){
        try {
            if (!StringUtils.hasText(id)){
                channel.basicReject(deliveryTag,false);
                log.error("数据接收，转化问题，无法消费，建议不入队占有资源，人工日志排查");
            }else {
                int limit = (int)redisUtils.getCacheObject(id);
                if(limit>=count){
                    channel.basicReject(deliveryTag,false);
                    redisUtils.deleteObject(id);
                }
                redisUtils.setCacheObject(id,limit+1);
                //根据逻辑判断，是否要重返队列
                //第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝
                channel.basicReject(deliveryTag,true);
                log.info("消费该"+id+"消息失败，重回队列");
            }
        } catch (IOException e) {
            log.error("缓存出错，拒绝重新入队");
            e.printStackTrace();
        }
    }
}
