package com.example.demo01.controller.notify;

import com.example.demo01.common.R;
import com.example.demo01.entity.msgModel.NotifyResponseModel;
import com.example.demo01.entity.msgModel.SXMessage;
import com.example.demo01.entity.msgModel.TextMsgModel;
import com.example.demo01.utils.HttpHeaderUtil;
import com.example.demo01.utils.UUIDUtil;
import com.example.demo01.utils.XMLUtil;
import com.example.demo01.utils.XmlToBean;
import io.swagger.annotations.ApiOperation;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

//状态通知报告
@RestController
@RequestMapping("notify")
public class NotifyController {
    @Autowired
    HttpHeaderUtil httpHeaderUtil;
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("deliveryStatus")
    @ApiOperation(value = "获取状态通知报告",tags = "获取状态通知报告")
    public R deliveryStatus(HttpServletRequest request){
        //请求头获取主叫地址
        String address = request.getHeader("Address");
        BufferedReader reader=null;
        NotifyResponseModel notifyResponseModel=null;
        try {
            reader=request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            notifyResponseModel = XmlToBean.xmlToNotifyResponseModel(s.toString());
            System.out.println("address："+address+"    "+notifyResponseModel.toString());
            //TODO 先保存数据  再转发到机器人处理（业务）
            //TODO 处理逻辑
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        // ResponseEntity<String> response = httpsTemplate.postForEntity("https://" + messageModel.getServerRoot() + "/messaging/group/" + messageModel.getApiVersion() + "/outbound/" + messageModel.getChatbotURI() + "/requests", entity, String.class);
        //http测试使用
        ResponseEntity<String> response = restTemplate.postForEntity("http://" + textMsgModel.getServerRoot() + "/messaging/" + textMsgModel.getApiVersion() + "/outbound/" + textMsgModel.getChatbotURI() + "/requests/"+textMsgModel.getMessageId(), entity, String.class);
        //响应数据
        return R.ok();
    }
}
