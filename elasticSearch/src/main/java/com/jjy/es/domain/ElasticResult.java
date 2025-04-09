package com.jjy.es.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticResult {
    private List<String> sources; // elastic的 hit的 source 字段结果
    private List<String> highlights; // elastic的 hit的 highlight 字段结果
}
