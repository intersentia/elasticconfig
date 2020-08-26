package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.*;
import be.intersentia.elasticsearch.configuration.factory.MappingFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * This class is responsible for translating an ObjectMapping to a Map object the ElasticSearch client understands.
 */
public class ObjectMappingParser extends AbstractMappingParser<ObjectMapping> {

    public ObjectMappingParser(Class<?> clazz, Field field, ObjectMapping annotation) {
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
        return "object";
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        mapping.put("dynamic", annotation.dynamic().name().toLowerCase());
        mapping.put("enabled", annotation.enabled());
        mapping.put("type", "object");
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll().name().toLowerCase());
        }
        if (field == null) {
            mapping.put("properties", new HashMap<String, Object>());
        } else if (Collection.class.isAssignableFrom(field.getType())) {
            ParameterizedType listType = (ParameterizedType) field.getGenericType();
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            mapping.putAll(MappingFactory.createMapping(Collections.singletonList(listClass), false, clazz));
        } else {
            mapping.putAll(MappingFactory.createMapping(Collections.singletonList(field.getType()), false, clazz));
        }
    }
}
