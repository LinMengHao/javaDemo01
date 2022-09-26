package com.example.demo01.controller.gateway;

import com.example.demo01.common.R;
import com.example.demo01.entity.msgModel.TextMsgModel;
import com.example.demo01.utils.AsyncUtils;
import com.example.demo01.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("xiuzhi/bj_mobile")
public class SendMsgGateWayApi {
    @Autowired
    AsyncUtils asyncUtils;

    @PostMapping("MsgSync")
    public R sendMsg(TextMsgModel textMsgModel){
        log.info("线程名称"+Thread.currentThread().getName());
        long l = System.currentTimeMillis();
        String uuid32 = UUIDUtil.getUUID32();
        textMsgModel.setId(uuid32);
        asyncUtils.sendMsgToMQ(textMsgModel);
        long l1 = System.currentTimeMillis();
        log.info("接受到响应耗时：" +(l1-l)+"毫秒");
        return R.ok();
    }
}
