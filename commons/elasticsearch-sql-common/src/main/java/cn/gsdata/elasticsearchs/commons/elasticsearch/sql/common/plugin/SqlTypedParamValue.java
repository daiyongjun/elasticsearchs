package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.plugin;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ConstructingObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;

import java.io.IOException;

public class SqlTypedParamValue implements ToXContent {

    private static final ConstructingObjectParser<SqlTypedParamValue, Void> PARSER = null;
    //=
    //new ConstructingObjectParser<>("params", true, objects ->
    //        new SqlTypedParamValue(
    //                objects[0],
    //                DataType.fromEsType((String) objects[1])));

    private static final ParseField VALUE = new ParseField("value");
    private static final ParseField TYPE = new ParseField("type");

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {

        return null;
    }
}
