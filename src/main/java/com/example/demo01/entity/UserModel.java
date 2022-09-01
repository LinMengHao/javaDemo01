package com.example.demo01.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserModel {
    @Value("${XZKJ.APIKEY}")
    private String apiKey;
    @Value("${XZKJ.APISECRECT}")
    private String apiSecrect;

    @Value("${YD.IP}")
    private String ip;

    @Value("${YD.PORT}")
    private String port;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecrect() {
        return apiSecrect;
    }

    public void setApiSecrect(String apiSecrect) {
        this.apiSecrect = apiSecrect;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
