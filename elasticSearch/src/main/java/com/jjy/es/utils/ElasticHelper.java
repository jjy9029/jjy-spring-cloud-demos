package com.jjy.es.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.jjy.es.domain.*;
import org.apache.http.HttpHost;
import org.apache.lucene.util.automaton.LimitedFiniteStringsIterator;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;


// elasticsearch 的client操作过程太多了，所以封装一个工具类来简化操作
// 只需要传入参数，就可以操作es了
@Component
public class ElasticHelper {
    private String url = "http://localhost:9200";
    private RestHighLevelClient client;

    @PostConstruct
    public void init() {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create(url)));
    }

    @PreDestroy
    public void destroy() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    // 根据id获取文档 id为es给文档的唯一标识一般不用来查询
    public String getDocById(String indexName, String docId) throws IOException {
        GetRequest request = new GetRequest(indexName, docId);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        System.out.println(sourceAsString);
        return sourceAsString;
    }

    // 新增文档
    public boolean createDoc(String indexName, String docId, Object object) throws IOException {
        IndexRequest request = new IndexRequest(indexName).id(docId);
        request.source(JSONUtil.toJsonStr(object), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        if(response.getResult() == DocWriteResponse.Result.CREATED){
            return true;
        }
        return false;
    }

    // 更新文档
    public boolean updateDoc(String indexName, String docId, Map<String, Object> updateDocProperties) throws IOException {
        UpdateRequest request = new UpdateRequest(indexName, docId);
        // 判断是否有需要更新的字段
        if (updateDocProperties != null && updateDocProperties.size() > 0) {
            for (Map.Entry<String, Object> entry : updateDocProperties.entrySet()) {
                request.doc(entry.getKey(), entry.getValue());
            }
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            if (response.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            }
        }
        return false;
    }

    // 删除文档
    public boolean deleteDoc(String indexName, String docId) throws IOException {
        DeleteRequest request = new DeleteRequest(indexName, docId);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        if (response.getResult() == DocWriteResponse.Result.DELETED) {
            return true;
        }
        return false;
    }




    // es的 bool 查询，可以实现复杂的查询条件，包括must、should、filter、mustNot等，这里封装了常用的查询条件
    // 包括match、term、range、bool等子查询条件，可以根据需求灵活使用
    // 高亮功能可以根据需求选择是否开启，如果开启，则会返回匹配到的高亮内容
    public ElasticResult boolQuerySearch(String indexname,String highlightField, int from, int size,
          List<MatchCondition> mustQuery, List<MatchCondition> shouldQuery,
          List<TermCondition> filterTermQuery, List<RangeCondition> filterRangeQuery,
          List<TermCondition> mustNotTermQuery,List<RangeCondition> mustNotRangeQuery) throws IOException {

        SearchRequest request = new SearchRequest(indexname);
        // 添加查询条件
        request = handleSearchRequestQuery(request,
                mustQuery, shouldQuery,
                filterTermQuery, filterRangeQuery,
                mustNotTermQuery, mustNotRangeQuery);

        // 添加高亮条件
        if (highlightField != null && highlightField != ""){
            request = handleSearchRequestHighlight(request, highlightField);
        }

        // 分页条件
        request.source().from(from).size(size);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        ElasticResult elasticResult = handleSearchResponse(response, highlightField);
        return elasticResult;
    }


    // 根据提供的sortFiled和sortOrder排序查询结果集
    // 由于是自己定义排序顺序，那么就不采用es的打分机制,也就不使用 匹配查询
    // 所以 sortFiled和sortOrder是必传参数和一些过滤的条件
    // 参与的排序的一般是精确值
    public ElasticResult sortQuerySearch(String indexname, int from, int size,
                                         List<SortCondition> sortConditions,
                                         List<TermCondition> filterTermQuery,
                                         List<RangeCondition> filterRangeQuery,
                                         List<TermCondition> mustNotTermQuery,
                                         List<RangeCondition> mustNotRangeQuery) throws IOException {

        SearchRequest request = new SearchRequest(indexname);

        // 添加过滤条件 但是打分的匹配条件就传入为null，不进行处理
        request = handleSearchRequestQuery(request,
                null,null,
                filterTermQuery, filterRangeQuery,
                mustNotTermQuery, mustNotRangeQuery);
        // 添加排序条件
        request = handleSearchRequestOrder(request, sortConditions);
        request.source().from(from).size(size);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        ElasticResult elasticResult = handleSearchResponse(response, null);
        return elasticResult;
    }



    // 自定义评分权重函数
    public ElasticResult functionScoreQuerySearch(String indexname, int from, int size,
                                  List<ScoreFunctionCondition> scoreFunctionConditions)throws  IOException{

        SearchRequest request = new SearchRequest(indexname);
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = {};
        if (scoreFunctionConditions != null && scoreFunctionConditions.size() > 0){
            for (int i = 0; i < scoreFunctionConditions.size(); i++){
                ScoreFunctionCondition scoreFunctionCondition = scoreFunctionConditions.get(i);
                if (scoreFunctionCondition.getCondition().getClass().equals(MatchCondition.class)) {

                }else if (scoreFunctionCondition.getCondition().getClass().equals(TermCondition.class)){

                }else if (scoreFunctionCondition.getCondition().getClass().equals(RangeCondition.class)){

                }
            }
        }
        request.source().query(QueryBuilders.functionScoreQuery(filterFunctionBuilders));
        request.source().from(from).size(size);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        ElasticResult elasticResult = handleSearchResponse(response, null);
        return elasticResult;
    }





     //
    //  下面的方法是一些 private 辅助方法，主要是用于上面public函数 实现功能时候的辅助工具方法
    //

    // 这个是处理搜索请求的各种条件 ，需要使用条件就传入对应的list ，没有的的话就传入null
    private SearchRequest handleSearchRequestQuery(SearchRequest request,  // 搜索请求
                                                   List<MatchCondition> mustQuery, // must条件
                                                   List<MatchCondition> shouldQuery, // should条件
                                                   List<TermCondition> filterTermQuery, // filter精确值条件
                                                   List<RangeCondition> filterRangeQuery, // filter范围条件
                                                   List<TermCondition> mustNotTermQuery, // mustNot精确值条件
                                                   List<RangeCondition> mustNotRangeQuery) {  // mustNot范围条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); // 创建bool查询器
        // 添加must条件
        if (mustQuery != null && mustQuery.size() > 0) {
            for (MatchCondition item : mustQuery) {
                boolQueryBuilder.must(
                        QueryBuilders.matchQuery(item.getFieldName(), item.getMatchValue())
                );
            }
        }

        // 添加shoud条件
        if (shouldQuery != null && shouldQuery.size() > 0) {
            for (MatchCondition item : shouldQuery) {
                boolQueryBuilder.should(
                        QueryBuilders.matchQuery(item.getFieldName(), item.getMatchValue())
                );
            }
        }

        // 添加filter精确值条件
        if (filterTermQuery != null && filterTermQuery.size() > 0) {
            for (TermCondition item : filterTermQuery) {
                boolQueryBuilder.filter(
                        QueryBuilders.termQuery(item.getFiledName(), item.getTermValue())
                );
            }
        }

        // 添加filter范围条件
        if (filterRangeQuery != null && filterRangeQuery.size() > 0) {
            for (RangeCondition item : filterRangeQuery) {
                boolQueryBuilder.filter(
                        QueryBuilders.rangeQuery(item.getFiledName())
                                .gt(item.getGtCondition())
                                .lt(item.getLtCondition())
                );
            }
        }

        // 添加mustNot精确值条件
        if (mustNotTermQuery != null && mustNotTermQuery.size() > 0) {
            for (TermCondition item : mustNotTermQuery) {
                boolQueryBuilder.mustNot(
                        QueryBuilders.termQuery(item.getFiledName(), item.getTermValue())
                );
            }
        }

        // 添加mustNot范围条件
        if (mustNotRangeQuery != null && mustNotRangeQuery.size() > 0) {
            for (RangeCondition item : mustNotRangeQuery) {
                boolQueryBuilder.mustNot(
                        QueryBuilders.rangeQuery(item.getFiledName())
                                .gt(item.getGtCondition())
                                .lt(item.getLtCondition())
                );
            }
        }
        // 添加bool查询器到搜索请求中
        request.source().query(boolQueryBuilder);
        return request;
    }


    //给搜索请求添加排序条件 排序条件的顺序和传入的排序条件顺序一致所以要用list遍历
    // list中是 一个个 键值对 key是排序的字段，value是排序的顺序（ASC/DESC）
    private SearchRequest handleSearchRequestOrder(SearchRequest request, List<SortCondition> sortConditions) {
        if (sortConditions != null && sortConditions.size() > 0) {
            for (SortCondition item : sortConditions) {
                request.source().sort(item.getFieldName(), item.getSortOrder());
            }
        }
        return request;
    }

    // 给搜索请求添加高亮条件
    // 要使用highlight方法，指定的highlightField必须是text类型字段 并且要是在查询条件中
    private SearchRequest handleSearchRequestHighlight(SearchRequest request, String highlightField) {
        request.source().highlighter(SearchSourceBuilder.highlight().field(highlightField));
        return request;
    }



    // 处理搜索到的hits结果集 根据需求装载sources和highlights
    // sources是文档的内容，highlights是根据match匹配到高亮内容（不是所有查询都可以产生高亮）
    private ElasticResult handleSearchResponse(SearchResponse response, String highlightField) {
        List<String> sources = new ArrayList<>();
        List<String> highlights = new ArrayList<>();
        SearchHits hits = response.getHits();
        // 判断是否有命中的结果,有结果则遍历装入sources中，并且判断是否有高亮需求，有则装入highlights中
        if (hits != null && hits.getTotalHits().value > 0) {
            for (SearchHit hit : hits) {
                sources.add(hit.getSourceAsString());
                //  判断高亮需求是否存在，存在则获取高亮内容
                if (highlightField != null && highlightField != "") {
                    String highlightContent = hit.getHighlightFields()
                            .get(highlightField).getFragments()[0].toString();
                    highlights.add(highlightContent);
                }
            }
        }

        // 封装ElasticResult对象
        return ElasticResult.builder()
                .sources(sources)
                .highlights(highlights)
                .build();
    }


}
