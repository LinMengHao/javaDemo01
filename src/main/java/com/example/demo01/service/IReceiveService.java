package com.example.demo01.service;

import com.example.demo01.common.R;
import com.example.demo01.entity.xmlToBean.Messages;

public interface IReceiveService {
    /**
     * 解析报文，转发给机器人
     * @param messages 状态报告报文
     * @return
     */
    public abstract R notifyStatus(Messages messages);

    /**
     * 解析用于5G消息接入层向Chatbot发送上行消息
     * @param messages
     * @return
     */
    public abstract R receiveMsg(Messages messages);
}
