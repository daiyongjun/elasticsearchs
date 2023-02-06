package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.terms;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 包括或排出字段
 *
 * @author daiyongjun
 */
public class IncludeExclude implements ToXContent {
    public static final ParseField INCLUDE_FIELD = new ParseField("include");
    public static final ParseField EXCLUDE_FIELD = new ParseField("exclude");
    public static final ParseField PARTITION_FIELD = new ParseField("partition");
    public static final ParseField NUM_PARTITIONS_FIELD = new ParseField("num_partitions");

    String include, exclude;
    private final SortedSet<String> includeValues, excludeValues;
    /**
     * 参考文档：https://blog.csdn.net/weixin_39605347/article/details/111217538
     * 聚合理论上不支持scroll游标查询，单是支持分区获取，基于分区获取统计结果，初始分区号
     */
    private final int partition;
    /**
     * 分区数
     */
    private final int numPartitions;

    public IncludeExclude(String include, String exclude) {
        if (include == null && exclude == null) {
            throw new IllegalArgumentException();
        }
        this.include = include;
        this.exclude = exclude;
        this.includeValues = null;
        this.excludeValues = null;
        this.partition = 0;
        this.numPartitions = 0;
    }

    public IncludeExclude(int partition, int numPartitions) {
        if (partition < 0 || partition >= numPartitions) {
            throw new IllegalArgumentException("Partition must be >=0 and < numPartition which is " + numPartitions);
        }
        this.partition = partition;
        this.numPartitions = numPartitions;
        this.include = null;
        this.exclude = null;
        this.includeValues = null;
        this.excludeValues = null;

    }

    public IncludeExclude(SortedSet<String> includeValues, SortedSet<String> excludeValues) {
        if (includeValues == null && excludeValues == null) {
            throw new IllegalArgumentException();
        }
        this.include = null;
        this.exclude = null;
        this.partition = 0;
        this.numPartitions = 0;
        this.includeValues = includeValues;
        this.excludeValues = excludeValues;
    }

    public static IncludeExclude parseInclude(XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();
        if (token == XContentParser.Token.VALUE_STRING) {
            return new IncludeExclude(parser.text(), null);
        } else if (token == XContentParser.Token.START_ARRAY) {
            return new IncludeExclude(new TreeSet<>(parseArrayToSet(parser)), null);
        } else if (token == XContentParser.Token.START_OBJECT) {
            String currentFieldName = null;
            Integer partition = null, numPartitions = null;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (NUM_PARTITIONS_FIELD.match(currentFieldName)) {
                    numPartitions = parser.intValue();
                } else if (PARTITION_FIELD.match(currentFieldName)) {
                    partition = parser.intValue();
                } else {
                    throw new ElasticsearchException(
                            "Unknown parameter in Include/Exclude clause: " + currentFieldName);
                }
            }
            if (partition == null) {
                throw new IllegalArgumentException("Missing [" + PARTITION_FIELD.getPreferredName()
                        + "] parameter for partition-based include");
            }
            if (numPartitions == null) {
                throw new IllegalArgumentException("Missing [" + NUM_PARTITIONS_FIELD.getPreferredName()
                        + "] parameter for partition-based include");
            }
            return new IncludeExclude(partition, numPartitions);
        } else {
            throw new IllegalArgumentException("Unrecognized token for an include [" + token + "]");
        }
    }

    public static IncludeExclude merge(IncludeExclude include, IncludeExclude exclude) {
        if (include == null) {
            return exclude;
        }
        if (exclude == null) {
            return include;
        }
        if (include.isPartition()) {
            throw new IllegalArgumentException("Cannot specify any excludes when using a partition-based include");
        }
        String includeMethod = include.isNotNull() ? "regex" : "set";
        String excludeMethod = exclude.isNotNull() ? "regex" : "set";
        if (!includeMethod.equals(excludeMethod)) {
            throw new IllegalArgumentException("Cannot mix a " + includeMethod + "-based include with a "
                    + excludeMethod + "-based method");
        }
        if (include.isNotNull()) {
            return new IncludeExclude(include.include, exclude.exclude);
        } else {
            return new IncludeExclude(include.includeValues, exclude.excludeValues);
        }
    }

    public static IncludeExclude parseExclude(XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();
        if (token == XContentParser.Token.VALUE_STRING) {
            return new IncludeExclude(null, parser.text());
        } else if (token == XContentParser.Token.START_ARRAY) {
            return new IncludeExclude(null, new TreeSet<>(parseArrayToSet(parser)));
        } else {
            throw new IllegalArgumentException("Unrecognized token for an exclude [" + token + "]");
        }
    }

    private static Set<String> parseArrayToSet(XContentParser parser) throws IOException {
        final Set<String> set = new HashSet<>();
        if (parser.currentToken() != XContentParser.Token.START_ARRAY) {
            throw new ElasticsearchException("Missing start of array in include/exclude clause");
        }
        while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
            if (!parser.currentToken().isValue()) {
                throw new ElasticsearchException("Array elements in include/exclude clauses should be string values");
            }
            set.add(new String(parser.text()));
        }
        return set;
    }

    /**
     * 判断是否分区聚合
     *
     * @return boolean
     */
    public boolean isPartition() {
        return partition > 0;
    }

    /**
     * 验证include和exclude不能为null
     *
     * @return boolean
     */
    public boolean isNotNull() {
        return include != null || exclude != null;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        if (include != null) {
            builder.field(INCLUDE_FIELD.getPreferredName(), include);
        } else if (includeValues != null) {
            builder.startArray(INCLUDE_FIELD.getPreferredName());
            for (String value : includeValues) {
                builder.value(value);
            }
            builder.endArray();
        } else if (isPartition()) {
            builder.startObject(INCLUDE_FIELD.getPreferredName());
            builder.field(PARTITION_FIELD.getPreferredName(), partition);
            builder.field(NUM_PARTITIONS_FIELD.getPreferredName(), numPartitions);
            builder.endObject();
        }
        if (exclude != null) {
            builder.field(EXCLUDE_FIELD.getPreferredName(), exclude);
        } else if (excludeValues != null) {
            builder.startArray(EXCLUDE_FIELD.getPreferredName());
            for (String value : excludeValues) {
                builder.value(value);
            }
            builder.endArray();
        }
        return builder;
    }
}
