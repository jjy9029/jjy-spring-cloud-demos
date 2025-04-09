package com.jjy.seata.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient("seata-order")
public interface OrderServiceClient {
    @PutMapping("/seata-order/update/{id}")
    public boolean updateOrderById(@PathVariable("id") int id);
}
