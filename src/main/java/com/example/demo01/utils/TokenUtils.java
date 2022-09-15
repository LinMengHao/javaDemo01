package com.example.demo01.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TokenUtils {
    //获取GMT时间
    public static String getGMTDate() {
        String format=null;
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date=new Date();
            format = simpleDateFormat.format(date);
            System.out.println(format);
        }catch (Exception e){
            e.printStackTrace();
            //TODO 日志
        }
        return format;
    }
    //获取鉴权字符串 Authorization
    public static String getAuthorization(String cspid,String cspToken){
        String date = getGMTDate();
        String sha256 = Sha256Utils.getSHA256(cspToken + date);
        String authorization = Base64Utils.encode(cspid + ":" + sha256);
        return "Basic "+authorization;
    }



//==========================================TEST==============================================

    //获取sign，签名规则：去除null后，字典排序，通过sha256后用base64
    public static String getSign(JSONObject jsonObject){
        //将json字符串转treeMap，在treeMap做排序，并去除null
        TreeMap<String,String> treeMap=JSONObject.parseObject(jsonObject.toJSONString(),
                new TypeReference<TreeMap<String,String>>(){});
        StringBuilder stringBuilder=new StringBuilder();
        for (Map.Entry<String,String> entry:treeMap.entrySet()){
            if(entry.getValue()==null){
                continue;
            }
            stringBuilder.append("&");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
        }
        stringBuilder.deleteCharAt(0);
        //算法加密sha256
        String sha256 = Sha256Utils.getSHA256(stringBuilder.toString());
        //base64加密
        String result = Base64Utils.encode(sha256);
        return result;
    }


    public static void main(String[] args) {
        Long start=System.currentTimeMillis();
        TreeMap<String,String> treeMap=new TreeMap<>();
        treeMap.put("apiKey","15-73728378");
        treeMap.put("apiSecrect","dghs");
        treeMap.put("reqTime","2022");
        treeMap.put("boy",null);
        for (Map.Entry<String,String> entry:treeMap.entrySet()){
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
        Long end=System.currentTimeMillis();
        System.out.println(end-start+"毫秒");
    }
    @Test
    public void test(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("apiKey","15-23930392033092");
        jsonObject.put("apiSecrect","#239*3");
        jsonObject.put("reqTime","1642670601273");
        String sign = getSign(jsonObject);
        System.out.println(sign);
    }

    @Test
    public void test1() throws ParseException {
        String token="ff556388699c84a4ae73bc719eda6480c0d8290c575f297d7f65dad8f9c6804f";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2020-07-06 19:28:53");
        String s = date.toGMTString();
        System.out.println(s);
        String appid=Sha256Utils.getSHA256(token+"Mon, 0"+s);
        System.out.println(appid);
        String encode = Base64Utils.encode("test12345"+":"+appid);
        System.out.println(encode);
    }


    @Test
    public void test2() throws ParseException {
        getGMTDate();
    }

}
