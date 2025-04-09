package com.jjy.seata;

import com.jjy.seata.client.OrderServiceClient;
import com.jjy.seata.domain.Payment;
import com.jjy.seata.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SeataPaymentTest {
    @Autowired
    private PaymentService paymentService;


    @Test
    void test(){
        Payment payment = Payment.builder().id(1).amount(101.0).build();
        boolean b = paymentService.updatePayment(payment);
        System.out.println(b);
    }
}
