package be.intersentia.elasticsearch.configuration.parser.mapping;

import be.intersentia.elasticsearch.configuration.annotation.mapping.*;
import be.intersentia.elasticsearch.configuration.factory.MappingFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * This class is responsible for translating an ObjectMapping to a Map object the ElasticSearch client understands.
 */
public class ObjectMappingParser extends AbstractMappingParser<ObjectMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public ObjectMappingParser(Class<?> clazz, Field field, ObjectMapping annotation) {
        super(clazz, field, annotation);
    }
    @SuppressWarnings("unused") // This constructor is called using reflection
    public ObjectMappingParser(Class<?> clazz, Field field, ObjectMappings annotations) {
        super(clazz, field, annotations.value());
    }

    @Override
    public String getFieldName(ObjectMapping annotation) {
        return getFieldName(annotation, annotation.field());
    }

    @Override
    public String getMappingName(ObjectMapping annotation) {
        return "DEFAULT";
    }

    @Override
    public String getType(ObjectMapping annotation) {
        return "object";
    }

    /**
     * Get the Map object created based on the Mapping annotation.
     */
    @Override
    public void addMapping(Map<String, Object> map, List<AbstractMappingParser<?>> nestedParsers, boolean isNested) {
        for (ObjectMapping annotation : annotations) {
            Map<String, Object> annotationMap = new HashMap<>();
            addMapping(annotationMap, annotation);
            map.put(getFieldName(annotation), annotationMap);
        }
    }

    @Override
    public void addMapping(Map<String, Object> mapping, ObjectMapping annotation) {
        mapping.put("dynamic", annotation.dynamic().name().toLowerCase());
        mapping.put("enabled", annotation.enabled());
        mapping.put("type", "object");
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
