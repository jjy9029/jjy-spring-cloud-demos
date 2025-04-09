package com.jjy.es;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.jjy.es.domain.Item;
import org.HdrHistogram.Histogram;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticSearchTest {
    private RestHighLevelClient client;



    @BeforeEach
    void setUp() {
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://localhost:9200")));
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
    }

    @Test
    void testInsert() throws IOException {
        IndexRequest request = new IndexRequest("", "", "").id("");
        request.source("{}", XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void testGet() throws IOException {
        GetRequest request = new GetRequest("jjy_test_elastic", "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSource());
    }

    @Test
    void testUpdate() throws IOException {
        UpdateRequest request = new UpdateRequest("", "");
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
    }

    @Test
    void testDelete() throws IOException {
        DeleteRequest request = new DeleteRequest("", "");
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        delete.getResult();
    }

    @Test
    void testSearch() throws IOException {
        SearchRequest request = new SearchRequest("jjy_test_elastic");
        request.source().query(QueryBuilders.matchAllQuery());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must();
        boolQueryBuilder.must();
        request.source().query(boolQueryBuilder);
        request.source().from();
        request.source().size();
        request.source().highlighter(SearchSourceBuilder.highlight().field("info"));
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        // 总条数
        long total = hits.getTotalHits().value;
        //
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsMap());
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                HighlightField field = highlightFields.get("info");
                String string = field.getFragments()[0].toString();
            }
        }
    }


    @Test
    void testBoolQuery(List<Map.Entry<String, String>> mustQuery,
                       List<Map.Entry<String, String>> shoudQuery,
                       List<Map.Entry<String, String>> mustNotQuery,
                       List<Map.Entry<String, String>> filterQuery
    ) throws IOException {
        SearchRequest request = new SearchRequest("jjy_test_elastic");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (mustQuery != null && mustQuery.size() > 0) {
            for (Map.Entry<String, String> entry : mustQuery) {
                boolQueryBuilder.must(
                        QueryBuilders.matchQuery(entry.getKey(), entry.getValue())
                );
            }
        }

        request.source().query(boolQueryBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        if (hits.getHits().length > 0 && hits != null) {

        }
    }

    @Test
    void testScoreFunctionQuery() {
        SearchRequest request = new SearchRequest("jjy_test_elastic");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(
                QueryBuilders.matchQuery("name", "张三")
        );
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = {
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.matchQuery("name", "张三"),
                        ScoreFunctionBuilders.weightFactorFunction(10f)
                ),

                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        ScoreFunctionBuilders.fieldValueFactorFunction("age")
                                .factor(10f).modifier(FieldValueFactorFunction.Modifier.LOG1P).setWeight(2f)
                )

        };
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, filterFunctionBuilders);
        request.source().query(functionScoreQueryBuilder);
    }

    @Test
    void testAggregation() throws IOException {

    }
}