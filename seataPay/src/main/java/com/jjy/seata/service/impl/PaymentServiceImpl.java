package com.jjy.seata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjy.seata.client.OrderServiceClient;
import com.jjy.seata.domain.Payment;
import com.jjy.seata.mapper.PaymentMapper;
import com.jjy.seata.service.PaymentService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Override
    @GlobalTransactional
    public boolean updatePayment(Payment payment) {
        orderServiceClient.updateOrderById(1);
        for (int i = 0; i <1 ; i++) {
            System.out.println(1);
        }
        if(true){
            throw new RuntimeException("guyi");
        }
        boolean  res = updateById(payment);
        return  res;
    }


}
