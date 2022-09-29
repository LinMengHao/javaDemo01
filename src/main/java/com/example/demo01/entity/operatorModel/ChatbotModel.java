package com.example.demo01.entity.operatorModel;

import java.io.Serializable;
//机器人信息实体类
public class ChatbotModel implements Serializable {
    //Chatbot ID，包含域名部分
    private String chatbotId;
    /*
        11-新增审核不通过
        12-变更审核不通过
        20-管理平台新增审核中
        21-管理平台变更审核中
        24-上架审核中
        25-上架审核不通过
        26调试白名单审核
        27-调试白名单审核不通过
        30-在线
        31-已下线
        40-暂停
        41-黑名单
        42-已下线（关联的CSP被下线）
        50-调试
     */
    private String status;

    public ChatbotModel() {
    }

    public String getChatbotId() {
        return chatbotId;
    }

    public void setChatbotId(String chatbotId) {
        this.chatbotId = chatbotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
