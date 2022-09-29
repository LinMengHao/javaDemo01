package com.example.demo01.utils;

import com.example.demo01.entity.msgModel.TextMsgModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.ws.Action;
import java.time.Duration;
import java.util.Map;

/**
 * 请求头方法
 */
@Component
public class HttpHeaderUtil {
    @Autowired
    RedisUtils redisUtils;


    //文本消息头,鉴权信息现生成
    public HttpHeaders getHttpHeadersByText(TextMsgModel textMsgModel){
        String date = DateUtil.getGMTDate();
        textMsgModel.setDate(date);
        String cspid = textMsgModel.getCspid();
        String csptoken = textMsgModel.getCsptoken();
        String authorization = TokenUtils.getAuthorization(cspid, csptoken,date);
        textMsgModel.setAuthorization(authorization);
        //请求头
        HttpHeaders headers=new HttpHeaders();
        headers.set("Content-Type",textMsgModel.getContentType());
        StringBuilder address=new StringBuilder();
        //多发为00000，单发为手机号
        if (!"00000".equals(textMsgModel.getAddress())){
            address.append("+86");
        }
        address.append(textMsgModel.getAddress());
        headers.set("Address",address.toString());
        headers.set("Authorization",textMsgModel.getAuthorization());
        headers.set("Date",textMsgModel.getDate());
        return headers;
    }


    /**
     * 文本消息头,鉴权信息存缓存
     * @param textMsgModel
     * @param time 缓存时间
     * @return
     */
    public HttpHeaders getHttpHeadersByText(TextMsgModel textMsgModel, Duration time){
        String date = DateUtil.getGMTDate();
        textMsgModel.setDate(date);
        if(!StringUtils.hasText(redisUtils.getCacheObject("authorization"))){
            String cspid = textMsgModel.getCspid();
            String csptoken = textMsgModel.getCsptoken();
            String authorization = TokenUtils.getAuthorization(cspid, csptoken,date);
            textMsgModel.setAuthorization(authorization);
            //24小时过期
            redisUtils.setCacheObject("authorization",authorization, time);
        }else {
            textMsgModel.setAuthorization(redisUtils.getCacheObject("authorization"));
        }
        //请求头
        HttpHeaders headers=new HttpHeaders();
        headers.set("Content-Type",textMsgModel.getContentType());
        StringBuilder address=new StringBuilder();
        //多发为00000，单发为手机号
        if (!"00000".equals(textMsgModel.getAddress())){
            address.append("+86");
        }
        address.append(textMsgModel.getAddress());
        headers.set("Address",address.toString());
        headers.set("Authorization",textMsgModel.getAuthorization());
        headers.set("Date",textMsgModel.getDate());
        return headers;
    }


    public HttpHeaders getHttpHeadersByMap(Map<String,String> map){
        //请求头
        HttpHeaders headers=new HttpHeaders();
        headers.set("Content-Type",map.get("Content-Type"));
        StringBuilder address=new StringBuilder();
        //多发为00000，单发为手机号
        if (!"00000".equals(map.get("address"))){
            address.append("+86");
        }
        address.append(map.get("address"));
        headers.set("address",address.toString());
        headers.set("Authorization",map.get("Authorization"));
        headers.set("Date",map.get("Date"));
        return headers;
    }
}
