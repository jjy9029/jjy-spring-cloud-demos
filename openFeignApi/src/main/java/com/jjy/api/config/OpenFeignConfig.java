package com.jjy.api.config;
import com.jjy.api.client.fallback.OpenFeignProdiverClientFallback;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@Import({FallbackBeanConfig.class})
public class OpenFeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }
    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 获取登录用户
//                Long userId = UserContext.getUser();
//                if(userId == null) {
//                    // 如果为空则直接跳过
//                    return;
//                }
//                // 如果不为空则放入请求头中，传递给下游微服务
//                template.header("user-info", userId.toString());、
                return;
            }
        };
    }


}
