package be.intersentia.elasticsearch.configuration.parser.mapping;

import be.intersentia.elasticsearch.configuration.annotation.mapping.NumericMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.NumericMappings;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;
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
    @SuppressWarnings("unused") // This constructor is called using reflection
    public NumericMappingParser(Class<?> clazz, Field field, NumericMappings annotations) {
        super(clazz, field, annotations.value());
    }

    @Override
    public String getFieldName(NumericMapping annotation) {
        return getFieldName(annotation, annotation.field());
    }

    @Override
    public String getMappingName(NumericMapping annotation) {
        return "DEFAULT";
    }

    @Override
    public String getType(NumericMapping annotation) {
        return getNumericType(annotation).name().toLowerCase();
    }

    private NumericMapping.NumericType getNumericType(NumericMapping annotation) {
        if (annotation.type() != NumericMapping.NumericType.DEFAULT) {
            return annotation.type();
        }
        String typeName = field.getType().getSimpleName().toLowerCase();
        if (typeName.contains("long")) {
            return NumericMapping.NumericType.LONG;
        } else if (typeName.contains("int")) {
            return NumericMapping.NumericType.INTEGER;
        } else if (typeName.contains("short")) {
            return NumericMapping.NumericType.SHORT;
        } else if (typeName.contains("byte")) {
            return NumericMapping.NumericType.BYTE;
        } else if (typeName.contains("double")) {
            return NumericMapping.NumericType.DOUBLE;
        } else if (typeName.contains("float")) {
            return NumericMapping.NumericType.FLOAT;
        }
        return NumericMapping.NumericType.DOUBLE;
    }

    @Override
    public void addMapping(Map<String, Object> mapping, NumericMapping annotation) {
        mapping.put("boost", annotation.boost());
        mapping.put("coerce", annotation.coerce());
        mapping.put("copy_to", annotation.copyTo());
        mapping.put("doc_values", annotation.docValues());
        mapping.put("ignore_malformed", annotation.ignoreMalformed());
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll());
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
