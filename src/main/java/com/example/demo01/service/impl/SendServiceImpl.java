package com.example.demo01.service.impl;

import com.example.demo01.common.R;
import com.example.demo01.entity.msgModel.ResponseModel;
import com.example.demo01.entity.msgModel.TextMsgModel;
import com.example.demo01.service.ISendService;
import com.example.demo01.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Service
public class SendServiceImpl implements ISendService {
    //打印数据到特定日志文件
    private static final Logger logger= LogManager.getLogger("shortMessageRollingFile");

    @Autowired
    HttpHeaderUtil httpHeaderUtil;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public R sendMsg(TextMsgModel msg) {

        return null;
    }

    @Override
    public R sendTxtMsg(TextMsgModel textMsgModel) {
        String date = DateUtil.getGMTDate();
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel, Duration.ofHours(24));
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);

        //消息内容请求体
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = restTemplate.postForEntity("http://" + textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        //xml转化bean
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @Override
    public R sendCardMsg(TextMsgModel msg) {
        return null;
    }

    @Override
    public R sendFileMsg(TextMsgModel msg) {
        return null;
    }

    @Override
    public R sendMenuMsg(TextMsgModel msg) {
        return null;
    }

    @Override
    public R sendContributionMsg(TextMsgModel msg) {
        return null;
    }

    @Override
    public R sendUpMsg(TextMsgModel msg) {
        return null;
    }
}
