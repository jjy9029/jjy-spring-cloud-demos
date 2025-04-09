package com.jjy.api.client;

import com.jjy.api.client.fallback.OpenFeignProdiverClientFallback;
import com.jjy.api.config.FallbackBeanConfig;
import com.jjy.api.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "openFeignProvider",
        configuration = {OpenFeignConfig.class, FallbackBeanConfig.class},
        fallbackFactory = OpenFeignProdiverClientFallback.class
)// 要被定义成Client的服务的名称
public interface OpenFeignProviderClient {
    @GetMapping("/provider/get")  // 请求方式和路径和服务的接口一样
    public String provide();  // 函数名称和返回值也要相同
}
