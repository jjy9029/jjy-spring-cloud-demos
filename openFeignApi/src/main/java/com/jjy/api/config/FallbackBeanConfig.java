package com.jjy.api.config;

import com.jjy.api.client.fallback.OpenFeignProdiverClientFallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// 在这个配置类当中，定义每个Client失效的fallback类，并将其注入到Spring容器当中。
@Configuration
public class FallbackBeanConfig {
    @Bean
    public OpenFeignProdiverClientFallback openFeignProdiverClientFallback() {
        return new OpenFeignProdiverClientFallback();
    }
}
