package com.jjy.seata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjy.seata.domain.Order;
import com.jjy.seata.mapper.OrderMapper;
import com.jjy.seata.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public boolean updateOrder(Order order) {
        boolean b = updateById(order);
        return b;
    }
}
