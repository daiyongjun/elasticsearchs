package cn.gsdata.elasticsearchs.open.sql.core.expression.aggregation;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils;
import cn.gsdata.elasticsearchs.open.sql.core.expression.DSL;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.INTEGER;

public class AvgAggregatorTest {
    protected static List<ExprValue> tuples = Arrays.asList(
            ExprValueUtils.tupleValue(
                    Map.of(
                            "integer_value",
                            1,
                            "long_value",
                            1L,
                            "string_value",
                            "f",
                            "double_value",
                            1d,
                            "float_value",
                            1f,
                            "date_value",
                            "2020-01-01",
                            "datetime_value",
                            "2020-01-01 00:00:00",
                            "time_value",
                            "00:00:00",
                            "timestamp_value",
                            "2020-01-01 00:00:00")),
            ExprValueUtils.tupleValue(
                    Map.of(
                            "integer_value",
                            3,
                            "long_value",
                            3L,
                            "string_value",
                            "m",
                            "double_value",
                            3d,
                            "float_value",
                            3f,
                            "date_value",
                            "1970-01-01",
                            "datetime_value",
                            "1970-01-01 19:00:00",
                            "time_value",
                            "19:00:00",
                            "timestamp_value",
                            "1970-01-01 19:00:00")),
            ExprValueUtils.tupleValue(
                    Map.of(
                            "integer_value",
                            4,
                            "long_value",
                            4L,
                            "string_value",
                            "n",
                            "double_value",
                            4d,
                            "float_value",
                            4f,
                            "date_value",
                            "2040-01-01",
                            "datetime_value",
                            "2040-01-01 07:00:00",
                            "time_value",
                            "07:00:00",
                            "timestamp_value",
                            "2040-01-01 07:00:00")));

    //public void avg_field_expression() {
    //    ExprValue result = aggregation(dsl.avg(DSL.ref("integer_value", INTEGER)), tuples);
    //    assertEquals(2.5, result.value());
    //}
}

