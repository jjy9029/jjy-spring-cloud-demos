package com.jjy.openFeign.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@FeignClient("openFeignProvider")
public interface TestOpenFeignClient {
    @GetMapping("/provider/get")
    public String provide();
}
