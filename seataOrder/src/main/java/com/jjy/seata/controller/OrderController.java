package com.jjy.seata.controller;

import com.jjy.seata.domain.Order;
import com.jjy.seata.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seata-order")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PutMapping("/update/{id}")
    public boolean updateOrderById(@PathVariable("id") Integer id){
        Order order = Order.builder().id(1).orderStatus(1).build();
        boolean res = orderService.updateOrder(order);
        return res;
    }

}
