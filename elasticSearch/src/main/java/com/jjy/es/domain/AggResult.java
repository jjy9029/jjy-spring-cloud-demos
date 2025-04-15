package com.jjy.es.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggResult {
    private String keyName;
    private Object key; // 分组的key值
    private long docCount; // 聚合的doc数量
    private String statsName; // 聚合的字段名称
    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private long count;
}
