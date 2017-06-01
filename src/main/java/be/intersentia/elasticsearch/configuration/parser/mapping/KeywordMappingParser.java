package be.intersentia.elasticsearch.configuration.parser.mapping;

import be.intersentia.elasticsearch.configuration.annotation.mapping.KeywordMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.KeywordMappings;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is responsible for translating a KeywordMapping to a Map object the ElasticSearch client understands.
 */
public class KeywordMappingParser extends AbstractMappingParser<KeywordMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public KeywordMappingParser(Class<?> clazz, Field field, KeywordMapping annotation) {
        super(clazz, field, annotation);
    }
    @SuppressWarnings("unused") // This constructor is called using reflection
    public KeywordMappingParser(Class<?> clazz, Field field, KeywordMappings annotations) {
        super(clazz, field, annotations.value());
    }

    @Override
    public String getFieldName(KeywordMapping annotation) {
        return getFieldName(annotation, annotation.field());
    }

    @Override
    public String getMappingName(KeywordMapping annotation) {
        return annotation.mappingName();
    }

    @Override
    public String getType(KeywordMapping annotation) {
        return "keyword";
    }

    @Override
    public void addMapping(Map<String, Object> mapping, KeywordMapping annotation) {
        mapping.put("boost", annotation.boost());
        mapping.put("copy_to", annotation.copyTo());
        mapping.put("doc_values", annotation.docValues());
        mapping.put("eager_global_ordinals", annotation.eagerGlobalOrdinals());
        mapping.put("ignore_above", annotation.ignoreAbove());
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll());
        }
        mapping.put("index", annotation.index());
        mapping.put("index_options", annotation.indexOptions());
        mapping.put("norms", annotation.norms());
        if (!"DEFAULT".equals(annotation.nullValue())) {
            mapping.put("null_value", annotation.nullValue());
        }
        mapping.put("store", annotation.store());
        if (!"DEFAULT".equals(annotation.searchAnalyzer())) {
            mapping.put("search_analyzer", annotation.searchAnalyzer());
        }
        if (!"DEFAULT".equals(annotation.similarity())) {
            mapping.put("similarity", annotation.similarity());
        }
        if (!"DEFAULT".equals(annotation.normalizer())) {
            mapping.put("normalizer", annotation.normalizer());
        }
    }
}
