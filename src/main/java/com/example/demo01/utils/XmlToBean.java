package com.example.demo01.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.demo01.entity.DeliveryInfo;
import com.example.demo01.entity.FileInfo;
import com.example.demo01.entity.msgModel.GroupMsgResponseCode;
import com.example.demo01.entity.msgModel.NotifyResponseModel;
import com.example.demo01.entity.msgModel.ResponseModel;
import com.example.demo01.entity.msgModel.SXMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlToBean {
    public static ResponseModel xmlToResponseModel(String s){
        ResponseModel responseModel=null;
        try {
            Document document= DocumentHelper.parseText(s);
            responseModel=new ResponseModel();
            //获取根节点
            Element rootElement = document.getRootElement();
            Element messageId = rootElement.element("messageId");
            Element clientCorrelator = rootElement.element("clientCorrelator");
            if (messageId != null) {
                responseModel.setMessageId(messageId.getText());
            }
            if (clientCorrelator!=null){
                responseModel.setClientCorrelator(clientCorrelator.getText());
            }
            if (rootElement.element("policyException")!=null){
                Element policyException = rootElement.element("policyException");
                Element exceptionId = policyException.element("exceptionId");
                Element text = policyException.element("text");
                Element variables = policyException.element("variables");
                responseModel.setVariables(variables.getText());
                responseModel.setText(text.getText());
                responseModel.setExceptionId(exceptionId.getText());
                responseModel.setResponseCode(GroupMsgResponseCode.ERROR);
            }else {
                //群发响应节点
                List<DeliveryInfo> deliveryInfos = responseModel.getDeliveryInfos();
                Element deliveryInfoList = rootElement.element("deliveryInfoList");
                if(deliveryInfoList!=null){
                    List<Element> elements = deliveryInfoList.elements("deliveryInfo");
                    for (int i = 0; i < elements.size(); i++) {
                        DeliveryInfo deliveryInfo=new DeliveryInfo();
                        Element element = elements.get(i);
                        Element address = element.element("address");
                        deliveryInfo.setAddress(address.getText());
                        Element deliveryStatus = element.element("deliveryStatus");
                        deliveryInfo.setDeliveryStatus(deliveryStatus.getText());
                        Element policyException = element.element("policyException");
                        if(policyException!=null){
                            Element exceptionId = policyException.element("exceptionId");
                            Element text = policyException.element("text");
                            Element variables = policyException.element("variables");
                            deliveryInfo.setText(text.getText());
                            deliveryInfo.setVariables(variables.getText());
                            deliveryInfo.setExceptionId(exceptionId.getText());
                            responseModel.setResponseCode(GroupMsgResponseCode.SEGMENT);
                        }
                        deliveryInfos.add(deliveryInfo);
                    }
                }
            }
            System.out.println(responseModel.toString());
        }catch (DocumentException e){
            e.printStackTrace();
        }
        return responseModel;
    }

    public static NotifyResponseModel xmlToNotifyResponseModel(String s){
        Map<String,String>map=new HashMap<>();
        NotifyResponseModel o=null;
        try{
            Document document=DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements();
            for (int i = 0; i < elements.size(); i++) {
                System.out.println(elements.get(i).getName());
                Element element = elements.get(i);
                if("deliveryInfo".equals(element.getName())){
                    List<Element> list = element.elements();
                    for (int j = 0; j < list.size(); j++) {
                        map.put(list.get(j).getName(),list.get(j).getText());
                    }
                }else if("description".equals(element.getName())){
                        map.put(element.getName(),element.getText());
                        map.put("code","success");
                }else if("link".equals(element.getName())){
                    if(StringUtils.hasText(element.attributeValue("rel"))){
                        map.put("rel",element.attributeValue("rel"));
                    }
                    if(StringUtils.hasText(element.attributeValue("href"))){
                        map.put("href",element.attributeValue("href"));
                    }
                }
            }
            o= (NotifyResponseModel)mapToObject(map, NotifyResponseModel.class);
            System.out.println(o.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //map转java对象
    public static Object mapToObject(Map<String, String> map, Class<?> beanClass) throws Exception {
        String jsonStr = JSONObject.toJSONString(map);
        return JSONObject.parseObject(jsonStr, beanClass);
    }

    @Test
    public void test(){
        xmlToNotifyResponseModel("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<msg:deliveryInfoNotification xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">  \n" +
                "<deliveryInfo>\n" +
                "    <address> tel:+8619585550103</address>\n" +
                "    <messageId>5eae954c-42ca-4181-9ab4-9c0ef2e2ac55</messageId>\n" +
                "    <deliveryStatus>DeliveryImpossible</deliveryStatus>\n" +
                "<description>SVC0002</description>\n" +
                "    <text>AO msg service capability invalid</text>\n" +
                "</deliveryInfo>\n" +
                "<link rel=\"OutboundMessageRequest\"    href=\"http://example.com/exampleAPI/messaging/v1/outbound/sip%3A12599%40botplatform.rcs.chinamobile.com/requests/5eae954c-42ca-4181-9ab4-9c0ef2e2ac55\"/>\n" +
                "</msg:deliveryInfoNotification>");
    }

    public static SXMessage xmlToSXMessage(String s){
        SXMessage sxMessage=new SXMessage();
        try {
            Document document=DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element inboundMessage = rootElement.element("inboundMessage");
            Element destinationAddress = inboundMessage.element("destinationAddress");
            Element senderAddress = inboundMessage.element("senderAddress");
            Element origUser = inboundMessage.element("origUser");
            Element resourceURL = inboundMessage.element("resourceURL");
            Element link = inboundMessage.element("link");
            Element messageId = inboundMessage.element("messageId");
            Element contentType = inboundMessage.element("contentType");
            Element serviceCapability = inboundMessage.element("serviceCapability");
            Element capabilityId = serviceCapability.element("capabilityId");
            Element version = serviceCapability.element("version");
            Element bodyText = inboundMessage.element("bodyText");
            Element conversationID = inboundMessage.element("conversationID");
            Element contributionID = inboundMessage.element("contributionID");
            String rel = link.attributeValue("rel");
            String href = link.attributeValue("href");
            List<String>list=new ArrayList<>();
            list.add(rel);
            list.add(href);
            sxMessage.setBodyText(bodyText.getText());
            sxMessage.setCapabilityId(capabilityId.getText());
            sxMessage.setContentType(contentType.getText());
            sxMessage.setLink(list);
            sxMessage.setContributionID(contributionID.getText());
            sxMessage.setConversationID(conversationID.getText());
            sxMessage.setVersion(version.getText());
            sxMessage.setDestinationAddress(destinationAddress.getText());
            sxMessage.setMessageId(messageId.getText());
            sxMessage.setOrigUser(origUser.getText());
            sxMessage.setSenderAddress(senderAddress.getText());
            sxMessage.setResourceURL(resourceURL.getText());
        }catch (DocumentException e) {
            e.printStackTrace();
        }
        return sxMessage;
    }

    //上行文件
    public static SXMessage xmlToSXFileMessage(String s){
        SXMessage sxMessage=null;
        try {
            sxMessage = xmlToSXMessage(s);
            String bodyText = sxMessage.getBodyText();
            Document document=DocumentHelper.parseText(bodyText);
            Element file = document.getRootElement();
            String xmlns = file.attributeValue("xmlns");
            List<FileInfo> fileInfos = sxMessage.getFileInfos();
            List<Element> elements = file.elements();
            for (int i = 0; i < elements.size(); i++) {
                FileInfo fileInfo=new FileInfo();
                Element element = elements.get(i);
                String type = element.attributeValue("type");
                fileInfo.setType(type);
                Element file_size = element.element("file-size");
                if(file_size!=null){
                    fileInfo.setFileSize(file_size.getText());
                }
                Element file_name = element.element("file-name");
                if(file_name!=null){
                    fileInfo.setFileName(file_name.getText());
                }
                Element content_type = element.element("content-type");
                if(content_type!=null){
                    fileInfo.setContentType(content_type.getText());
                }

                Element data = element.element("data");
                Map<String,String> map=new HashMap<>();
                if(data!=null){
                    if(StringUtils.hasText(data.attributeValue("url"))){
                        map.put("url",data.attributeValue("url"));
                    }
                    if(StringUtils.hasText(data.attributeValue("url"))){
                        map.put("until",data.attributeValue("until"));
                    }
                }
                fileInfo.setData(map);
                fileInfos.add(fileInfo);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return sxMessage;
    }
}