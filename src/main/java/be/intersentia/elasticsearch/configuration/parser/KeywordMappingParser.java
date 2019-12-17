package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.IndexOptions;
import be.intersentia.elasticsearch.configuration.annotation.mapping.KeywordMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;
import org.apache.commons.lang3.ArrayUtils;

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

    @Override
    public String getFieldName() {
        return getFieldName(annotation.field());
    }

    @Override
    public String getMappingName() {
        return annotation.mappingName();
    }

    @Override
    public String getType() {
        return "keyword";
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        mapping.put("boost", annotation.boost());
        if (ArrayUtils.isNotEmpty(annotation.copyTo())) {
            mapping.put("copy_to", annotation.copyTo());
        }
        mapping.put("doc_values", annotation.docValues());
        mapping.put("eager_global_ordinals", annotation.eagerGlobalOrdinals());
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll().name().toLowerCase());
        }
        mapping.put("ignore_above", annotation.ignoreAbove());
        mapping.put("index", annotation.index());
        if (annotation.indexOptions() != IndexOptions.DEFAULT) {
            mapping.put("index_options", annotation.indexOptions());
        }
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
        if (annotation.splitQueriesOnWhitespace() != OptionalBoolean.DEFAULT) {
            mapping.put("split_queries_on_whitespace", annotation.splitQueriesOnWhitespace());
        }
    }
}
