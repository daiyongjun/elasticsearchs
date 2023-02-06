package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ESTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json.JsonXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.SearchModule;
import org.junit.After;
import org.junit.Before;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertNull;

/**
 * term query 抽象测试类
 *
 * @param <QB>
 */
public class AbstractQueryTestCase<QB extends QueryBuilder> extends ESTestCase {

    private static ServiceHolder serviceHolder;


    @Before
    public void beforeTest() throws IOException {
        System.out.println("初始化资源！");
        if (serviceHolder == null) {
            serviceHolder = new ServiceHolder();
        }
    }

    @After
    public void afterTest() throws IOException {
        if (serviceHolder != null) {
            serviceHolder.close();
        }
    }

    @Override
    protected NamedXContentRegistry xContentRegistry() {
        return serviceHolder.xContentRegistry;
    }

    private static class ServiceHolder implements Closeable {
        private final NamedXContentRegistry xContentRegistry;

        ServiceHolder() throws IOException {
            SearchModule searchModule = new SearchModule();
            xContentRegistry = new NamedXContentRegistry(Stream.of(
                    searchModule.getNamedXContents().stream()
            ).flatMap(Function.identity()).collect(toList()));
        }

        @Override
        public void close() throws IOException {
            System.out.println("关闭初始化资源！");
        }
    }

    protected QB parseQuery(String queryAsString) throws IOException {
        XContentParser parser = createParser(JsonXContent.JSON_X_CONTENT, queryAsString);
        return (QB) parseQuery(parser);
    }

    protected QueryBuilder parseQuery(XContentParser parser) throws IOException {
        //解析QueryBuilder相关类
        QueryBuilder parseInnerQueryBuilder = AbstractQueryBuilder.parseInnerQueryBuilder(parser,"");
        assertNull(parser.nextToken());
        return parseInnerQueryBuilder;
    }
}
