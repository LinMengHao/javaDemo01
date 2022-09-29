package com.example.demo01.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.demo01.entity.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.*;
@Slf4j
public class XMLUtil {
    public static void main(String[] args) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("templateID","sasaasa");
        System.out.println(jsonObject.toJSONString());
        String xml = createXml();
        System.out.println(xml);
    }

    /**
     * 构造消息体xml
     * @param map 参数为key，标签为value  其中奇偶将true，false分开
     * @param root 根结点
     * @param xmlns 根结点的属性
     * @return xml字符串
     */
    //纯文本消息体（xml格式）  TODO 日志打印 xml内容
    public static String FixedTemplateXml(Map<String,String> map,String root,String xmlns,String el){
        String xmlStr=null;
        try{
            Document document=DocumentHelper.createDocument();
            //根结点(一级标签)
            Element element = document.addElement(root, xmlns);
            //（二级标签）
            Element outboundIMMessage = element.addElement(el);
            //（三级标签）
            Element serviceCapability = outboundIMMessage.addElement("serviceCapability");

            int count=0;
            for (Map.Entry<String,String> entry:map.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                //一级标签所属
                //电话标签
                if("destinationAddress".equals(value)){
                    StringBuilder stringBuilder1=new StringBuilder();
                    stringBuilder1.append("tel:+86");
                    stringBuilder1.append(key);
                    //address 多个或者一个目标地址时，默认写第一个。
                    if(count==0){
                        Element address = element.addElement("address");
                        address.setText(stringBuilder1.toString());
                        count++;
                    }
                    Element phoneNum = element.addElement(value);
                    phoneNum.setText(stringBuilder1.toString());
                }else if("senderAddress".equals(value)||"clientCorrelator".equals(value)){
                    Element e = element.addElement(value);
                    e.setText(key);
                }
                //二级标签所属下的特殊格式标签
                else if("smsBodyText".equals(value)||"bodyText".equals(value)
                        ||"mmsBodyText".equals(value)||"mmsBodyTextLarge".equals(value)){
                    StringBuilder stringBuilder2=new StringBuilder();
                    stringBuilder2.append("<![CDATA[");
                    stringBuilder2.append(key);
                    stringBuilder2.append("]]>");
                    Element body=outboundIMMessage.addElement(value);
                    body.setText(stringBuilder2.toString());
                }else if(("conversationID".equals(value)||"contributionID".equals(value))
                        &&(!map.containsValue("conversationID")||!map.containsValue("contributionID"))){
                    Element conversationID = outboundIMMessage.addElement("conversationID");
                    conversationID.setText(key);
                    Element contributionID = outboundIMMessage.addElement("contributionID");
                    contributionID.setText(key);
                }else if("shortMessageSupported".equals(value)||"storeSupported".equals(value)
                        ||"multimediaMessageSupported".equals(value)){
                    //参数键，使用奇偶数，区分true和false，也保证键唯一
                    Element element1 = outboundIMMessage.addElement(value);
                    int i = Integer.parseInt(key);
                    if (i%2==0){
                        element1.setText("false");
                    }else {
                        element1.setText("true");
                    }
                }

                //三级标签所属
                else if("capabilityId".equals(value)||"version".equals(value)){
                    Element element1 = serviceCapability.addElement(value);
                    element1.setText(key);
                }else {
                    //二级标签所属(较多)
                    Element addElement = outboundIMMessage.addElement(value);
                    addElement.setText(key);
                }


                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("UTF-8");
                File file = new File("/Users/yoca-391/Documents/demo01/xmlfiles/test1.xml");
                XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
                writer.setEscapeText(false);
                writer.write(document);
                writer.close();
                // 生成xml字符串
                StringWriter sw = new StringWriter();
                XMLWriter xmlWriter = new XMLWriter(sw, format);
                // 设置是否转义，默认使用转义字符
                xmlWriter.setEscapeText(false);
                xmlWriter.write(document);
                xmlWriter.close();
                xmlStr=sw.toString();
            }
        }catch (Exception e){
            //生成错误
            e.printStackTrace();
            //TODO 日志
        }
        return xmlStr;
    }
    //FixedTemplateXml方法参数测试
    @Test
    public void test1(){
        String uuid321= UUIDUtil.getUUID32();
        String uuid322 = UUIDUtil.getUUID32();
        System.out.println(uuid321+"   "+uuid322);
        Map<String,String> map=new HashMap<>();
        map.put("19585550104","destinationAddress");
        map.put("19585550103","destinationAddress");
        map.put("chatbotURI","senderAddress");
        map.put("12520040","clientCorrelator");
        map.put("chatbotSA","capabilityId");
        map.put("+g.gsma.rcs.botversion=&quot;#=1&quot;","version");
        map.put("text/plain","contentType");
        map.put("{\n" +
                "    \"message\": {\n" +
                "        \"generalPurposeCard\": {\n" +
                "            \"content\": {\n" +
                "                \"description\": \"这是卡片正文\",\n" +
                "                \"media\": {\n" +
                "                    \"height\": \"MEDIUM_HEIGHT\",\n" +
                "                    \"mediaContentType\": \"image/jpeg\",\n" +
                "                    \"mediaFileSize\": \"20875\",\n" +
                "                    \"mediaUrl\": \"https://http01.hn.rcs.chinamobile.com:9091/Access/PF?ID=MDkxNjk2NjYxRDZBQ0I2NkI4QjRENkNERTU3QkEyMkExNkU0NjMzRkU2RDgxQ0NFMkExQzgyOEVDNjA4OERENzY5RjdDQjM4ODE5RTUzRjAyMzgwMDZDNzY4NkJDOTZF\",\n" +
                "                    \"thumbnailContentType\": \"image/jpeg\",\n" +
                "                    \"thumbnailFileSize\": \"10036\",\n" +
                "                    \"thumbnailUrl\": \"https://http01.hn.rcs.chinamobile.com:9091/Access/PF?ID=NDFEMEU4RjUyQ0E2NTVERUJDNERCMjJBREFGMzdGNzYxOUUxQUNGRTgyQzRFOUZFQUZCNzAzNThDQjExMkM2MTAxQUI0QTcwNzYxQzQwODREODIzMThERUYwOTg2NzY2\"\n" +
                "                },\n" +
                "                \"suggestions\": [\n" +
                "                    {\n" +
                "                        \"action\": {\n" +
                "                            \"displayText\": \"打开链接\",\n" +
                "                            \"postback\": {\n" +
                "                                \"data\": \"1536311810897088512\"\n" +
                "                            },\n" +
                "                            \"urlAction\": {\n" +
                "                                \"openUrl\": {\n" +
                "                                    \"application\": \"webview\",\n" +
                "                                    \"url\": \"https://rcs.10086.cn/\"\n" +
                "                                }\n" +
                "                            }\n" +
                "                        }\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"action\": {\n" +
                "                            \"mapAction\": {\n" +
                "                                \"requestLocationPush\": {}\n" +
                "                            },\n" +
                "                            \"displayText\": \"上报当前位置\",\n" +
                "                            \"postback\": {\n" +
                "                                \"data\": \"set_by_chatbot_request_location_push\"\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"title\": \"这是卡片标题\"\n" +
                "            },\n" +
                "            \"layout\": {\n" +
                "                \"cardOrientation\": \"VERTICAL\",\n" +
                "                \"cardWidth\": \"MEDIUM_WIDTH\",\n" +
                "                \"imageAlignment\": \"LEFT\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}","bodyText");
        map.put(uuid321,"conversationID");
        map.put(uuid322,"contributionID");
        map.put("Delivered","reportRequest");
        map.put("Failed","reportRequest");
        map.put("Interworking","reportRequest");
        map.put("1","storeSupported");
        map.put("2","shortMessageSupported");
        map.put("给你发送了一则测试阅信 rcs.10086.cn/82pYTd","smsBodyText");
        String xml = FixedTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1","outboundIMMessage");
        System.out.println(xml);
    }
    /**
     * //TODO 利用反射机制，做通用优化
     * @param list file文件列表
     * @param root 根结点
     * @param xmlns
     * @return
     */
    public static String FileTemplateXml(List<FileInfo> list,String root,String xmlns){
        String xmlStr=null;
        try {
            Document document = DocumentHelper.createDocument();
            Element files = document.addElement(root, xmlns);
            for (int i = 0; i < list.size(); i++) {
                FileInfo fileInfo=list.get(i);
                Element file = files.addElement("file-info");
                if(StringUtils.hasText(fileInfo.getType())){
                    file.addAttribute("type",fileInfo.getType());
                }

                if(StringUtils.hasText(fileInfo.getFileSize())){
                    Element file_size = file.addElement("file-size");
                    file_size.setText(fileInfo.getFileSize());
                }

                if(StringUtils.hasText(fileInfo.getFileName())){
                    Element file_name = file.addElement("file-name");
                    file_name.setText(fileInfo.getFileName());
                }

                if(StringUtils.hasText(fileInfo.getContentType())){
                    Element content_type = file.addElement("content-type");
                    content_type.setText(fileInfo.getContentType());
                }

                Map<String, String> map = fileInfo.getData();
                if(map.containsKey("url")&&StringUtils.hasText(map.get("url"))){
                    Element data = file.addElement("data");
                    data.addAttribute("url",map.get("url"));
                    if (map.containsKey("until")&&StringUtils.hasText(map.get("until"))){
                        data.addAttribute("until",map.get("until"));
                    }
                }

            }
            xmlStr = getXmlStr(document, "UTF-8", false,false,"/Users/yoca-391/Documents/demo01/xmlfiles/test1.xml");
        }catch (Exception e){
            e.printStackTrace();
        }
        return xmlStr;
    }
    //FileTemplateXml测试
    @Test
    public void test2(){
        List<FileInfo> fileInfos=new ArrayList<>();
        FileInfo info1=new FileInfo();
        info1.setContentType("image/jpeg");
        info1.setFileSize("15191");
        info1.setType("thumbnail");
        Map<String, String> data = info1.getData();
        data.put("url","https://http01.hn.rcs.chinamobile.com:9091/Access/PF?ID=QzYzNTE5Njc2NDUzMzhDQTk3OUMzQzQxQTkwN0ZCNjQyOTFGMjU4OTlFOURFREE4NTAyN0IxNjcyOUZEQTBFNjNEQTQ0M0E5OENCQjE4MzdGQzQzRkJENkM0RjQwOTE0");
        data.put("until","2022-08-12T23:59:59Z");

        FileInfo info2=new FileInfo();
        info2.setContentType("video/mp4");
        info2.setFileSize("1482235");
        info2.setType("file");
        info2.setFileName("mda-merqtf8xju5x91gn.mp4");
        Map<String, String> data2 = info2.getData();
        data2.put("url","https://http01.hn.rcs.chinamobile.com:9091/Access/PF?ID=NjJBNDEyNURGMzc1NjgxNTZBMzdFOERFQ0M5NDE4QzlEODc2MTRCNkM1NDlEQzhEQkZGQjBBQ0YxQ0MyMTZGQ0UyMzk5NkNGRTk3NTg5QURGRjg1RjBFRTkxNDI3QzZD");
        data2.put("until","2022-08-12T23:59:59Z");
        fileInfos.add(info1);
        fileInfos.add(info2);
        FileTemplateXml(fileInfos,"file","urn:gsma:params:xml:ns:rcs:rcs:fthttp");
    }

    /**
     * 构造消息体xml
     * @param map 参数为value，标签为key  其中奇偶将true，false分开
     * @param root 根结点
     * @param xmlns 根结点的属性
     * @return xml字符串
     */
    //纯文本消息体（xml格式）  TODO 日志打印 xml内容
    public static String txtTemplateXml(Map<String,String> map,String root,String xmlns,String el){
        String xmlStr=null;
        try{
            Document document=DocumentHelper.createDocument();
            //根结点(一级标签)
            Element element = document.addElement(root, xmlns);
            //（二级标签）
            Element outboundIMMessage = element.addElement(el);
            //（三级标签）
            Element serviceCapability = outboundIMMessage.addElement("serviceCapability");

            int count=0;
            for (Map.Entry<String,String> entry:map.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                //一级标签所属
                //电话标签,值为电话号码，多个号码时，用逗号隔开
                if("destinationAddress".equals(key)){
                    if(value.indexOf(",")!=-1){
                        String[] split = value.split(",");
                        //address 多个或者一个目标地址时，默认写第一个。
                        Element address = element.addElement("address");
                        address.setText("tel:+86"+split[0]);
                        for (int i = 0; i < split.length; i++) {
                            Element phoneNum = element.addElement(key);
                            phoneNum.setText("tel:+86"+split[i]);
                        }
                    }else if(StringUtils.hasText(value)){
                        Element address = element.addElement("address");
                        address.setText("tel:+86"+value);
                        Element phoneNum = element.addElement(key);
                        phoneNum.setText("tel:+86"+value);
                    }
                }else if("senderAddress".equals(key)||"clientCorrelator".equals(key)){
                    Element e = element.addElement(key);
                    e.setText(value);
                }else if("destinationAddress".equals(key)){
                    if(value.indexOf(",")!=-1){
                        String[] split = key.split(",");
                        //address 多个或者一个目标地址时，默认写第一个。
                        Element address = element.addElement("address");
                        address.setText("tel:+86"+split[0]);
                        for (int i = 0; i < split.length; i++) {
                            Element phoneNum = element.addElement(key);
                            phoneNum.setText("tel:+86"+split[i]);
                        }
                    }else if(StringUtils.hasText(value)){
                        Element address = element.addElement("address");
                        address.setText("tel:+86"+value);
                        Element phoneNum = element.addElement(key);
                        phoneNum.setText("tel:+86"+value);
                    }
                }else if("senderAddress".equals(key)||"clientCorrelator".equals(key)){
                    Element e = element.addElement(key);
                    e.setText(value);
                }
                //二级标签所属下的特殊格式标签
                else if("smsBodyText".equals(key)||"bodyText".equals(key)
                        ||"mmsBodyText".equals(key)||"mmsBodyTextLarge".equals(key)){
                    StringBuilder stringBuilder2=new StringBuilder();
                    stringBuilder2.append("<![CDATA[");
                    stringBuilder2.append(value);
                    stringBuilder2.append("]]>");
                    Element body=outboundIMMessage.addElement(key);
                    body.setText(stringBuilder2.toString());
                }else if("reportRequest".equals(key)){
                    if(value.indexOf(",")!=-1){
                        String[] split = value.split(",");
                        for (int i = 0; i < split.length; i++) {
                            Element phoneNum = element.addElement(key);
                            phoneNum.setText(split[i]);
                        }
                    }else if(StringUtils.hasText(value)){
                        Element phoneNum = element.addElement(key);
                        phoneNum.setText(value);
                    }
                } else if(("conversationID".equals(key)||"contributionID".equals(key))
                        &&(!map.containsValue("conversationID")||!map.containsValue("contributionID"))){
                    Element conversationID = outboundIMMessage.addElement("conversationID");
                    conversationID.setText(value);
                    Element contributionID = outboundIMMessage.addElement("contributionID");
                    contributionID.setText(value);
                }//else if("shortMessageSupported".equals(key)||"storeSupported".equals(key)
//                        ||"multimediaMessageSupported".equals(key)){
//                    //参数键，使用奇偶数，区分true和false，也保证键唯一
//                    Element element1 = outboundIMMessage.addElement(key);
//                    int i = Integer.parseInt(value);
//                    if (i%2==0){
//                        element1.setText("false");
//                    }else {
//                        element1.setText("true");
//                    }
//                }

                //三级标签所属
                else if("capabilityId".equals(key)||"version".equals(key)){
                    Element element1 = serviceCapability.addElement(key);
                    element1.setText(value);
                }else {
                    //二级标签所属(较多)
                    Element addElement = outboundIMMessage.addElement(key);
                    addElement.setText(value);
                }
                xmlStr = getXmlStr(document, "UTF-8", false);
            }
        }catch (Exception e){
            //生成错误
            e.printStackTrace();
            //TODO 日志
            log.info("组包出错：缺少参数");
        }
        return xmlStr;
    }
    //TxtTemplateXml方法测试
    @Test
    public void test3(){
        Map<String,String> map=new HashMap<>();
        map.put("destinationAddress","18756232770,18756232771,18756232772,18756232773");
        map.put("reportRequest","Delivered,Failed,Interworking");
        map.put("senderAddress","sip%3A12599%40botplatform.rcs.chinamobile.com");
        map.put("conversationID",UUIDUtil.getUUID32());
        map.put("capabilityId","ChatbotSA");
        map.put("version","+g.gsma.rcs.botversion=&quot;#=1&quot;");
        map.put("contentType","text/plain");
        map.put("bodyText","Hello world");
        map.put("clientCorrelator","567895");
        System.out.println(map.toString());
        String xml = txtTemplateXml(map, "msg:outboundMessageRequest", "urn:oma:xml:rest:netapi:messaging:1", "outboundIMMessage");
        System.out.println(xml);
    }

    /**
     * 构造消息体xml,状态报告报文
     * @param map
     * @param root 根结点
     * @param xmlns 根结点的属性
     * @return xml字符串
     */
    //纯文本消息体（xml格式）  TODO 日志打印 xml内容
    public static String notifyTemplateXml(Map<String,String> map,String root,String xmlns,String el){
        String xmlStr=null;
        try{
            Document document=DocumentHelper.createDocument();
            //根结点(一级标签)
            Element element = document.addElement(root, xmlns);
            //（二级标签）
            Element outboundIMMessage = element.addElement(el);

            int count=0;
            for (Map.Entry<String,String> entry:map.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                //一级标签所属
                //电话标签,值为电话号码，多个号码时，用逗号隔开
                if("description".equals(key)){
                    Element element1 = element.addElement(key);
                    element1.setText(value);
                }else if("rel".equals(key)||"href".equals(key)){
                    Element link = element.addElement("link");
                    link.addAttribute(key,value);
                }
                else {
                    //二级标签所属(较多)
                    Element addElement = outboundIMMessage.addElement(key);
                    addElement.setText(value);
                }
                xmlStr = getXmlStr(document, "UTF-8", false);
            }
        }catch (Exception e){
            //生成错误
            e.printStackTrace();
            //TODO 日志
        }
        return xmlStr;
    }



    /**
     * 返回字符串，并打印xml文件
     * @param document xml创建对象
     * @param code 编码（UTF-8）
     * @param escapeText1 字符串是否转义
     * @param escapeText2 文件是否转义
     * @param path 文件地址
     * @return
     */
    public static String getXmlStr(Document document,String code,boolean escapeText1,boolean escapeText2,String path){
        String xmlStr=null;
        try {
            // 设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");
            // 生成xml文件
            File file = new File("/Users/yoca-391/Documents/demo01/xmlfiles/test1.xml");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            // 生成xml字符串
            StringWriter sw = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(sw, format);
            // 设置是否转义，默认使用转义字符
            xmlWriter.setEscapeText(false);
            xmlWriter.write(document);
            xmlWriter.close();
            xmlStr=sw.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return xmlStr;
    }

    /**
     * 只返回字符串
     * @param document xml创建对象
     * @param code 编码（UTF-8）
     * @param escapeText1 是否转义
     * @return xml字符串
     */
    public static String getXmlStr(Document document,String code,boolean escapeText1){
        String xmlStr=null;
        try {
            // 设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");
            // 生成xml字符串
            StringWriter sw = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(sw, format);
            // 设置是否转义，默认使用转义字符
            xmlWriter.setEscapeText(false);
            xmlWriter.write(document);
            xmlWriter.close();
            xmlStr=sw.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return xmlStr;
    }


    //TODO 文件 卡片...  在bodyText 中插入json 和xml的一些内容

    public static String createXml() {
        try {
            // 1、创建document对象
            Document document = DocumentHelper.createDocument();
            // 2、创建根节点root
            Element msg = document.addElement("msg:outboundMessageRequest","urn:oma:xml:rest:netapi:messaging:1");
            // 3、向msg节点添加name属性
            //msg.addAttribute("name", "test");
            // 4、生成子节点及子节点内容
            Element address = msg.addElement("address");
            address.setText("tel:+8619585550104");
            Element destinationAddress1 = msg.addElement("destinationAddress");
//            destinationAddress1.addAttribute("name","子节点");
            destinationAddress1.setText("tel:+8619585550104");
            Element senderAddress = msg.addElement("senderAddress");
            senderAddress.setText("chatbotUrl");

                Element outboundIMMessage = msg.addElement("outboundIMMessage");
                    Element conversationID = outboundIMMessage.addElement("conversationID");
                    String uuid32 = UUIDUtil.getUUID32();
                    conversationID.setText(uuid32);
                    Element contributionID = outboundIMMessage.addElement("contributionID");
                    contributionID.setText(uuid32);
                    Element serviceCapability = outboundIMMessage.addElement("serviceCapability");
                        Element capabilityId = serviceCapability.addElement("capabilityId");
                        capabilityId.setText("chatbotSA");
                        Element version = serviceCapability.addElement("version");
                        version.setText("+g.gsma.rcs.botversion=&quot;#=1&quot;");
                    Element contentType = outboundIMMessage.addElement("contentType");
                    contentType.setText("text/plain");
                    Element bodyText = outboundIMMessage.addElement("bodyText");
                    bodyText.setText("这是一条纯文本即时消息");
                    Element reportRequest1 = outboundIMMessage.addElement("reportRequest");
                    reportRequest1.setText("Delivered");
                    Element reportRequest2 = outboundIMMessage.addElement("reportRequest");
                    reportRequest2.setText("Failed");
                    Element reportRequest3 = outboundIMMessage.addElement("reportRequest");
                    reportRequest3.setText("Interworking");
                Element clientCorrelator = msg.addElement("clientCorrelator");
                clientCorrelator.setText("567895");


            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");
            // 6、生成xml文件
            File file = new File("/Users/yoca-391/Documents/demo01/xmlfiles/test1.xml");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            // 7、生成xml字符串
            StringWriter sw = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(sw, format);
            // 设置是否转义，默认使用转义字符
            xmlWriter.setEscapeText(false);
            xmlWriter.write(document);
            xmlWriter.close();
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成失败");
        }
        return "fail";
    }

    public static String createXmlTest() {
        try {
            // 1、创建document对象
            Document document = DocumentHelper.createDocument();
            // 2、创建根节点root
            Element root = document.addElement("root");
            // 3、向root节点添加name属性
            root.addAttribute("name", "test");
            // 4、生成子节点及子节点内容
            Element child = root.addElement("child");
            child.addAttribute("name","子节点");
            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");
            // 6、生成xml文件
            File file = new File("/Users/yoca-391/Documents/demo01/xmlfiles/test2.xml");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            // 7、生成xml字符串
            StringWriter sw = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(sw, format);
            // 设置是否转义，默认使用转义字符
            xmlWriter.setEscapeText(false);
            xmlWriter.write(document);
            xmlWriter.close();
            System.out.println("生成test.xml成功");
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成test.xml失败");
        }
        return "fail";
    }



}
