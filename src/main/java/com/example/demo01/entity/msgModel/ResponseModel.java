package com.example.demo01.entity.msgModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

//响应解析
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "msg:outboundMessageRequest")
public class ResponseModel {

    @XmlElement(name = "messageId")
    private String messageId;
    @XmlElement(name = "clientCorrelator")
    private String clientCorrelator;

    public ResponseModel() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClientCorrelator() {
        return clientCorrelator;
    }

    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "messageId='" + messageId + '\'' +
                ", clientCorrelator='" + clientCorrelator + '\'' +
                '}';
    }
}
