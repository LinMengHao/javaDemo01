package com.example.demo01.utils;

import com.example.demo01.entity.FileInfo;
import com.example.demo01.entity.msgModel.TextMsgModel;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean 转 map
 * 反射，不同方法构建不同规则map
 */
public class BeanToMapUtil {
    //获取文本消息bean时，进行特殊转化成map，作为参数，构建二次https请求接入层
    public static Map<String,String> getTxtParamMap(TextMsgModel textMsgModel){
        Map<String,String> map=new HashMap<>();
        try {
            Class<? extends TextMsgModel> aClass = textMsgModel.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field:declaredFields){
                //暴力反射
                field.setAccessible(true);
                String name = field.getName();
                if("serialVersionUID".equals(name)||"serverRoot".equals(name)||"apiVersion".equals(name)
                        ||"cspid".equals(name)||"csptoken".equals(name)||"authorization".equals(name)
                        ||"Date".equals(name)||"address".equals(name)){
                    continue;
                }
                if("destinationAddress".equals(name)||"reportRequest".equals(name)){
                    List<String> list=(List<String>)field.get(textMsgModel);
                    System.out.println(list.toString());
                    for (int i = 0; i <list.size() ; i++) {
                        map.put(list.get(i),name);
                    }
                }else {
                    String o = (String)field.get(textMsgModel);
                    System.out.println(name+": "+o);
                    if(StringUtils.hasText(o)){
                        map.put(o,name);
                    }
                }
            }
        }catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
