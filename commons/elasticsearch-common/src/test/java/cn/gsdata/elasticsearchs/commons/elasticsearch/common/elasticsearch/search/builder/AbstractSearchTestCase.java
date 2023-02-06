package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ESTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.SearchModule;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json.JsonXContent;
import org.junit.After;
import org.junit.Before;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public abstract class AbstractSearchTestCase extends ESTestCase {
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

    private NamedXContentRegistry xContentRegistry;

    @Override
    protected NamedXContentRegistry xContentRegistry() {
        return serviceHolder.xContentRegistry;
    }

    protected SearchSourceBuilder parseSearch(String searchAsString, String fieldName) throws IOException {
        XContentParser parser = createParser(JsonXContent.JSON_X_CONTENT, searchAsString);
        return SearchSourceBuilder.fromXContent(parser, fieldName);
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
}
