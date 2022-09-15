package com.example.demo01.controller.sendmsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;

@RestController
public class TestMsgBodyController {
    @RequestMapping("messaging/group/v1/outbound/sip%3A12599%40botplatform.rcs.chinamobile.com/requests")
    public void sendTxTMsgBody(HttpServletRequest request,HttpServletResponse response)  {
        BufferedReader reader=null;
        BufferedWriter writer=null;
        try{
            writer=new BufferedWriter(new FileWriter(new File("/Users/yoca-391/Documents/demo01/xmlfiles/test1.xml")));
            System.out.println(request.getMethod());
            System.out.println(request.getPathInfo());
            System.out.println(request.getRequestURI());
            System.out.println(request.getRequestURL());
            System.out.println(request.getQueryString());
            Enumeration<String> headerNames = request.getHeaderNames();
            StringBuilder sb=new StringBuilder();
            while  (headerNames.hasMoreElements())              //读取请求消息头
            {
                String name = headerNames.nextElement();
                String str=name +  ":"  + request.getHeader(name);
                writer.write(str);
                writer.newLine();
                writer.flush();
            }
            //获取请求体字符流
            reader = request.getReader();
            String s = null;
            while((s = reader.readLine())!=null){
                writer.write(s);
                writer.newLine();
                writer.flush();
                System.out.println(s);
            }
            //TODO 模拟返回响应xml
            response.setHeader("Content-Type","application/xml");
            response.getOutputStream().write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">\n" +
                    "<messageId>5eae954c-42ca-4181-9ab4-9c0ef2e2ac66</messageId>\n" +
                    "    <clientCorrelator>567895</clientCorrelator>\n" +
                    "</msg:outboundMessageRequest>").getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Autowired
    RestTemplate restTemplate;
    @RequestMapping("testResponse")
    public void testResponse(HttpServletResponse response) throws IOException {

    }
}
