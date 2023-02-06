package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.terms;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;

import java.io.IOException;

/**
 * @author daiyongjun
 */
public class BucketCount implements ToXContent {
    private int requiredSize;
    private int shardSize;
    private long minDocCount;
    private long shardMinDocCount;

    public BucketCount(int requiredSize, int shardSize, long minDocCount, long shardMinDocCount) {
        this.requiredSize = requiredSize;
        this.shardSize = shardSize;
        this.minDocCount = minDocCount;
        this.shardMinDocCount = shardMinDocCount;
    }

    public BucketCount(BucketCount bucketCount) {
        this(bucketCount.requiredSize, bucketCount.shardSize, bucketCount.minDocCount, bucketCount.shardMinDocCount);
    }

    public long getShardMinDocCount() {
        return shardMinDocCount;
    }

    public void setShardMinDocCount(long shardMinDocCount) {
        this.shardMinDocCount = shardMinDocCount;
    }

    public long getMinDocCount() {
        return minDocCount;
    }

    public void setMinDocCount(long minDocCount) {
        this.minDocCount = minDocCount;
    }

    public int getRequiredSize() {
        return requiredSize;
    }

    public void setRequiredSize(int requiredSize) {
        this.requiredSize = requiredSize;
    }

    public int getShardSize() {
        return shardSize;
    }

    public void setShardSize(int shardSize) {
        this.shardSize = shardSize;
    }

    public void ensureValidity() {
        // shard_size cannot be smaller than size as we need to at least fetch <size> entries from every shards in order to return <size>
        if (shardSize < requiredSize) {
            setShardSize(requiredSize);
        }

        // shard_min_doc_count should not be larger than min_doc_count because this can cause buckets to be removed that would match the min_doc_count criteria
        if (shardMinDocCount > minDocCount) {
            setShardMinDocCount(minDocCount);
        }

        if (requiredSize <= 0 || shardSize <= 0) {
            throw new ElasticsearchException("parameters [required_size] and [shard_size] must be >0 in terms aggregation.");
        }

        if (minDocCount < 0 || shardMinDocCount < 0) {
            throw new ElasticsearchException("parameter [min_doc_count] and [shardMinDocCount] must be >=0 in terms aggregation.");
        }
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.field(TermsAggregationBuilder.REQUIRED_SIZE_FIELD_NAME.getPreferredName(), requiredSize);
        if (shardSize != -1) {
            builder.field(TermsAggregationBuilder.SHARD_SIZE_FIELD_NAME.getPreferredName(), shardSize);
        }
        builder.field(TermsAggregationBuilder.MIN_DOC_COUNT_FIELD_NAME.getPreferredName(), minDocCount);
        builder.field(TermsAggregationBuilder.SHARD_MIN_DOC_COUNT_FIELD_NAME.getPreferredName(), shardMinDocCount);
        return builder;
    }
}
