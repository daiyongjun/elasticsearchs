package cn.gsdata.elasticsearchs.open.sql.elasticsearch.data.value;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import lombok.Setter;

import java.util.Map;

/**
 * 从Elasticsearch响应构造ExprValue
 *
 * @author daiyongjun
 */
public class ElasticsearchExprValueFactory {

    /**
     * 字段与ExprType的映射。"byteV": (ExprType)BYTE " shortV" : SHORT 等映射关系
     */
    @Setter
    private Map<String, ExprType> typeMapping;

}
