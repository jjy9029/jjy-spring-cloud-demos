package com.jjy.openFeign.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
public class ProviderController {
    @Value("${server.port}")
    private String port;
    @GetMapping("/get")
    public String provide() throws InterruptedException {
        Thread.sleep(10000);
        return " this is openFeign provider!"+port;
    }
}
