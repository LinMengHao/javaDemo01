package com.example.demo01.com.lmh.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("test")
    public String test(){
        return "Holle World";
    }
}
