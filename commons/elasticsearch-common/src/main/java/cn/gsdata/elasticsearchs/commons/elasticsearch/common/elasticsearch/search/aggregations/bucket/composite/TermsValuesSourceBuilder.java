package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.composite;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * @author daiyongjun
 */
public class TermsValuesSourceBuilder extends CompositeValuesSourceBuilder<TermsValuesSourceBuilder> {
    static final String TYPE = "terms";

    private static final ObjectParser<Void, TermsValuesSourceBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(TermsValuesSourceBuilder.TYPE);
        declareValuesSourceFields(PARSER, null);
    }

    static TermsValuesSourceBuilder parse(String name, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new TermsValuesSourceBuilder(name));
    }

    public TermsValuesSourceBuilder(String name) {
        super(name);
    }


    @Override
    protected void doXContentBody(XContentBuilder builder) throws IOException {
    }

    @Override
    String type() {
        return TYPE;
    }
}
