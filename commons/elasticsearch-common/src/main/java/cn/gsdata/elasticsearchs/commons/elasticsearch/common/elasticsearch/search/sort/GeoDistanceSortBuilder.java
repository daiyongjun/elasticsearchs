//package cn.yanwei.study.elasticsearch.search.sort;
//
//import cn.yanwei.study.elasticsearch.search.common.xcontent.XContentBuilder;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
///**
// * 基于地域距离（GeoDistance）排序
// *
// * @author daiyongjun
// */
//public class GeoDistanceSortBuilder extends SortBuilder<GeoDistanceSortBuilder> {
//    public static final String NAME = "_geo_distance";
//
//    private final String fieldName;
//    private final List<GeoPoint> points = new ArrayList<>();
//
//    @Override
//    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
//        return null;
//    }
//}
