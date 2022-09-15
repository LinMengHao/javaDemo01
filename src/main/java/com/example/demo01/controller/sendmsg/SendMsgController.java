package com.example.demo01.controller.sendmsg;

import com.example.demo01.common.R;
import com.example.demo01.conf.HttpsClientRequestFactory;
import com.example.demo01.entity.FileInfo;
import com.example.demo01.entity.msgModel.MessageModel;
import com.example.demo01.entity.msgModel.ResponseModel;
import com.example.demo01.utils.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sendmsg")
public class SendMsgController {
    @Autowired
    RestTemplate restTemplate;
    //https请求
    private static RestTemplate httpsTemplate=new RestTemplate(new HttpsClientRequestFactory());
    @Autowired
    MessageModel messageModel;
    @Autowired
    RedisUtils redisUtils;
    /**
     * 发送纯文本即时消息
     * @return
     */
    @RequestMapping("groupSend")
    public R textMsg(){
        initMessageModel();
        //TODO 请求头可提取工具类（待办）
        //请求头
        HttpHeaders headers=new HttpHeaders();
        headers.set("Content-Type",messageModel.getContentType());
        StringBuilder address=new StringBuilder();
        //多发为00000，单发为手机号
        if (!"00000".equals(messageModel.getAddress())){
            address.append("+86");
        }
        address.append(messageModel.getAddress());
        headers.set("address",address.toString());
        headers.set("Authorization",messageModel.getAuthorization());
        headers.set("Date",messageModel.getDate());

        //xml请求体
        List<String> list = messageModel.getDestinationAddress();
        List<String> reportRequest = messageModel.getReportRequest();
        Map<String,String> map=new HashMap<>();
        for (int i = 0; i < messageModel.getDestinationAddress().size(); i++) {
            map.put(list.get(i),"destinationAddress");
        }
        for (int i = 0; i < reportRequest.size(); i++) {
            map.put(reportRequest.get(i),"reportRequest");
        }
        String uuid32 = UUIDUtil.getUUID32();
        map.put(uuid32,"conversationID");
        map.put(messageModel.getSenderAddress(),"senderAddress");
        map.put(messageModel.getClientCorrelator(),"clientCorrelator");
        map.put(messageModel.getCapabilityId(),"capabilityId");
        map.put(messageModel.getVersion(),"version");
        map.put(messageModel.getContentType(),"contentType");
        map.put(messageModel.getBodyText(),"bodyText");
        String xml = XMLUtil.FixedTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1","outboundIMMessage");

        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
//        ResponseEntity<String> response = httpsTemplate.postForEntity("http://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //TODO 接受消息发送后的响应
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = restTemplate.postForEntity("http://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        String s = responseModelResponseEntity.getBody().toString();
        //转化
        ResponseModel responseModel = xmlToResponseModel(s);
        System.out.println(responseModel.toString());
        return R.ok();
    }
    /**
     * 发送纯文件即时消息
     * @return
     */
    @RequestMapping("fileSend")
    public R fileMsg(){
        initMessageModel();
        HttpHeaders headers = getHttpHeadersByText();
        Map<String, String> map = getParamByFile();
        String xml = XMLUtil.FixedTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1","outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
//        ResponseEntity<String> response = httpsTemplate.postForEntity("http://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //TODO 接受消息发送后的响应
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = restTemplate.postForEntity("http://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        String s = responseModelResponseEntity.getBody().toString();
        //转化
        ResponseModel responseModel = xmlToResponseModel(s);
        System.out.println(responseModel.toString());
        return R.ok();
    }

    /**
     * 将响应回来的xml字符串类型，转化成bean，提供后续使用
     * @param s
     * @return
     */
    private ResponseModel xmlToResponseModel(String s){
        ResponseModel responseModel=null;
        try {
            Document document= DocumentHelper.parseText(s);
            responseModel=new ResponseModel();
            //获取根节点
            Element rootElement = document.getRootElement();
            Element messageId = rootElement.element("messageId");
            Element clientCorrelator = rootElement.element("clientCorrelator");
            responseModel.setMessageId(messageId.getText());
            responseModel.setClientCorrelator(clientCorrelator.getText());
            System.out.println(responseModel.toString());
        }catch (DocumentException e){
            e.printStackTrace();
        }
        return responseModel;
    }


    //=========================================测试工具方法，非正式版本使用=====================================================
    //初始化一些数据，测试方便，后面应该用不上
    private void initMessageModel(){
        String date = DateUtil.getGMTDate();
        messageModel.setDate(date);
        if(!StringUtils.hasText(redisUtils.getCacheObject("authorization"))){
            String cspid = messageModel.getCspid();
            String csptoken = messageModel.getCsptoken();
            String authorization = TokenUtils.getAuthorization(cspid, csptoken);
            messageModel.setAuthorization(authorization);
            //24小时过期
            redisUtils.setCacheObject("authorization",authorization, Duration.ofHours(24));
        }else {
            messageModel.setAuthorization(redisUtils.getCacheObject("authorization"));
        }
        String testAddress = messageModel.getTestAddress();
        String reports = messageModel.getReports();
        List<String> list = messageModel.getDestinationAddress();
        List<String> reportRequest = messageModel.getReportRequest();
        if(testAddress.indexOf(",")!=-1){
            String[] split = testAddress.split(",");
            for (String phone:split){
                list.add(phone);
            }
        }else {
            list.add(testAddress);
        }
        if(reports.indexOf(",")!=-1){
            String[] split = reports.split(",");
            for (String report:split){
                reportRequest.add(report);
            }
        }else {
            reportRequest.add(reports);
        }
    }

    //文本消息 获取请求头（文件也可用）
    private HttpHeaders getHttpHeadersByText(){
        //请求头
        HttpHeaders headers=new HttpHeaders();
        headers.set("Content-Type",messageModel.getContentType());
        StringBuilder address=new StringBuilder();
        //多发为00000，单发为手机号
        if (!"00000".equals(messageModel.getAddress())){
            address.append("+86");
        }
        address.append(messageModel.getAddress());
        headers.set("address",address.toString());
        headers.set("Authorization",messageModel.getAuthorization());
        headers.set("Date",messageModel.getDate());
        return headers;
    }
    //基础参数
    private Map<String,String> getParam(){
        //xml请求体
        List<String> list = messageModel.getDestinationAddress();
        Map<String,String> map=new HashMap<>();
        for (int i = 0; i < messageModel.getDestinationAddress().size(); i++) {
            map.put(list.get(i),"destinationAddress");
        }
        String uuid32 = UUIDUtil.getUUID32();
        map.put(uuid32,"conversationID");
        map.put(messageModel.getSenderAddress(),"senderAddress");
        map.put(messageModel.getCapabilityId(),"capabilityId");
        map.put(messageModel.getVersion(),"version");
        map.put(messageModel.getContentType(),"contentType");
        return map;
    }
    //文本消息所需参数
    private Map<String,String> getParamByText(String bodyText){
        Map<String, String> map = getParam();
        List<String> reportRequest = messageModel.getReportRequest();
        for (int i = 0; i < reportRequest.size(); i++) {
            map.put(reportRequest.get(i),"reportRequest");
        }
        map.put(messageModel.getClientCorrelator(),"clientCorrelator");
        map.put(bodyText,"bodyText");
        return map;
    }
    //文件消息所需参数
    private Map<String,String> getParamByFile(){
        Map<String, String> map = getParam();
        //xml请求体
        List<String> list = messageModel.getDestinationAddress();
        for (int i = 0; i < messageModel.getDestinationAddress().size(); i++) {
            map.put(list.get(i),"destinationAddress");
        }
        map.put(messageModel.getClientCorrelator(),"clientCorrelator");
        List<FileInfo> infos=new ArrayList<>();
        FileInfo info1=new FileInfo();
        info1.setContentType("image/jpeg");
        info1.setFileSize("15191");
        info1.setType("thumbnail");
        Map<String, String> data = info1.getData();
        data.put("url","https://http01.hn.rcs.chinamobile.com:9091/Access/PF?ID=QzYzNTE5Njc2NDUzMzhDQTk3OUMzQzQxQTkwN0ZCNjQyOTFGMjU4OTlFOURFREE4NTAyN0IxNjcyOUZEQTBFNjNEQTQ0M0E5OENCQjE4MzdGQzQzRkJENkM0RjQwOTE0");
        data.put("until","2022-08-12T23:59:59Z");

        FileInfo info2=new FileInfo();
        info2.setContentType("video/mp4");
        info2.setFileSize("1482235");
        info2.setType("file");
        info2.setFileName("mda-merqtf8xju5x91gn.mp4");
        Map<String, String> data2 = info2.getData();
        data2.put("url","https://http01.hn.rcs.chinamobile.com:9091/Access/PF?ID=NjJBNDEyNURGMzc1NjgxNTZBMzdFOERFQ0M5NDE4QzlEODc2MTRCNkM1NDlEQzhEQkZGQjBBQ0YxQ0MyMTZGQ0UyMzk5NkNGRTk3NTg5QURGRjg1RjBFRTkxNDI3QzZD");
        data2.put("until","2022-08-12T23:59:59Z");
        infos.add(info1);
        infos.add(info2);
        String xml = XMLUtil.FileTemplateXml(infos, "file", "urn:gsma:params:xml:ns:rcs:rcs:fthttp");
        map.put(xml,"bodyText");
        return map;
    }
}
