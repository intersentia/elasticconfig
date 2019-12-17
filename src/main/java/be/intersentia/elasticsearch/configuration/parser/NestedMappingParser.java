package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.NestedMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;
import be.intersentia.elasticsearch.configuration.factory.MappingFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * This class is responsible for translating a NestedMapping to a Map object the ElasticSearch client understands.
 */
public class NestedMappingParser extends AbstractMappingParser<NestedMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public NestedMappingParser(Class<?> clazz, Field field, NestedMapping annotation) {
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
        return "nested";
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        mapping.put("dynamic", annotation.dynamic().name().toLowerCase());
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll().name().toLowerCase());
        }
        if (field == null) {
            mapping.put("properties", new HashMap<String, String>());
        } else if (Collection.class.isAssignableFrom(field.getType())) {
            ParameterizedType listType = (ParameterizedType) field.getGenericType();
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            mapping.putAll(MappingFactory.createMapping(Collections.singletonList(listClass), false, clazz));
        } else {
            mapping.putAll(MappingFactory.createMapping(Collections.singletonList(field.getType()), false, clazz));
        }
    }
}
