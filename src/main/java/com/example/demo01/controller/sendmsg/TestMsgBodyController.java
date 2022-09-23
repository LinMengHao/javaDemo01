package com.example.demo01.controller.sendmsg;

import com.example.demo01.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.List;

import static java.lang.System.out;

@RestController
public class TestMsgBodyController {
    @RequestMapping("messaging/group/v1/outbound/sip%3A12599%40botplatform.rcs.chinamobile.com/requests")
    public void sendTxTMsgBody(HttpServletRequest request,HttpServletResponse response)  {
        BufferedReader reader=null;
        BufferedWriter writer=null;
        try{
            writer=new BufferedWriter(new FileWriter(new File("/Users/yoca-391/Documents/demo01/xmlfiles/test1.xml")));
            out.println(request.getMethod());
            out.println(request.getPathInfo());
            out.println(request.getRequestURI());
            out.println(request.getRequestURL());
            out.println(request.getQueryString());
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
                out.println(s);
            }
            //TODO 模拟返回响应xml
            response.setHeader("Content-Type","application/xml");
            //全部响应成功
            response.getOutputStream().write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">\n" +
                    "<messageId>5eae954c-42ca-4181-9ab4-9c0ef2e2ac66</messageId>\n" +
                    "    <clientCorrelator>567895</clientCorrelator>\n" +
                    "</msg:outboundMessageRequest>").getBytes());
            //部分响应成功
//            response.getOutputStream().write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                    "<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">\n" +
//                    "<messageId>5eae954c-42ca-4181-9ab4-9c0ef2e2ac66</messageId>\n" +
//                    "<clientCorrelator>567895</clientCorrelator>\n" +
//                    "<deliveryInfoList>\n" +
//                    "<deliveryInfo>\n" +
//                    "<address>tel:+8619585550104</address>\n" +
//                    "<deliveryStatus>MessageWaiting</deliveryStatus>\n" +
//                    "</deliveryInfo>\n" +
//                    "<deliveryInfo>\n" +
//                    "<address>tel:+8619585550104</address>\n" +
//                    "<deliveryStatus> DeliveryImpossible </deliveryStatus>\n" +
//                    "<policyException>\n" +
//                    "      <exceptionId>POL0003</exceptionId>\n" +
//                    "      <text>Too many addresses specified in message part %1</text>\n" +
//                    "      <variables>destinationAddress</variables>\n" +
//                    "   </policyException>\n" +
//                    "</deliveryInfo>\n" +
//                    "<deliveryInfo>\n" +
//                    "<address>tel:+8619585550105</address>\n" +
//                    "<deliveryStatus>DeliveryImpossible</deliveryStatus>\n" +
//                    "<policyException>\n" +
//                    "      <exceptionId>POL0003</exceptionId>\n" +
//                    "      <text>Too many addresses specified in message part %1</text>\n" +
//                    "      <variables>destinationAddress</variables>\n" +
//                    "   </policyException>\n" +
//                    "</deliveryInfo>\n" +
//                    "<deliveryInfo>\n" +
//                    "<address>tel:+8619585550106</address>\n" +
//                    "<deliveryStatus>DeliveryImpossible</deliveryStatus>\n" +
//                    "</deliveryInfo>\n" +
//                    "</deliveryInfoList>\n" +
//                    "</msg:outboundMessageRequest>").getBytes());
            //列表过大，响应失败
//            response.getOutputStream().write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                    "<common:requestError xmlns:common=\"urn:oma:xml:rest:netapi:common:1\">\n" +
//                    "   <policyException>\n" +
//                    "      <exceptionId>POL0003</exceptionId>\n" +
//                    "      <text>Too many addresses specified in message part %1</text>\n" +
//                    "      <variables>destinationAddress</variables>\n" +
//                    "   </policyException>\n" +
//                    "</common:requestError>").getBytes());
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
    @RequestMapping("testReceiveTxtMsg")
    public void testReceiveTxtMsg(){
        HttpHeaders headers=new HttpHeaders();
        headers.set("Address","+8613800138002@bj.ims.mnc000.mcc460.3gppnetwork.org");
        headers.set("Content-Type","application/xml");
        HttpEntity<String>entity=new HttpEntity<>("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<msg:inboundMessageNotification xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">\n" +
                "<inboundMessage>\n" +
                "      <destinationAddress>sip:12599@botplatform.rcs.chinamobile.com</destinationAddress>\n" +
                "      <senderAddress> sip:+8613800138002@bj.ims.mnc000.mcc460.3gppnetwork.org </senderAddress> \n" +
                "      <origUser> sip:13800138002@bj.ims.mnc000.mcc460.3gppnetwork.org </origUser>\n" +
                "<resourceURL>https://example.com/exampleAPI/messaging/v1/inbound/messages/5eae954c-42ca-4181-9ab4-9c0ef2e2ac66</resourceURL>\n" +
                "      <link rel=\"Subscription\"  href=\"https://example.com/exampleAPI/messaging/v1/inbound/subscriptions/sub123\"/>\n" +
                "      <messageId>5eae954c-42ca-4181-9ab4-9c0ef2e2ac66</messageId>\n" +
                "      <contentType>text/plain</contentType>\n" +
                "<serviceCapability>\n" +
                "<capabilityId>ChatbotSA</capabilityId>\n" +
                "<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>\n" +
                "</serviceCapability>\n" +
                "<bodyText>我想聊天</bodyText>\n" +
                "      <conversationID>fceb900a-5018-4d11-9450-5bfbdc951ba0</conversationID>\n" +
                "      <contributionID>fceb900a-5018-4d11-9450-5bfbdc951ba0</contributionID>\n" +
                "   </inboundMessage>\n" +
                "</msg:inboundMessageNotification>",headers);
        restTemplate.postForEntity("http://localhost:8888/receive/txtMsg",entity,String.class);
    }

    @RequestMapping("testDownloadFile")
    public void testDownloadFile(HttpServletResponse response){
        FileInputStream in=null;
        ServletOutputStream out=null;
        try{
            in=new FileInputStream(new File("/Users/yoca-391/Documents/demo01/FILE/KING/test1.mp4"));
            response.setContentType("multipart/form-data");
            response.addHeader("Content-Disposition","form-data;"+" name="+"File"+";"+" filename="+"test2.mp4");
            out = response.getOutputStream();
            int len=0;
            byte[] bytes=new byte[1024*10];
            while((len=in.read(bytes))!=-1){
                out.write(bytes,0,bytes.length);
            }
            out.flush();
            response.setStatus(410);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @RequestMapping("testbatchYD")
    public R testbatchYD(HttpServletRequest request){
        System.out.println(request.getContentType());
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            out.println("参数名："+name + "\t" +"结果："+ request.getParameter(name));
        }
        List<MultipartFile> thumbnails = ((MultipartHttpServletRequest) request).getFiles("Thumbnail");
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("File");
        for (int i = 0; i < thumbnails.size(); i++) {
            MultipartFile file = thumbnails.get(i);
            if (file.isEmpty()) {
                return R.error().message("文件为空");
            }
            String contentType = file.getContentType();
            System.out.println("文件格式："+contentType);
            long size = file.getSize();
            System.out.println("文件大小："+size+"字节（磁盘约"+size/1000+"KB)");
            // 获取文件名
            String fileName = file.getOriginalFilename();
            System.out.println("上传的文件名为：" + fileName);//写日志
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            System.out.println("文件的后缀名为：" + suffixName);//写日志
            // 设置文件存储路径         *************************************************
            String filePath = "./FILE/CHATBOT/";
            String path = filePath + fileName;
            File dest = new File(new File(path).getAbsolutePath());// dist为文件，有多级目录的文件
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            try {
                file.transferTo(dest);// 文件写入
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return R.error().message("文件为空");
            }
            String contentType = file.getContentType();
            System.out.println("文件格式："+contentType);
            long size = file.getSize();
            System.out.println("文件大小："+size+"字节（磁盘约"+size/1000+"KB)");
            // 获取文件名
            String fileName = file.getOriginalFilename();
            System.out.println("上传的文件名为：" + fileName);//写日志
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            System.out.println("文件的后缀名为：" + suffixName);//写日志
            // 设置文件存储路径         *************************************************
            String filePath = "./FILE/CHATBOT/";
            String path = filePath + fileName;
            File dest = new File(new File(path).getAbsolutePath());// dist为文件，有多级目录的文件
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            try {
                file.transferTo(dest);// 文件写入
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return R.ok();
    }

    @RequestMapping("testDeleteFile")
    public R testDeleteFile(HttpServletRequest request){
        BufferedWriter writer=null;
        try{
            writer=new BufferedWriter(new FileWriter(new File("/Users/yoca-391/Documents/demo01/xmlfiles/test2.txt")));
            out.println(request.getMethod());
            out.println(request.getPathInfo());
            out.println(request.getRequestURI());
            out.println(request.getRequestURL());
            out.println(request.getQueryString());
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
        }catch(IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return R.ok();
    }

}
