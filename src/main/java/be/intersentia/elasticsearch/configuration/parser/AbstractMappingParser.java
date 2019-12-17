package be.intersentia.elasticsearch.configuration.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * This abstract class is responsible for transforming specific types of ElasticSearch Mapping interfaces into a Map
 * object the ElasticSearch client understands.
 */
public abstract class AbstractMappingParser<T extends Annotation> {
    protected Class<?> clazz;
    protected Field field;
    protected T annotation;

    AbstractMappingParser(Class<?> clazz, Field field, T annotation) {
        this.clazz = clazz;
        this.field = field;
        this.annotation = annotation;
    }

    public abstract String getFieldName();

    String getFieldName(String fieldName) {
        if (!"DEFAULT".equals(fieldName)) {
            return fieldName;
        } else if (field == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is annotated with an ElasticSearch "
                    + getType() + " mapping without a fieldName. FieldName is only optional for method annotations");
        }
        return this.field.getName();
    }

    /**
     * Get the name of the mapping. No two mappings for the same field may have the ame name. There always needs to be
     * exactly one annotation with the "default" name, which will be the default mapping. All other annotations will be
     * created as multi-fields underneath the default mapping.  This is often useful to index the same field in
     * different ways for different purposes. For instance, a String field could be mapped as a TextMapping for
     * full-text search, and as a KeywordMapping for sorting or aggregations.
     */
    public abstract String getMappingName();

    public abstract String getType();

    /**
     * Get the Map object created based on the Mapping annotation.
     */
    public Map<String, Object> getMapping() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        addMapping(map);
        return map;
    }

    protected abstract void addMapping(Map<String, Object> map);

    public boolean hasDefault() {
        return "DEFAULT".equals(getMappingName());
    }
}