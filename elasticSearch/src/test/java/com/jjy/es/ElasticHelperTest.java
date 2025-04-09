package com.jjy.es;

import cn.hutool.json.JSONUtil;
import com.jjy.es.domain.ElasticResult;
import com.jjy.es.domain.MatchCondition;
import com.jjy.es.domain.SortCondition;
import com.jjy.es.utils.ElasticHelper;
import net.bytebuddy.build.ToStringPlugin;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticHelperTest {
    @Autowired
    private ElasticHelper elasticHelper;

    @Test
    void testGetDocById() throws IOException {
        String indexName ="jjy_test_elastic";
        String docId = "1";
        elasticHelper.getDocById(indexName,docId);
    }

    @Test
    void testUpdateDoc() throws IOException {
        String indexName ="jjy_test_elastic";
        String docId = "1";
        Map<String,Object> updateProperties =  new HashMap<>();
        Map<String,String> nameProperty = new HashMap<>();
        nameProperty.put("lastName","jjjjj");
        updateProperties.put("name",nameProperty);
        elasticHelper.updateDoc(indexName,docId,updateProperties);
    }


    @Test
    void testDeleteDoc() throws IOException {
        String indexName ="jjy_test_elastic";
        String docId = "1";
        boolean deleted = elasticHelper.deleteDoc(indexName, docId);
        System.out.println(deleted);
    }

    @Test
    void testCreateDoc(){

    }

    @Test
    void testSearchAllQuery() throws IOException {
        String indexName = "jjy_test_elastic";
        int from = 0;
        int size = 10;
    }

    @Test
    void testBoolMatchQuery() throws IOException {
        String indexName = "jjy_test_elastic";
        String highlightField = "info";
        int from = 0;
        int size = 3;
        List<MatchCondition> mustQuery = new ArrayList<>();
        mustQuery.add(new MatchCondition("info","牛奶"));
        ElasticResult elasticResult = elasticHelper.boolQuerySearch(indexName, highlightField, from, size,
                mustQuery, null,
                null, null,
                null, null);
        System.out.println(elasticResult);
    }

    @Test
    void testSortQuery() throws IOException {
        String indexName = "jjy_test_elastic";
        int from = 0;
        int size = 3;
        List<SortCondition> sortConditions = new ArrayList<>();
        sortConditions.add(new SortCondition("age", SortOrder.DESC));
        ElasticResult elasticResult = elasticHelper.sortQuerySearch(indexName, from, size, sortConditions,
                null, null,
                null, null);
        System.out.println(elasticResult);
    }



}
