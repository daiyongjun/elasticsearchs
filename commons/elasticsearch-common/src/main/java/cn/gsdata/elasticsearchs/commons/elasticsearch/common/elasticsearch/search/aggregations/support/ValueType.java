package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.index.fielddata.IndexFieldData;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.index.fielddata.IndexGeoPointFieldData;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.index.fielddata.IndexNumericFieldData;

public enum ValueType {
    /**
     * String
     */
    STRING((byte) 1, "string", "string", IndexFieldData.class, ValuesSourceType.BYTES),
    LONG((byte) 2, "byte|short|integer|long", "long", IndexNumericFieldData.class, ValuesSourceType.NUMERIC),
    DOUBLE((byte) 3, "float|double", "double", IndexNumericFieldData.class, ValuesSourceType.NUMERIC),
    NUMBER((byte) 4, "number", "number", IndexNumericFieldData.class, ValuesSourceType.NUMERIC),
    DATE((byte) 5, "date", "date", IndexNumericFieldData.class, ValuesSourceType.NUMERIC),
    IP((byte) 6, "ip", "ip", IndexFieldData.class, ValuesSourceType.BYTES),
    NUMERIC((byte) 7, "numeric", "numeric", IndexNumericFieldData.class, ValuesSourceType.NUMERIC),
    GEOPOINT((byte) 8, "geo_point", "geo_point", IndexGeoPointFieldData.class, ValuesSourceType.GEOPOINT),
    BOOLEAN((byte) 9, "boolean", "boolean", IndexNumericFieldData.class, ValuesSourceType.NUMERIC);

    private final byte id;
    final String description;
    final ValuesSourceType valuesSourceType;
    final Class<? extends IndexFieldData> fieldDataType;
    private String preferredName;

    ValueType(byte id, String description, String preferredName, Class<? extends IndexFieldData> fieldDataType, ValuesSourceType valuesSourceType) {
        this.id = id;
        this.description = description;
        this.preferredName = preferredName;
        this.fieldDataType = fieldDataType;
        this.valuesSourceType = valuesSourceType;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public static ValueType resolveForScript(String type) {
        switch (type) {
            case "string":
                return STRING;
            case "double":
            case "float":
                return DOUBLE;
            case "long":
            case "integer":
            case "short":
            case "byte":
                return LONG;
            case "date":
                return DATE;
            case "ip":
                return IP;
            case "boolean":
                return BOOLEAN;
            default:
                // TODO: do not be lenient here
                return null;
        }
    }

    public boolean isNotA(ValueType valueType) {
        return !isA(valueType);
    }

    public boolean isA(ValueType valueType) {
        return valueType.valuesSourceType == valuesSourceType &&
                valueType.fieldDataType.isAssignableFrom(fieldDataType);
    }
}