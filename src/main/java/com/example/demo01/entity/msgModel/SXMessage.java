package com.example.demo01.entity.msgModel;

import com.example.demo01.entity.FileInfo;

import java.util.ArrayList;
import java.util.List;
//上行消息实体类
public class SXMessage {
    //要接收消息通知的目的地址To
    String destinationAddress;
    //消息发送方地址From。地址格式为TEL URI或SIP URI
    String senderAddress;
    //原始消息发送方PAI。地址格式为TEL URI或SIP URI
    String origUser;
    //可选字段，指向以创建资源的URL。
    //路径中最后一节是消息的messageId。
    String resourceURL;
    //可选字段，对应源消息resourceURL
    //第一位数据是标签的属性rel的内容 第二位是href的内容
    List<String> link=new ArrayList<>();
    //消息ID，采用UUID算法生成。
    //上行消息该参数由终端生成
    String messageId;
    String contentType;
    String capabilityId;
    String version;
    //消息内容
    String bodyText;
    String conversationID;
    String contributionID;
    //上行文件内容列表
    List<FileInfo>fileInfos=new ArrayList<>();

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public SXMessage() {
    }

    public SXMessage(String destinationAddress, String senderAddress, String origUser, String resourceURL, List<String> link, String messageId, String contentType, String capabilityId, String version, String bodyText, String conversationID, String contributionID) {
        this.destinationAddress = destinationAddress;
        this.senderAddress = senderAddress;
        this.origUser = origUser;
        this.resourceURL = resourceURL;
        this.link = link;
        this.messageId = messageId;
        this.contentType = contentType;
        this.capabilityId = capabilityId;
        this.version = version;
        this.bodyText = bodyText;
        this.conversationID = conversationID;
        this.contributionID = contributionID;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getOrigUser() {
        return origUser;
    }

    public void setOrigUser(String origUser) {
        this.origUser = origUser;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public List<String> getLink() {
        return link;
    }

    public void setLink(List<String> link) {
        this.link = link;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getContributionID() {
        return contributionID;
    }

    public void setContributionID(String contributionID) {
        this.contributionID = contributionID;
    }

    @Override
    public String toString() {
        return "SXMessage{" +
                "destinationAddress='" + destinationAddress + '\'' +
                ", senderAddress='" + senderAddress + '\'' +
                ", origUser='" + origUser + '\'' +
                ", resourceURL='" + resourceURL + '\'' +
                ", link=" + link +
                ", messageId='" + messageId + '\'' +
                ", contentType='" + contentType + '\'' +
                ", capabilityId='" + capabilityId + '\'' +
                ", version='" + version + '\'' +
                ", bodyText='" + bodyText + '\'' +
                ", conversationID='" + conversationID + '\'' +
                ", contributionID='" + contributionID + '\'' +
                ", fileInfos=" + fileInfos +
                '}';
    }
}
