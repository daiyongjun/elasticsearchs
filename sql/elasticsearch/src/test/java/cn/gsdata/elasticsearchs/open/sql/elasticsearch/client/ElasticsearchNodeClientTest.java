package cn.gsdata.elasticsearchs.open.sql.elasticsearch.client;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.io.Resources;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexAbstraction;
import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.cluster.metadata.Metadata;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElasticsearchNodeClientTest {
    private static final String TEST_MAPPING_FILE = "mappings/accounts.json";

    @Mock(answer = RETURNS_DEEP_STUBS)
    private NodeClient nodeClient;

    @Test
    public void getIndexMappings() throws IOException {
        URL url = Resources.getResource(TEST_MAPPING_FILE);
        String mappings = Resources.toString(url, Charsets.UTF_8);
        String indexName = "test";
        ElasticsearchNodeClient client = mockClient(indexName, mappings);


    }

    /**
     * 基于字符串格式mappings，和索引名称mock出ElasticsearchClient具体实现之一，通过节点客户端实现ES插件（ElasticsearchNodeClient）
     *
     * @param indexName String 索引名称
     * @param mappings  String 定义在resources中的字符串格式mappings
     * @return ClusterService
     */
    private ElasticsearchNodeClient mockClient(String indexName, String mappings) {
        ClusterService clusterService = mockClusterService(indexName, mappings);
        //return new ElasticsearchNodeClient(clusterService, nodeClient);
        return null;
    }

    /**
     * 基于字符串格式mappings，和索引名称mock出clusterService
     *
     * @param indexName String 索引名称
     * @param mappings  String 定义在resources中的字符串格式mappings
     * @return ClusterService
     */
    public ClusterService mockClusterService(String indexName, String mappings) {
        ClusterService mockService = mock(ClusterService.class);
        ClusterState mockState = mock(ClusterState.class);
        Metadata mockMetaData = mock(Metadata.class);

        when(mockService.state()).thenReturn(mockState);
        when(mockState.metadata()).thenReturn(mockMetaData);
        try {
            ImmutableOpenMap.Builder<String, ImmutableOpenMap<String, MappingMetadata>> builder =
                    ImmutableOpenMap.builder();
            ImmutableOpenMap<String, MappingMetadata> metadata;
            if (mappings.isEmpty()) {
                metadata = ImmutableOpenMap.of();
            } else {
                metadata = IndexMetadata.fromXContent(createParser(mappings)).getMappings();
            }
            builder.put(indexName, metadata);
            when(mockMetaData.findMappings(any(), any(), any())).thenReturn(builder.build());
            //IndexNameExpressionResolver使用这个方法检查索引是否存在
            //抛出IndexNotFoundException。
            when(mockMetaData.getIndicesLookup())
                    .thenReturn(ImmutableSortedMap.of(indexName, mock(IndexAbstraction.class)));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to mock cluster service", e);
        }
        return mockService;
    }

    private XContentParser createParser(String mappings) throws IOException {
        return XContentType.JSON
                .xContent()
                .createParser(
                        NamedXContentRegistry.EMPTY, DeprecationHandler.THROW_UNSUPPORTED_OPERATION, mappings);
    }
}
