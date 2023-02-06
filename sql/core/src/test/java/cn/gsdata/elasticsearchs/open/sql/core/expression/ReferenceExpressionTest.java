package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.*;
import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.*;
import static cn.gsdata.elasticsearchs.open.sql.core.expression.DSL.ref;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ReferenceExpressionTest extends ExpressionTestBase {
    public static final String INT_TYPE_NULL_VALUE_FIELD = "int_null_value";
    public static final String INT_TYPE_MISSING_VALUE_FIELD = "int_missing_value";
    public static final String DOUBLE_TYPE_NULL_VALUE_FIELD = "double_null_value";
    public static final String DOUBLE_TYPE_MISSING_VALUE_FIELD = "double_missing_value";
    public static final String BOOL_TYPE_NULL_VALUE_FIELD = "null_value_boolean";
    public static final String BOOL_TYPE_MISSING_VALUE_FIELD = "missing_value_boolean";
    public static final String STRING_TYPE_NULL_VALUE_FILED = "string_null_value";
    public static final String STRING_TYPE_MISSING_VALUE_FILED = "string_missing_value";

    @Test
    public void resolve_value() {
        assertEquals(integerValue(1), ref("integer_value", INTEGER).valueOf(valueEnv()));
        assertEquals(longValue(1L), ref("long_value", LONG).valueOf(valueEnv()));
        assertEquals(floatValue(1f), ref("float_value", FLOAT).valueOf(valueEnv()));
        assertEquals(doubleValue(1d), ref("double_value", DOUBLE).valueOf(valueEnv()));
        assertEquals(booleanValue(true), ref("boolean_value", BOOLEAN).valueOf(valueEnv()));
        assertEquals(stringValue("str"), ref("string_value", STRING).valueOf(valueEnv()));
        assertEquals(tupleValue(ImmutableMap.of("str", 1)), ref("struct_value", STRUCT).valueOf(valueEnv()));
    }

    @Test
    public void resolve_type() {
        assertEquals(INTEGER, ref("integer_value", INTEGER).type());
        assertEquals(LONG, ref("long_value", LONG).type());
        assertEquals(FLOAT, ref("float_value", FLOAT).type());
        assertEquals(DOUBLE, ref("double_value", DOUBLE).type());
        assertEquals(BOOLEAN, ref("boolean_value", BOOLEAN).type());
        assertEquals(STRING, ref("string_value", STRING).type());
        assertEquals(STRUCT, ref("struct_value", STRUCT).type());
        assertEquals(ARRAY, ref("array_value", ARRAY).type());
        assertEquals(collectionValue(ImmutableList.of(1)),
                ref("array_value", ARRAY).valueOf(valueEnv()));
        assertEquals(LITERAL_NULL, ref(BOOL_TYPE_NULL_VALUE_FIELD, BOOLEAN).valueOf(valueEnv()));
        assertEquals(LITERAL_MISSING,
                ref(BOOL_TYPE_MISSING_VALUE_FIELD, BOOLEAN).valueOf(valueEnv()));
    }

    @Test
    public void path_as_whole_has_highest_priority() {
        ReferenceExpression expr = new ReferenceExpression("project.year", INTEGER);
        ExprValue actualValue = expr.resolve(tuple());
        assertEquals(INTEGER, actualValue.type());
        assertEquals(1990, actualValue.integerValue());
    }

    @Test
    public void one_path_value() {
        ReferenceExpression expr = ref("name", STRING);
        ExprValue actualValue = expr.resolve(tuple());
        assertEquals(STRING, actualValue.type());
        assertEquals("bob smith", actualValue.stringValue());
    }

    @Test
    public void multiple_path_value() {
        ReferenceExpression expr = new ReferenceExpression("address.state", STRING);
        ExprValue actualValue = expr.resolve(tuple());
        assertEquals(STRING, actualValue.type());
        assertEquals("WA", actualValue.stringValue());
    }

    @Test
    public void not_exist_path() {
        ReferenceExpression expr = new ReferenceExpression("missing_field", STRING);
        ExprValue actualValue = expr.resolve(tuple());
        assertTrue(actualValue.isMissing());
    }

    @Test
    public void object_field_contain_dot() {
        ReferenceExpression expr = new ReferenceExpression("address.local.state", STRING);
        ExprValue actualValue = expr.resolve(tuple());
        assertTrue(actualValue.isMissing());
    }

    @Test
    public void innner_none_object_field_contain_dot() {
        ReferenceExpression expr = new ReferenceExpression("address.project.year", INTEGER);
        ExprValue actualValue = expr.resolve(tuple());
        assertEquals(INTEGER, actualValue.type());
        assertEquals(1990, actualValue.integerValue());
    }

}
