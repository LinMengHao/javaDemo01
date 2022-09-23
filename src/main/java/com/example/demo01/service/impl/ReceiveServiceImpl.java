package com.example.demo01.service.impl;

import com.example.demo01.common.R;
import com.example.demo01.common.ResultCode;
import com.example.demo01.entity.xmlToBean.DeliveryInfo;
import com.example.demo01.entity.xmlToBean.InboundMessage;
import com.example.demo01.entity.xmlToBean.Messages;
import com.example.demo01.service.IReceiveService;
import com.example.demo01.utils.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
@Slf4j
@Service
public class ReceiveServiceImpl implements IReceiveService {
    //打印数据到特定日志文件
    private static final Logger logger= LogManager.getLogger("shortMessageRollingFile");
    @Override
    public R notifyStatus(Messages messages) {
        List<DeliveryInfo> deliveryInfos = messages.getDeliveryInfos();
        //主叫地址
        String mainAddress = messages.getMainAddress();
        for (int i = 0; i < deliveryInfos.size(); i++) {
            DeliveryInfo deliveryInfo = deliveryInfos.get(i);
            //发送回执消息的发送方地址
            String address = deliveryInfo.getAddress();
            String status = deliveryInfo.getDeliveryStatus();
            if(ResultCode.MESSAGESENT.getCode().equals(status)){
                log.info("主叫地址： "+mainAddress+" "+"发送回执消息的发送方地址："+address+" "+ResultCode.MESSAGESENT.getMessage());
            }
            else if(ResultCode.MESSAGEDISPLAYED.getCode().equals(status)){
                log.info("主叫地址： "+mainAddress+" "+"发送回执消息的发送方地址："+address+" "+ResultCode.MESSAGEDISPLAYED.getMessage());
            }
            else if(ResultCode.DELIVERYIMPOSSIBLE.getCode().equals(status)){
                log.info("主叫地址： "+mainAddress+" "+"发送回执消息的发送方地址："+address+" "+ResultCode.DELIVERYIMPOSSIBLE.getMessage());
                if(StringUtils.hasText(deliveryInfo.getDescription())){
                    log.info("失败原因："+deliveryInfo.getDescription());
                }
                if(StringUtils.hasText(deliveryInfo.getText())){
                    log.info("备注："+deliveryInfo.getText());
                }
            }
            else if(ResultCode.DELIVEREDTONETWORK.getCode().equals(status)){
                if("SMS".equals(deliveryInfo.getDescription())){
                    log.info("主叫地址： "+mainAddress+" "+"发送回执消息的发送方地址："+address+" 已转短消息发送"+ResultCode.DELIVEREDTONETWORK.getMessage());
                }
                if("MMS".equals(deliveryInfo.getDescription())){
                    log.info("主叫地址： "+mainAddress+" "+"发送回执消息的发送方地址："+address+" 已彩信消息发送"+ResultCode.DELIVEREDTONETWORK.getMessage());
                }
            }
            else if(ResultCode.DELIVEREDTOTERMINAL.getCode().equals(status)){
                log.info("主叫地址： "+mainAddress+" "+"发送回执消息的发送方地址："+address+" "+ResultCode.DELIVEREDTOTERMINAL.getMessage()+" "+messages.getDescription());
            }

        }
        return R.ok();
    }

    @Override
    public R receiveMsg(Messages messages) {
        InboundMessage inboundMessage = messages.getInboundMessage();
        String contentType = inboundMessage.getContentType();
        if(StringUtils.hasText(contentType)){
            String[] s = contentType.replace(" ", "").split(";");
            if(s.length<2){
                s[1]="charset=UTF-8";
            }
            if("text/plain".equals(s[0])){
                //纯文本消息
                if("UTF-8".equals(s[1].split("=")[1])){
                    log.info("消息内容："+inboundMessage.getBodyText());
                }else {
                    //base64编码内容  解码
                    String decode = Base64Utils.decode(inboundMessage.getBodyText());
                }
            }else if("application/vnd.gsma.rcs-ft-http+xml".equals(s[0])){
                //媒体文件消息
            }else if("application/vnd.gsma.botsuggestion.response.v1.0+json".equals(s[0])){
                //基于建议回复/操作消息的回复
            }
        }
        return null;
    }
}
