package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.BooleanMapping;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is responsible for translating a BooleanMapping to a Map object the ElasticSearch client understands.
 */
public class BooleanMappingParser extends AbstractMappingParser<BooleanMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public BooleanMappingParser(Class<?> clazz, Field field, BooleanMapping annotation) {
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
        return "boolean";
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        mapping.put("boost", annotation.boost());
        if (ArrayUtils.isNotEmpty(annotation.copyTo())) {
            mapping.put("copy_to", annotation.copyTo());
        }
        mapping.put("doc_values", annotation.docValues());
        mapping.put("index", annotation.index());
        if (!"DEFAULT".equals(annotation.nullValue())) {
            mapping.put("null_value", annotation.nullValue());
        }
        mapping.put("store", annotation.store());
    }
}
