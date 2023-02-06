package cn.gsdata.elasticsearchs.open.sql.elasticsearch.mapping;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.cluster.metadata.MappingMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Collections.emptyMap;

/**
 * Elasticsearch索引映射。因为对于不同的字段类型没有特定的行为，所以使用string来表示字段类型。如常见的数据库表mapping
 * id -> int, name -> String .......这里的id和int都是字符串类型，无需其他类型
 *
 * @author daiyongjun
 */
public class IndexMapping {
    /**
     * id -> int, name -> String .......这里的id和int都是字符串类型，无需其他类型
     */
    private final Map<String, String> fieldMappings;

    public IndexMapping(Map<String, String> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public IndexMapping(MappingMetadata metaData) {
        this.fieldMappings = flatMappings(metaData.getSourceAsMap());
    }

    private Map<String, String> flatMappings(Map<String, Object> indexMapping) {
        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();

        flatMappings(
                ((Map<String, Object>) indexMapping.getOrDefault("properties", emptyMap())),
                "", builder::put);
        return builder.build();
    }

    private void flatMappings(Map<String, Object> mappings, String path, BiConsumer<String, String> func) {
        mappings.forEach((fieldName, mappingObject) -> {
            Map<String, Object> mapping = (Map<String, Object>) mappingObject;
            String fullFieldName = path.isEmpty() ? fieldName : path + "." + fieldName;
            if (isMultiField(mapping)) {
                //Array
                func.accept(fullFieldName, "text_keyword");
            } else {
                //Object
                String type = (String) mapping.getOrDefault("type", "object");
                func.accept(fullFieldName, type);
            }
            if (mapping.containsKey("properties")) {
                // Nested field
                flatMappings((Map<String, Object>) mapping.get("properties"), fullFieldName, func);
            }
        });
    }

    /**
     * 数组型映射关系 fields
     *
     * @param mapping Map<String, Object>
     * @return boolean
     */
    private boolean isMultiField(Map<String, Object> mapping) {
        return mapping.containsKey("fields");
    }
}
