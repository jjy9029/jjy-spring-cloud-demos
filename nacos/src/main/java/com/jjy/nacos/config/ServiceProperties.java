package com.jjy.nacos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


// nacos 配置热更新的属性类，这些变量在nacos的同服务名的yml中设置值，这对象的属性值就会相应变化
@Data
@Component
@ConfigurationProperties(prefix = "jjy")// 前缀
public class ServiceProperties {
    private String username;  // 这个对应的是   nacos中 mynacosTest-dev.yaml 中的jjy.username
}




