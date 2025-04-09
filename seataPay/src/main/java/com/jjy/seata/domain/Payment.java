package com.jjy.seata.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @TableId
    private Integer id;
    private Integer orderId;
    private Double amount;
    private  String subject;
    private LocalDateTime payTime;
    private Integer payStatus;

}
