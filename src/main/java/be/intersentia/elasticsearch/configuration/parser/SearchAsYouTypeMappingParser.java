package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.IndexOptions;
import be.intersentia.elasticsearch.configuration.annotation.mapping.SearchAsYouTypeMapping;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is responsible for translating a TextMapping to a Map object the ElasticSearch client understands.
 */
public class SearchAsYouTypeMappingParser extends AbstractMappingParser<SearchAsYouTypeMapping> {

    // This constructor is called using reflection
    public SearchAsYouTypeMappingParser(Class<?> clazz, Field field, SearchAsYouTypeMapping annotation) {
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
        return "search_as_you_type";
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        if (!"DEFAULT".equals(annotation.analyzer())) {
            mapping.put("analyzer", annotation.analyzer());
        }

        mapping.put("max_shingle_size", annotation.maxShingleSize());

        if (ArrayUtils.isNotEmpty(annotation.copyTo())) {
            mapping.put("copy_to", annotation.copyTo());
        }
        mapping.put("index", annotation.index());
        if (annotation.indexOptions() != IndexOptions.DEFAULT) {
            mapping.put("index_options", annotation.indexOptions());
        }
        mapping.put("norms", annotation.norms());
        mapping.put("store", annotation.store());
        if (!"DEFAULT".equals(annotation.searchAnalyzer())) {
            mapping.put("search_analyzer", annotation.searchAnalyzer());
        }
        if (!"DEFAULT".equals(annotation.searchQuoteAnalyzer())) {
            mapping.put("search_quote_analyzer", annotation.searchQuoteAnalyzer());
        }
        if (!"DEFAULT".equals(annotation.similarity())) {
            mapping.put("similarity", annotation.similarity());
        }
        mapping.put("term_vector", annotation.termVector().name().toLowerCase());
    }
}
