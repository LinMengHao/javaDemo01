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
}
