package com.example.demo01;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientUtils {
    public static void main(String[] args) {
        //创建httpclient对象，相当于打开一个浏览器
        CloseableHttpClient httpClient= HttpClients.createDefault();
        //创建一个httpGet请求，相当于在浏览器输入地址
        HttpGet httpGet=new HttpGet("http://www.baidu.com/");
        CloseableHttpResponse response=null;
        try{
            //执行请求，等待响应
            response = httpClient.execute(httpGet);
            System.out.println("响应消息："+response);
            //状态码200，请求成功
            if(response.getStatusLine().getStatusCode()==200){
                //解析响应
                String s = EntityUtils.toString(response.getEntity());
                System.out.println("响应内容："+s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放资源

                try {
                    if(response!=null) {
                        response.close();
                    }
                    httpClient.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}
