package com.jjy.seata.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("my_order")
public class Order {
    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private Integer orderStatus;
    private String subject;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime cancelTime;
}
