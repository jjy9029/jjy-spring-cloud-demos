package com.jjy.openFeign.controller;

import com.jjy.api.client.OpenFeignProviderClient;
import com.jjy.openFeign.feign.TestOpenFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Autowired
    OpenFeignProviderClient openFeignProviderClient;
    @GetMapping("/get")
    public String consumer(){
        String res = "this is consumer"+"<br/>"+openFeignProviderClient.provide();
        return  res;
    }
}
