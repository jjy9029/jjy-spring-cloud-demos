package com.jjy.es;

import cn.hutool.json.JSONUtil;
import com.jjy.es.utils.ElasticHelper;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
        Map<String, Object> result = elasticHelper.searchAllQuery(indexName, from, size, null, "info");
        List<String> sources = (List<String>) result.get("sources");
        List<String> highlights = (List<String>) result.get("highlights");
        System.out.println(sources);
        System.out.println(highlights);
    }


}
