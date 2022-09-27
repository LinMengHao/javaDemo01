package com.example.demo01.controller.gateway;

import com.example.demo01.common.R;
import com.example.demo01.utils.RSAUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("xiuzhi/bj_mobile")
public class OperationGateWayApi {
    @PostMapping("MsgSync")
    public R msgSync(HttpServletRequest request){
        //鉴权
        String authorization = request.getHeader("Authorization").split(" ")[1];
        String timestamp = request.getHeader("Timestamp");
        String requestId = request.getHeader("Request-ID");
        String appId = request.getHeader("App-ID");
        boolean b = RSAUtils.verifyAuthorization(appId, authorization, timestamp, requestId);
        if (!b){
            return R.error();
        }
        return R.ok();
    }
}
