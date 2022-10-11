package com.example.demo01.service;

import com.example.demo01.common.R;
import com.example.demo01.entity.msgModel.TextMsgModel;

public interface ISendService {
    /**
     * 下行消息(默认)
     * @param msg
     * @return
     */
    public abstract R sendMsg(TextMsgModel msg);

    /**
     * 文本消息
     * @param msg
     * @return
     */
    R sendTxtMsg(TextMsgModel msg);

    /**
     * 卡片消息
     * @param msg
     * @return
     */
    R sendCardMsg(TextMsgModel msg);

    /**
     * 文件消息
     * @param msg
     * @return
     */
    R sendFileMsg(TextMsgModel msg);

    /**
     * 菜单消息
     * @param msg
     * @return
     */
    R sendMenuMsg(TextMsgModel msg);

    /**
     * 交互消息
     * @param msg
     * @return
     */
    R sendContributionMsg(TextMsgModel msg);

    /**
     * 回落消息
     * @param msg
     * @return
     */
    R sendUpMsg(TextMsgModel msg);

    R withdraw(TextMsgModel msg);
}

