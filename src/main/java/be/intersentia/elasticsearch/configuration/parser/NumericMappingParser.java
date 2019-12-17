package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.NumericMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is responsible for translating a NumericMapping to a Map object the ElasticSearch client understands.
 */
public class NumericMappingParser extends AbstractMappingParser<NumericMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public NumericMappingParser(Class<?> clazz, Field field, NumericMapping annotation) {
        super(clazz, field, annotation);
    }

    @Override
    public String getFieldName() {
        return getFieldName(annotation.field());
    }

    @Override
    public String getMappingName() {
        return "DEFAULT";
    }

    @Override
    public String getType() {
        if (annotation.type() != NumericMapping.NumericType.DEFAULT) {
            return annotation.type().getValue();
        }
        String typeName = field.getType().getSimpleName().toLowerCase();
        if (typeName.contains("long")) {
            return NumericMapping.NumericType.LONG.getValue();
        } else if (typeName.contains("int")) {
            return NumericMapping.NumericType.INTEGER.getValue();
        } else if (typeName.contains("short")) {
            return NumericMapping.NumericType.SHORT.getValue();
        } else if (typeName.contains("byte")) {
            return NumericMapping.NumericType.BYTE.getValue();
        } else if (typeName.contains("double")) {
            return NumericMapping.NumericType.DOUBLE.getValue();
        } else if (typeName.contains("float")) {
            return NumericMapping.NumericType.FLOAT.getValue();
        }
        return NumericMapping.NumericType.DOUBLE.getValue();
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        mapping.put("boost", annotation.boost());
        mapping.put("coerce", annotation.coerce());
        if (ArrayUtils.isNotEmpty(annotation.copyTo())) {
            mapping.put("copy_to", annotation.copyTo());
        }
        mapping.put("doc_values", annotation.docValues());
        mapping.put("ignore_malformed", annotation.ignoreMalformed());
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll().name().toLowerCase());
        }
        mapping.put("index", annotation.index());
        if (!"DEFAULT".equals(annotation.nullValue())) {
            mapping.put("null_value", getNullValue(annotation.nullValue()));
        }
        mapping.put("store", annotation.store());
    }

    private static Number getNullValue(String value) {
        try {
            return NumberUtils.toDouble(value);
        } catch (Exception e) {
            throw new IllegalStateException("nullValue needs to be castable to a Number");
        }
    }
}
