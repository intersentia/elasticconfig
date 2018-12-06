package be.intersentia.elasticsearch.configuration.parser.mapping;

import be.intersentia.elasticsearch.configuration.annotation.mapping.IndexOptions;
import be.intersentia.elasticsearch.configuration.annotation.mapping.OptionalBoolean;
import be.intersentia.elasticsearch.configuration.annotation.mapping.TextMapping;
import be.intersentia.elasticsearch.configuration.annotation.mapping.TextMappings;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for translating a TextMapping to a Map object the ElasticSearch client understands.
 */
public class TextMappingParser extends AbstractMappingParser<TextMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public TextMappingParser(Class<?> clazz, Field field, TextMapping annotation) {
        super(clazz, field, annotation);
    }
    @SuppressWarnings("unused") // This constructor is called using reflection
    public TextMappingParser(Class<?> clazz, Field field, TextMappings annotations) {
        super(clazz, field, annotations.value());
    }

    @Override
    public String getFieldName(TextMapping annotation) {
        return getFieldName(annotation, annotation.field());
    }

    @Override
    public String getMappingName(TextMapping annotation) {
        return annotation.mappingName();
    }

    @Override
    public String getType(TextMapping annotation) {
        return "text";
    }

    @Override
    public void addMapping(Map<String, Object> mapping, TextMapping annotation) {
        if (!"DEFAULT".equals(annotation.analyzer())) {
            mapping.put("analyzer", annotation.analyzer());
        }
        mapping.put("boost", annotation.boost());

        if (ArrayUtils.isNotEmpty(annotation.copyTo())) {
            mapping.put("copy_to", annotation.copyTo());
        }

        mapping.put("eager_global_ordinals", annotation.eagerGlobalOrdinals());
        mapping.put("fielddata", annotation.fieldData());
        addFieldDataFrequencyFilterMapping(mapping, annotation);
        if (annotation.includeInAll() != OptionalBoolean.DEFAULT) {
            mapping.put("include_in_all", annotation.includeInAll().name().toLowerCase());
        }
        mapping.put("index", annotation.index());
        if (annotation.indexOptions() != IndexOptions.DEFAULT) {
            mapping.put("index_options", annotation.indexOptions());
        }
        mapping.put("norms", annotation.norms());
        if (annotation.positionIncrementGap() != 100) {
            mapping.put("position_increment_gap", annotation.positionIncrementGap());
        }
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

    private void addFieldDataFrequencyFilterMapping(Map<String, Object> mapping, TextMapping annotation) {
        TextMapping.FieldDataFrequencyFilter frequencyFilter = annotation.fieldDataFrequencyFilter();
        if (frequencyFilter.min() != 0 || frequencyFilter.max() != 0 || frequencyFilter.minSegmentSize() != 0) {
            Map<String, Object> frequencyMap = new HashMap<String, Object>();
            frequencyMap.put("min", frequencyFilter.min());
            frequencyMap.put("max", frequencyFilter.max());
            frequencyMap.put("min_segment_size", frequencyFilter.minSegmentSize());
            mapping.put("fielddata_frequency_filter", frequencyMap);
        }
    }
}
