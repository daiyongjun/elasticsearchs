package cn.gsdata.elasticsearchs.open.sql.elasticsearch.request;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;

import java.util.function.Function;

/**
 * @author daiyongjun
 */
public interface ElasticsearchRequest {
    //ElasticsearchResponse search(Function<SearchRequest, SearchResponse> searchAction,
    //                             Function<SearchScrollRequest, SearchResponse> scrollAction);
}
