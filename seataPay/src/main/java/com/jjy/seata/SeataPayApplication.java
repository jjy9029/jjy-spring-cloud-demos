package com.jjy.seata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SeataPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeataPayApplication.class, args);
    }
}