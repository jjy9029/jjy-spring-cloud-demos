package com.jjy.seata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjy.seata.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
