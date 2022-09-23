package com.example.demo01.service.impl;

import com.example.demo01.common.R;
import com.example.demo01.common.ResultCode;
import com.example.demo01.entity.xmlToBean.DeliveryInfo;
import com.example.demo01.entity.xmlToBean.Messages;
import com.example.demo01.service.IReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
}
