package com.example.demo01.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class TestController {
    @RequestMapping("test")
    public String test(){
        return "Holle World";
    }
    @RequestMapping("token")
    public String getToken(){
        return "";
    }
}