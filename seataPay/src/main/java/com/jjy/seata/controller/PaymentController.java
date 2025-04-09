package com.jjy.seata.controller;

import com.jjy.seata.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seata-pay")
public class PaymentController {
   @Autowired
    private PaymentService paymentService;
}
