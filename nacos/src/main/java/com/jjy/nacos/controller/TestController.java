package com.jjy.nacos.controller;

import com.jjy.nacos.config.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    ServiceProperties serviceProperties;
    @GetMapping("/test")
    public String test() {
        return serviceProperties.getUsername();
    }
}
