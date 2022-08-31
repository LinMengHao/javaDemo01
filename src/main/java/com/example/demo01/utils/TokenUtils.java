package com.example.demo01.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class TokenUtils {
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

}
