package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.composite;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort.SortOrder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValueType;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.*;


import java.io.IOException;

public abstract class CompositeValuesSourceBuilder<AB extends CompositeValuesSourceBuilder<AB>> implements ToXContent {
    public static final ParseField FIELD_NAME = new ParseField("field");
    public static final ParseField MISSING_FIELD_NAME = new ParseField("missing");
    public static final ParseField VALUE_TYPE_NAME = new ParseField("value_type");
    public static final ParseField SCRIPT_FIELD_NAME = new ParseField("script");
    public static final ParseField ORDER_FIELD_NAME = new ParseField("order");

    protected final String name;
    private String field = null;
    //private Script script = null;
    private ValueType valueType = null;
    private Object missing = null;
    private SortOrder order = SortOrder.ASC;

    CompositeValuesSourceBuilder(String name) {
        this.name = name;
    }

    static <Context, Value extends CompositeValuesSourceBuilder<Value>> void declareValuesSourceFields(AbstractObjectParser<Context, Value> objectParser, ValueType targetValueType) {
        objectParser.declareField(Value::field, XContentParser::text, FIELD_NAME, ObjectParser.ValueType.STRING);
        objectParser.declareField(Value::missing, XContentParser::objectText, MISSING_FIELD_NAME, ObjectParser.ValueType.VALUE);
        objectParser.declareField(Value::valueType, p -> {
            ValueType valueType = ValueType.resolveForScript(p.text());
            if (targetValueType != null && valueType.isNotA(targetValueType)) {
                throw new ParsingException(p.getTokenLocation(),
                        "Aggregation [" + objectParser.getName() + "] was configured with an incompatible value type ["
                                + valueType + "]. It can only work on value of type ["
                                + targetValueType + "]");
            }
            return valueType;
        }, VALUE_TYPE_NAME, ObjectParser.ValueType.STRING);

        //objectParser.declareField(Value::script,
        //        (parser, context) -> Script.parse(parser), SCRIPT_FIELD_NAME, ObjectParser.ValueType.OBJECT_OR_STRING);

        objectParser.declareField(Value::order, XContentParser::text, ORDER_FIELD_NAME, ObjectParser.ValueType.STRING);
    }


    public AB field(String field) {
        if (field == null) {
            throw new IllegalArgumentException("[field] must not be null");
        }
        this.field = field;
        return (AB) this;
    }

    public AB missing(Object missing) {
        if (missing == null) {
            throw new IllegalArgumentException("[missing] must not be null");
        }
        this.missing = missing;
        return (AB) this;
    }

    public AB valueType(ValueType valueType) {
        if (valueType == null) {
            throw new IllegalArgumentException("[valueType] must not be null");
        }
        this.valueType = valueType;
        return (AB) this;
    }

    //public AB script(Script script) {
    //    if (script == null) {
    //        throw new IllegalArgumentException("[script] must not be null");
    //    }
    //    this.script = script;
    //    return (AB) this;
    //}

    public AB order(String order) {
        if (order == null) {
            throw new IllegalArgumentException("[order] must not be null");
        }
        this.order = SortOrder.fromString(order);
        return (AB) this;
    }

    public static CompositeValuesSourceBuilder<?> fromXContent(XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();
        XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_OBJECT, token, parser::getTokenLocation);
        token = parser.nextToken();
        XContentParserUtils.ensureExpectedToken(XContentParser.Token.FIELD_NAME, token, parser::getTokenLocation);
        String name = parser.currentName();
        token = parser.nextToken();
        XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_OBJECT, token, parser::getTokenLocation);
        token = parser.nextToken();
        XContentParserUtils.ensureExpectedToken(XContentParser.Token.FIELD_NAME, token, parser::getTokenLocation);
        String type = parser.currentName();
        token = parser.nextToken();
        XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_OBJECT, token, parser::getTokenLocation);
        final CompositeValuesSourceBuilder<?> builder;
        switch (type) {
            case TermsValuesSourceBuilder.TYPE:
                builder = TermsValuesSourceBuilder.parse(name, parser);
                break;
            case DateHistogramValuesSourceBuilder.TYPE:
                builder = DateHistogramValuesSourceBuilder.parse(name, parser);
                break;
            case HistogramValuesSourceBuilder.TYPE:
                builder = HistogramValuesSourceBuilder.parse(name, parser);
                break;
            default:
                throw new ParsingException(parser.getTokenLocation(), "invalid source type: " + type);
        }
        parser.nextToken();
        parser.nextToken();
        return builder;
    }

    @Override
    public final XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject(type());
        if (field != null) {
            builder.field("field", field);
        }
        if (missing != null) {
            builder.field("missing", missing);
        }
        if (valueType != null) {
            builder.field("value_type", valueType.getPreferredName());
        }
        builder.field("order", order);
        doXContentBody(builder);
        builder.endObject();
        return builder;
    }

    protected abstract void doXContentBody(XContentBuilder builder) throws IOException;

    public String name() {
        return name;
    }

    abstract String type();
}
