package com.jjy.es.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 范围查询
 */
public class RangeCondition {
    private String filedName;    // es的字段名
    private Object gtCondition; // 大于条件
    private Object ltCondition; // 小于条件
}
