package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.geo;

/**
 * 地理位置点
 *
 * @author daiyongjun
 */
public final class GeoPoint {
    /**
     *
     */
    private double lat;
    private double lon;

    public GeoPoint() {
    }

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
