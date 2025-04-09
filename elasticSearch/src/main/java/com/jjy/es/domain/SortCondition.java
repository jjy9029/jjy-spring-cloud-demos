package com.jjy.es.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.sort.SortOrder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SortCondition {
    private String fieldName;
    private SortOrder sortOrder;
}
