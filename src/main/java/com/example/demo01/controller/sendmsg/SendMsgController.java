package com.example.demo01.controller.sendmsg;

import com.alibaba.fastjson.JSONObject;
import com.example.demo01.common.R;
import com.example.demo01.conf.HttpsClientRequestFactory;
import com.example.demo01.conf.HttpsSkipRequestFactory;
import com.example.demo01.entity.FileInfo;
import com.example.demo01.entity.msgModel.MessageModel;
import com.example.demo01.entity.msgModel.ResponseModel;
import com.example.demo01.entity.msgModel.TextMsgModel;
import com.example.demo01.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//TODO 状态响应
@RestController
@RequestMapping("sendmsg")
@Api(value = "发送消息接口测试",tags = "发送消息相关接口")
public class SendMsgController {
    @Autowired
    RestTemplate restTemplate;
    //https请求
//    private static RestTemplate httpsTemplate=new RestTemplate(new HttpsClientRequestFactory());
    private static RestTemplate httpsTemplate=new RestTemplate(new HttpsSkipRequestFactory());
    @Autowired
    MessageModel messageModel;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    HttpHeaderUtil httpHeaderUtil;
    /**
     * 发送纯文本即时消息
     * @return
     */
    @RequestMapping("txtSend")
    @ApiOperation(value = "文本消息发送",notes = "文本相关消息的发送功能")
    public R txtMsg(@RequestBody TextMsgModel textMsgModel) {
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);

        //消息内容请求体
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
         ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" +textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
//        ResponseEntity<String> responseModelResponseEntity = restTemplate.postForEntity("http://" + textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        //xml转化bean
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModelResponseEntity.getStatusCode());
        System.out.println(responseModelResponseEntity.getHeaders().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @RequestMapping("fileSend")
    @ApiOperation(value = "文件消息发送",notes = "文件相关消息的发送功能")
    public R fileMsg(@RequestBody TextMsgModel textMsgModel) {
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        List<FileInfo> fileInfos = textMsgModel.getFileInfos();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
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
        ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" + textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //响应数据
        //xml转化bean
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @RequestMapping("cardSend")
    @ApiOperation(value = "单卡片或多卡片或消息",tags = "单卡片或多卡片消息")
    public R cardMsg(@RequestBody TextMsgModel textMsgModel){
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" +textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModelResponseEntity.getStatusCode());
        System.out.println(responseModelResponseEntity.getHeaders().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @RequestMapping("cardSuggestSend")
    @ApiOperation(value = "下行单卡片带内置按钮",tags = "下行单卡片带内置按钮")
    public R cardSuggestSend(@RequestBody TextMsgModel textMsgModel){
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" +textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModelResponseEntity.getStatusCode());
        System.out.println(responseModelResponseEntity.getHeaders().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @RequestMapping("cardsSuggestSend")
    @ApiOperation(value = "下行单播多卡片带内置按钮",tags = "下行单播多卡片带内置按钮")
    public R cardsSuggestSend(@RequestBody TextMsgModel textMsgModel){
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" +textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModelResponseEntity.getStatusCode());
        System.out.println(responseModelResponseEntity.getHeaders().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @RequestMapping("txtMenuSend")
    @ApiOperation(value = "下行单播携带悬浮菜单文本消息",tags = "下行单播携带悬浮菜单文本消息")
    public R txtMenuSend(@RequestBody TextMsgModel textMsgModel){
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" +textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModelResponseEntity.getStatusCode());
        System.out.println(responseModelResponseEntity.getHeaders().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @RequestMapping("contributionMsg")
    @ApiOperation(value = "交互下行消息",tags = "交互下行消息")
    public R contributionMsg(@RequestBody TextMsgModel textMsgModel){
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
        //请求体
        //标签 带上需要的随机id
        String uuid32 = UUIDUtil.getUUID32();
        map.put("conversationID",uuid32);
        //获取上行信息交互id
        String inReplyToContributionID = textMsgModel.getContributionID();
        map.put("inReplyToContributionID",inReplyToContributionID);
        String xml = XMLUtil.txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        HttpEntity<String> entity=new HttpEntity<String>(xml,headers);
        //https
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" +textMsgModel.getServerRoot() + "/messaging/interaction/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModelResponseEntity.getStatusCode());
        System.out.println(responseModelResponseEntity.getHeaders().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    //各种消息回落，直接在各种消息参数，补上对应即可，可用愿方法，该方法用于测试
    @RequestMapping("fallbackSend")
    @ApiOperation(value = "下行消息回落up1.0文本消息",tags = "下行消息回落up1.0文本消息")
    public R fallbackSend(@RequestBody TextMsgModel textMsgModel){
        System.out.println(textMsgModel.toString());
        Map<String, String> map = textMsgModel.getMap();
        List<FileInfo> fileInfos = textMsgModel.getFileInfos();
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel);
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
        ResponseEntity<String> responseModelResponseEntity = httpsTemplate.postForEntity("https://" +textMsgModel.getServerRoot() + "/messaging/group/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseModel responseModel = XmlToBean.xmlToResponseModel(responseModelResponseEntity.getBody().toString());
        System.out.println(responseModelResponseEntity.getStatusCode());
        System.out.println(responseModelResponseEntity.getHeaders().toString());
        System.out.println(responseModel.toString());
        return R.ok();
    }

    @RequestMapping("withdraw")
    @ApiOperation(value = "撤回消息",tags = "撤回消息")
    public R withdraw(@RequestBody TextMsgModel textMsgModel){
        System.out.println(textMsgModel.toString());
        //请求头 鉴权信息有效时间为24小时
        HttpHeaders headers = httpHeaderUtil.getHttpHeadersByText(textMsgModel, Duration.ofHours(24));
        //请求体
        Document document= DocumentHelper.createDocument();
        Element root = document.addElement("msg:messageStatusReport", "urn:oma:xml:rest:netapi:messaging:1");
        Element status = root.addElement("status");
        status.setText("RevokeRequested");
        Element address = root.addElement("address");
        address.setText("tel:+86"+textMsgModel.getAddress());
        String xmlStr = XMLUtil.getXmlStr(document, "UTF-8", false);
        HttpEntity<String> entity=new HttpEntity<String>(xmlStr,headers);
        //https
        ResponseEntity<String> response = httpsTemplate.exchange("https://" + messageModel.getServerRoot() + "/messaging/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests/"+textMsgModel.getMessageId()+"/status", HttpMethod.PUT, entity, String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders().toString());
        return R.ok();
    }


    @Test
    public void test(){
        JSONObject object=new JSONObject();
        object.put("bodyText","--next\n" +
                "Content-Type: text/plain\n" +
                "Content-Disposition: inline; filename=\"Message\"\n" +
                "Content-Length: 42\n" +
                "\n" +
                "只是一条带悬浮菜单纯文本消息\n" +
                "--next\n" +
                "Content-Type: application/vnd.gsma.botsuggestion.v1.0+json\n" +
                "Content-Disposition: inline; filename=\"Chiplist.lst\"\n" +
                "Content-Length: 572\n" +
                "\n" +
                "{\n" +
                "    \"suggestions\": [\n" +
                "        {\n" +
                "            \"reply\": {\n" +
                "                \"displayText\": \"上行YES\",\n" +
                "                \"postback\": {\n" +
                "                    \"data\": \"YES\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"displayText\": \"打开链接\",\n" +
                "                \"postback\": {\n" +
                "                    \"data\": \"user_open_url\"\n" +
                "                },\n" +
                "                \"urlAction\": {\n" +
                "                    \"openUrl\": {\n" +
                "                        \"application\": \"webview\",\n" +
                "                        \"url\": \"https://rcs.10086.cn/\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"dialerAction\": {\n" +
                "                    \"dialPhoneNumber\": {\n" +
                "                        \"phoneNumber\": \"10086\"\n" +
                "                    }\n" +
                "                },\n" +
                "                \"displayText\": \"拨打10086\",\n" +
                "                \"postback\": {\n" +
                "                    \"data\": \"dialPhoneNumber_10086\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"mapAction\": {\n" +
                "                    \"requestLocationPush\": {}\n" +
                "                },\n" +
                "                \"displayText\": \"上报当前位置\",\n" +
                "                \"postback\": {\n" +
                "                    \"data\": \"set_by_chatbot_request_location_push\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"mapAction\": {\n" +
                "                    \"showLocation\": {\n" +
                "                        \"location\": {\n" +
                "                            \"latitude\": 23.170408552888855,\n" +
                "                            \"longitude\": 113.40224851582335,\n" +
                "                            \"label\": \"中国移动南方基地智汇中心\"\n" +
                "                        },\n" +
                "                        \"fallbackUrl\": \"https://j.map.baidu.com/eb/XWkf\"\n" +
                "                    }\n" +
                "                },\n" +
                "                \"displayText\": \"搜索并导航到“中国移动南方基地智汇中心”\",\n" +
                "                \"postback\": {\n" +
                "                    \"data\": \"set_by_chatbot_open_map\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}\n" +
                "--next--");
        System.out.println(object.toJSONString());
    }



}

