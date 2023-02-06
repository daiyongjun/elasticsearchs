package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.adjacency.AdjacencyMatrixAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.missing.MissingAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置可以在搜索时完成的事情，例如查询、聚合和建议。
 *
 * @author daiyongjun
 */
public class SearchModule {

    private final List<NamedXContentRegistry.Entry> namedXContents = new ArrayList<>();


    public SearchModule() {
        registerQueryParsers();
        registerAggregations();
    }

    /**
     * 获取namedXContents
     *
     * @return List<NamedXContentRegistry.Entry>
     */
    public List<NamedXContentRegistry.Entry> getNamedXContents() {
        return this.namedXContents;
    }

    /**
     * 初始化namedXContents，注册相关Query类
     */
    private void registerQueryParsers() {
        registerQuery(new SearchPlugin.QuerySpec<TermQueryBuilder>(TermQueryBuilder.NAME, TermQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<TermsQueryBuilder>(TermsQueryBuilder.NAME, TermsQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<BoolQueryBuilder>(BoolQueryBuilder.NAME, BoolQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<MatchAllQueryBuilder>(MatchAllQueryBuilder.NAME, MatchAllQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<MatchQueryBuilder>(MatchQueryBuilder.NAME, MatchQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<MatchPhraseQueryBuilder>(MatchPhraseQueryBuilder.NAME, MatchPhraseQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<TermsSetQueryBuilder>(TermsSetQueryBuilder.NAME, TermsSetQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<RangeQueryBuilder>(RangeQueryBuilder.NAME, RangeQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<ExistsQueryBuilder>(ExistsQueryBuilder.NAME, ExistsQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<PrefixQueryBuilder>(PrefixQueryBuilder.NAME, PrefixQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<WildcardQueryBuilder>(WildcardQueryBuilder.NAME, WildcardQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<RegexpQueryBuilder>(RegexpQueryBuilder.NAME, RegexpQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<FuzzyQueryBuilder>(FuzzyQueryBuilder.NAME, FuzzyQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<TypeQueryBuilder>(TypeQueryBuilder.NAME, TypeQueryBuilder::fromXContent));
        registerQuery(new SearchPlugin.QuerySpec<IdsQueryBuilder>(IdsQueryBuilder.NAME, IdsQueryBuilder::fromXContent));


    }

    private void registerQuery(SearchPlugin.QuerySpec<?> spec) {
        namedXContents.add(new NamedXContentRegistry.Entry(QueryBuilder.class, spec.getName(), (p, c) -> spec.getParser().fromXContent(p, c)));
    }

    private void registerAggregations() {

        //metrics
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(AvgAggregationBuilder.NAME, AvgAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(CardinalityAggregationBuilder.NAME, CardinalityAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(MaxAggregationBuilder.NAME, MaxAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(MinAggregationBuilder.NAME, MinAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(SumAggregationBuilder.NAME, SumAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(StatsAggregationBuilder.NAME, StatsAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(ExtendedStatsAggregationBuilder.NAME, ExtendedStatsAggregationBuilder::parse));


        //bucketExtendedStatsAggregationBuilder
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(TermsAggregationBuilder.NAME, TermsAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(RangeAggregationBuilder.NAME, RangeAggregationBuilder::parse));


        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(AdjacencyMatrixAggregationBuilder.NAME, AdjacencyMatrixAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(CompositeAggregationBuilder.NAME, CompositeAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(FiltersAggregationBuilder.NAME, FiltersAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(GlobalAggregationBuilder.NAME, GlobalAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(MissingAggregationBuilder.NAME, MissingAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(NestedAggregationBuilder.NAME, NestedAggregationBuilder::parse));


        //registerAggregation((new AggregationSpec(CompositeAggregationBuilder.NAME, CompositeAggregationBuilder::new,
        //        CompositeAggregationBuilder::parse).addResultReader(InternalComposite::new)));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(HistogramAggregationBuilder.NAME, HistogramAggregationBuilder::parse));
        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(DateHistogramAggregationBuilder.NAME, DateHistogramAggregationBuilder::parse));


        registerAggregation(new SearchPlugin.AggregationSpec<AggregationBuilder>(DateRangeAggregationBuilder.NAME, DateRangeAggregationBuilder::parse));

    }

    private void registerAggregation(SearchPlugin.AggregationSpec<?> spec) {
        namedXContents.add(new NamedXContentRegistry.Entry(AggregationBuilder.class, spec.getName(), (p, c) ->
                spec.getParser().parse(String.valueOf(c), p)));
    }
}