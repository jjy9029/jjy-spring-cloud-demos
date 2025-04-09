package com.jjy.es.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.jjy.es.domain.MatchCondition;
import com.jjy.es.domain.RangeCondition;
import com.jjy.es.domain.TermCondition;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
    public void createDoc(String indexName, String docId, Object object) throws IOException {
        IndexRequest request = new IndexRequest(indexName).id(docId);
        request.source(JSONUtil.toJsonStr(object), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
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


    public Map<String, String> boolMatchQuery(String indexname,String highlightField,
          List<MatchCondition> mustQuery, List<MatchCondition> shouldQuery,
          List<TermCondition> filterTermQuery, List<RangeCondition> filterRangeQuery,
          List<TermCondition> mustNotTermQuery,List<RangeCondition> mustNotRangeQuery) throws IOException {

        Map<String, String> result = new HashMap<>();
        SearchRequest request = new SearchRequest(indexname);

        request = handleSearchRequestQuery(request,
                mustQuery, shouldQuery,
                filterTermQuery, filterRangeQuery,
                mustNotTermQuery, mustNotRangeQuery);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        handleSearchResponse(response,highlightField);
        return result;
    }

    public Map<String, Object> searchAllQuery(String indexname, int from, int size,
                                              List<Map.Entry<String, SortOrder>> sortsProperty, String highlightField) throws IOException {
        Map<String, Object> result = new HashMap<>();
        List<String> sources = new ArrayList<>();
        List<String> highlights = new ArrayList<>();
        SearchRequest request = new SearchRequest(indexname);
        request.source().query(QueryBuilders.matchAllQuery());
        request.source().from(from).size(size);
        // 排序条件应用 并且为了不影响排序的先后次序使用了list遍历而不是用map
        if (sortsProperty != null && sortsProperty.size() > 0) {
            for (Map.Entry<String, SortOrder> entry : sortsProperty) {
                request.source().sort(entry.getKey(), entry.getValue());
            }
        }
//        if (highlightField != null && !highlightField.equals("")){
//            request.source().highlighter(SearchSourceBuilder.highlight()
//                    .field("info")
//                    .preTags("<em>")
//                    .postTags("</em>"));
//            System.out.println(highlightField);
//            System.out.println("highlight");
//        }
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return handleSearchResponse(response, null);
    }






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
    private SearchRequest handleSearchRequestOrder(SearchRequest request, List<Map.Entry<String, SortOrder>> sortsProperty) {
        if (sortsProperty != null && sortsProperty.size() > 0) {
            for (Map.Entry<String, SortOrder> item : sortsProperty) {
                request.source().sort(item.getKey(), item.getValue());
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
    private Map<String, Object> handleSearchResponse(SearchResponse response, String highlightField) {
        Map<String, Object> result = new HashMap<>();
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
        // 装载结果集 如果没有命中结果，则sources为空，highlights为空
        result.put("sources", sources);
        result.put("highlights", highlights);
        return result;
    }


}
