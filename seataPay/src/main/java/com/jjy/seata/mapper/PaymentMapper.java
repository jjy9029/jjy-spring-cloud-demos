package com.jjy.seata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjy.seata.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}
