package com.jjy.seata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jjy.seata.domain.Order;

public interface OrderService extends IService<Order> {
    public boolean updateOrder(Order order);
}
