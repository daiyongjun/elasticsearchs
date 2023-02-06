package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script.Script;


/**
 * Builders工厂类
 *
 * @author daiyongjun
 */
public class SortBuilders {
    /**
     * 生产ScoreSortBuilder
     *
     * @return ScoreSortBuilder
     */
    public static ScoreSortBuilder scoreSort() {
        return new ScoreSortBuilder();
    }

    /**
     * 生产FieldSortBuilder
     *
     * @param field String
     * @return FieldSortBuilder
     */
    public static FieldSortBuilder fieldSort(String field) {
        return new FieldSortBuilder(field);
    }

    /**
     * 生产ScriptSortBuilder
     *
     * @param script 脚本类
     * @param type   脚本类型
     * @return ScriptSortBuilder
     */
    public static ScriptSortBuilder scriptSort(Script script, ScriptSortBuilder.ScriptSortType type) {
        return new ScriptSortBuilder(script, type);
    }

    //public static GeoDistanceSortBuilder geoDistanceSort(String fieldName, double lat, double lon) {
    //    return new GeoDistanceSortBuilder(fieldName, lat, lon);
    //}

    //public static GeoDistanceSortBuilder geoDistanceSort(String fieldName, GeoPoint... points) {
    //    return new GeoDistanceSortBuilder(fieldName, points);
    //}

    //public static GeoDistanceSortBuilder geoDistanceSort(String fieldName, String ... geohashes) {
    //    return new GeoDistanceSortBuilder(fieldName, geohashes);
    //}
}
