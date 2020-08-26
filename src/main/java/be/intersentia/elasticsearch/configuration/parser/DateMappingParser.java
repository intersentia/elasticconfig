package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.DateMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is responsible for translating a DateMapping to a Map object the ElasticSearch client understands.
 */
public class DateMappingParser extends AbstractMappingParser<DateMapping> {

    public DateMappingParser(Class<?> clazz, Field field, DateMapping annotation) {
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
        return "date";
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        mapping.put("boost", annotation.boost());
        if (ArrayUtils.isNotEmpty(annotation.copyTo())) {
            mapping.put("copy_to", annotation.copyTo());
        }
        mapping.put("doc_values", annotation.docValues());
        if (!"DEFAULT".equals(annotation.format())) {
            mapping.put("format", annotation.format());
        }
        if (!"DEFAULT".equals(annotation.locale())) {
            mapping.put("locale", annotation.locale());
        }
        mapping.put("ignore_malformed", annotation.ignoreMalformed());
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll().name().toLowerCase());
        }
        mapping.put("index", annotation.index());
        if (!"DEFAULT".equals(annotation.nullValue())) {
            mapping.put("null_value", annotation.nullValue());
        }
        mapping.put("store", annotation.store());
    }
}
