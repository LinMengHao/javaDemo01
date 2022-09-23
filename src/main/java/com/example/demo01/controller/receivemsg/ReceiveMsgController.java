package com.example.demo01.controller.receivemsg;

import com.example.demo01.common.R;
import com.example.demo01.conf.HttpsClientRequestFactory;
import com.example.demo01.entity.FileInfo;
import com.example.demo01.entity.msgModel.MessageModel;
import com.example.demo01.entity.msgModel.SXMessage;
import com.example.demo01.utils.DateUtil;
import com.example.demo01.utils.TokenUtils;
import com.example.demo01.utils.XmlToBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("receive")
@Api(value = "上行文本消息",tags = "上行文本消息")
public class ReceiveMsgController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MessageModel messageModel;
    private static RestTemplate httpsTemplate=new RestTemplate(new HttpsClientRequestFactory());
    @RequestMapping("txtMsg")
    @ApiOperation(value = "接受文本上行消息",notes = "接受文本上行消息")
    public R receiveTxtMsg(HttpServletRequest request){
        //请求头获取主叫地址
        String address = request.getHeader("Address");
        BufferedReader reader=null;
        SXMessage sxMessage=null;
        try {
            reader=request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            sxMessage = XmlToBean.xmlToSXMessage(s.toString());
            //TODO 先保存数据  再转发到机器人处理（业务）
            List<String> link = sxMessage.getLink();
            String rel = link.get(0);
            String href = link.get(1);
            System.out.println("address："+address+"    "+sxMessage.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }
    //从YD下载用户上行文件，内容必须一致 TODO 判断有效期
    @RequestMapping("fileMsg")
    @ApiOperation(value = "接收上行图片消息",tags = "接收上行图片消息")
    public R receiveFileMsg(HttpServletRequest request){
        String address = request.getHeader("Address");
        BufferedReader reader=null;
        SXMessage sxMessage=null;
        FileOutputStream out=null;
        try {
            reader=request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            sxMessage = XmlToBean.xmlToSXFileMessage(s.toString());
            //TODO 先保存数据  再转发到机器人处理（业务）
            List<String> link = sxMessage.getLink();
            String rel = link.get(0);
            String href = link.get(1);
            System.out.println("address："+address+"    "+sxMessage.toString());
            //TODO 下载文件
            List<FileInfo> fileInfos = sxMessage.getFileInfos();
            for (FileInfo fileInfo:fileInfos){
                HttpHeaders headers=new HttpHeaders();
                headers.set("Terminal-type","Chatbot");
                String date = DateUtil.getGMTDate();
                headers.set("Date",date);
                headers.set("Authorization", TokenUtils.getAuthorization(messageModel.getCspid(),messageModel.getCsptoken(),date));
                headers.set("User-Agent","SP/"+messageModel.getChatbotURI());
                headers.set("X-3GPP-Intended-Identity",messageModel.getChatbotURI());
                HttpEntity<String> entity=new HttpEntity<>(headers);
//                httpsTemplate.exchange(fileInfo.getData().get("url"), HttpMethod.GET,entity,String.class);
                ResponseEntity<byte[]> exchange = restTemplate.exchange("http://localhost:8888/testDownloadFile", HttpMethod.GET, entity, byte[].class);
                byte[] body=exchange.getBody();
                HttpStatus code = exchange.getStatusCode();
                int value1 = code.value();
                if(value1==404){
                    return R.ok().message("文件不存在");
                }else if(value1==410){
                    return R.ok().message("文件过期");
                }
                //获取文件名
                HttpHeaders headers1 = exchange.getHeaders();
                List<String> list = headers1.get("Content-Disposition");
                String s2 = list.get(0);
                String[] split = s2.split(";");
                String filename=null;
                for (String value : split) {
                    if (value.trim().startsWith("filename")) {
                        filename = value.substring(value.indexOf('=') + 1).trim();
                    }
                    if(StringUtils.hasText(filename)){
                        if(body!=null){
                            String filePath="./FILE/CHATBOT/"+filename;
                            File dest = new File(filePath);
                            // 检测是否存在目录
                            if (dest.exists()) {
                                dest.delete();
                            }

                            out = new FileOutputStream(dest);
                            out.write(body,0,body.length);
                            out.flush();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return R.error();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return R.ok();
    }

    @RequestMapping("suggestReplyMsg")
    @ApiOperation(value = "用户点击建议回复上行回复消息")
    public R suggestReplyMsg(HttpServletRequest request){
        //请求头获取主叫地址
        String address = request.getHeader("Address");
        BufferedReader reader=null;
        SXMessage sxMessage=null;
        try {
            reader=request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            sxMessage = XmlToBean.xmlToSXMessage(s.toString());
            List<String> link = sxMessage.getLink();
            String rel = link.get(0);
            String href = link.get(1);
            System.out.println("address："+address+"    "+sxMessage.toString());
            //TODO 先保存数据  再转发到机器人处理（业务）
            //建议回复消息的json数据
            String bodyText = sxMessage.getBodyText();
            System.out.println(bodyText);
            //TODO 处理逻辑
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }

    @RequestMapping("suggestActionMsg")
    @ApiOperation(value = "用户点击建议操作上行回复消息")
    public R suggestActionMsg(HttpServletRequest request){
        //请求头获取主叫地址
        String address = request.getHeader("Address");
        BufferedReader reader=null;
        SXMessage sxMessage=null;
        try {
            reader=request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            sxMessage = XmlToBean.xmlToSXMessage(s.toString());
            List<String> link = sxMessage.getLink();
            String rel = link.get(0);
            String href = link.get(1);
            System.out.println("address："+address+"    "+sxMessage.toString());
            //TODO 先保存数据  再转发到机器人处理（业务）
            //建议回复消息的json数据
            String bodyText = sxMessage.getBodyText();
            System.out.println(bodyText);
            //TODO 处理逻辑
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }

}
