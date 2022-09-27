package com.example.demo01.service.impl;

import com.example.demo01.common.R;
import com.example.demo01.entity.FileInfo;
import com.example.demo01.entity.msgModel.ResponseModel;
import com.example.demo01.entity.msgModel.TextMsgModel;
import com.example.demo01.service.ISendService;
import com.example.demo01.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;
@Slf4j
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
        //TODO 了解字段后，写通用方法
        return R.ok();
    }

    @Override
    public R sendTxtMsg(TextMsgModel textMsgModel) {
        long l = System.currentTimeMillis();
        log.info("消费开始时间："+l);
        log.info("消费者线程： "+Thread.currentThread().getName());
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
//        ResponseEntity<String> responseModelResponseEntity = restTemplate.postForEntity("http://" + textMsgModel.getServerRoot()
//                + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        //xml转化bean
//        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        ResponseModel responseModel = XmlToBean.xmlToResponseModel("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">\n" +
                "<messageId>5eae954c-42ca-4181-9ab4-9c0ef2e2ac66</messageId>\n" +
                "    <clientCorrelator>567895</clientCorrelator>\n" +
                "</msg:outboundMessageRequest>");
        System.out.println(responseModel.toString());
        long l1 = System.currentTimeMillis();
        log.info("消费结束时间："+l1);
        log.info("消费时间: "+(l1-l));
        return R.ok();
    }

    @Override
    public R sendCardMsg(TextMsgModel textMsgModel) {
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel, Duration.ofHours(24));
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = restTemplate.postForEntity("http://" +
                textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/"
                + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        //xml转化bean
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @Override
    public R sendFileMsg(TextMsgModel textMsgModel) {
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        List<FileInfo> fileInfos = textMsgModel.getFileInfos();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel, Duration.ofHours(24));
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        String bodyText = XMLUtil.FileTemplateXml(fileInfos, "file", "urn:gsma:params:xml:ns:rcs:rcs:fthttp");
        map.put("bodyText",bodyText);
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = restTemplate.postForEntity("http://" +
                textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() +
                "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        //xml转化bean
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @Override
    public R sendMenuMsg(TextMsgModel textMsgModel) {
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel, Duration.ofHours(24));
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
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
    public R sendContributionMsg(TextMsgModel textMsgModel) {
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel, Duration.ofHours(24));
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        //获取上行信息交互id
        String inReplyToContribution = textMsgModel.getContributionID();
        map.put("inReplyToContribution",inReplyToContribution);
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
    public R sendUpMsg(TextMsgModel textMsgModel) {
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        List<FileInfo> fileInfos = textMsgModel.getFileInfos();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel, Duration.ofHours(24));
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        String bodyText = XMLUtil.FileTemplateXml(fileInfos, "file", "urn:gsma:params:xml:ns:rcs:rcs:fthttp");
        map.put("bodyText",bodyText);
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
}
