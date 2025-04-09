package com.jjy.openFeign;
import com.jjy.api.config.FallbackBeanConfig;
import com.jjy.api.config.OpenFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(value = "com.jjy.api.client",defaultConfiguration = {OpenFeignConfig.class})
// 包名为api模块中存放FeignClient接口的包名
@SpringBootApplication
public class openFeignConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(openFeignConsumerApplication.class, args);
    }
}