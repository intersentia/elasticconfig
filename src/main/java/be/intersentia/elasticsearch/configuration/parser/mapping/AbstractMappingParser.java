package be.intersentia.elasticsearch.configuration.parser.mapping;

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
    protected List<T> annotations;

    AbstractMappingParser(Class<?> clazz, Field field, T... annotations) {
        this.clazz = clazz;
        this.field = field;
        this.annotations = new ArrayList<T>();
        this.annotations.addAll(Arrays.asList(annotations));
    }

    public abstract String getFieldName(T annotation);

    String getFieldName(T annotation, String fieldName) {
        if (!"DEFAULT".equals(fieldName)) {
            return fieldName;
        } else if (field == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is annotated with an ElasticSearch "
                    + getType(annotation) + " mapping without a fieldName. FieldName is only optional for method annotations");
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
    public abstract String getMappingName(T annotation);

    public abstract String getType(T annotation);

    /**
     * Get the Map object created based on the Mapping annotation.
     */
    public void addMapping(Map<String, Object> map, List<AbstractMappingParser<?>> nestedParsers, boolean isNested) {
        for (T annotation : annotations) {
            Map<String, Object> annotationMap = new HashMap<String, Object>();
            annotationMap.put("type", getType(annotation));
            addMapping(annotationMap, annotation);
            if (!nestedParsers.isEmpty()) {
                Map<String, Object> nestedMap = new HashMap<String, Object>();
                for (AbstractMappingParser<?> parser : nestedParsers) {
                    parser.addMapping(nestedMap, new ArrayList<AbstractMappingParser<?>>(), true);
                }
                annotationMap.put("fields", nestedMap);
            }
            map.put(isNested ? getMappingName(annotation) : getFieldName(annotation), annotationMap);
        }
    }

    protected abstract void addMapping(Map<String, Object> map, T annotation);

    public boolean hasDefault() {
        for (T annotation : annotations) {
            if ("DEFAULT".equals(getMappingName(annotation))) {
                return true;
            }
        }
        return false;
    }
}