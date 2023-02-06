package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.avg;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AvgAggregationBuilderTests extends AbstractSearchTestCase {

    @Test
    public void avg() throws IOException {
        String query = "{\"aggs\":{\"指标\":{\"avg\":{\"field\":\"score\"}}}}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }

    @Test
    public void script() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"avg_corrected_grade\" : {\n" +
                "            \"avg\" : {\n" +
                "                \"field\" : \"grade\",\n" +
                "                \"script\" : {\n" +
                "                    \"lang\": \"painless\",\n" +
                "                    \"source\": \"_value * params.correction\",\n" +
                "                    \"params\" : {\n" +
                "                        \"correction\" : 1.2\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        assertEquals("{\"aggregations\":{\"avg_corrected_grade\":{\"avg\":{\"field\":\"grade\",\"script\":{\"source\":\"_value * params.correction\",\"lang\":\"painless\",\"params\":\"{correction=1.2}\"}}}}}", searchSourceBuilder.toString());
    }
}
