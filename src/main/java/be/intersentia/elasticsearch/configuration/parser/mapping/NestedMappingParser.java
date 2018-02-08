package be.intersentia.elasticsearch.configuration.parser.mapping;

import be.intersentia.elasticsearch.configuration.annotation.mapping.NestedMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.NestedMappings;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;
import be.intersentia.elasticsearch.configuration.factory.MappingFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class is responsible for translating a NestedMapping to a Map object the ElasticSearch client understands.
 */
public class NestedMappingParser extends AbstractMappingParser<NestedMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public NestedMappingParser(Class<?> clazz, Field field, NestedMapping annotation) {
        super(clazz, field, annotation);
    }
    @SuppressWarnings("unused") // This constructor is called using reflection
    public NestedMappingParser(Class<?> clazz, Field field, NestedMappings annotations) {
        super(clazz, field, annotations.value());
    }

    @Override
    public String getFieldName(NestedMapping annotation) {
        return getFieldName(annotation, annotation.field());
    }

    @Override
    public String getMappingName(NestedMapping annotation) {
        return "DEFAULT";
    }

    @Override
    public String getType(NestedMapping annotation) {
        return "nested";
    }

    @Override
    public void addMapping(Map<String, Object> mapping, NestedMapping annotation) {
        mapping.put("dynamic", annotation.dynamic().name().toLowerCase());
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll().name().toLowerCase());
        }
        if (field == null) {
            mapping.put("properties", new HashMap<String, String>());
        } else if (Collection.class.isAssignableFrom(field.getType())) {
            ParameterizedType listType = (ParameterizedType) field.getGenericType();
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            mapping.putAll(MappingFactory.createMapping(listClass, false, false, Optional.empty(), Optional.of(clazz)));
        } else {
            mapping.putAll(MappingFactory.createMapping(field.getType(), false, false, Optional.empty(), Optional.of(clazz)));
        }
    }
}
