package com.jjy.seata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jjy.seata.domain.Payment;

public interface PaymentService extends IService<Payment> {
    public boolean updatePayment(Payment payment);
}
