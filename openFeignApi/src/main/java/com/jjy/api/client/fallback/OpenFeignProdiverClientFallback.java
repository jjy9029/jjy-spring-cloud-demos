package com.jjy.api.client.fallback;

import com.jjy.api.client.OpenFeignProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
// 降级类，当OpenFeignProviderClient接口调用失败时，会调用此类中的create方法，返回一个降级对象
@Slf4j
public class OpenFeignProdiverClientFallback implements FallbackFactory<OpenFeignProviderClient> {
    @Override
    public OpenFeignProviderClient create(Throwable cause) {
        return new OpenFeignProviderClient() {
            @Override
            public String provide() {
                log.info("fallback!!!!");
                return "fallback!!!!";
            }
        };
    }
}
