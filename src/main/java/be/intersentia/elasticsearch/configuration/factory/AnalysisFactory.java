package be.intersentia.elasticsearch.configuration.factory;

import be.intersentia.elasticsearch.configuration.annotation.analyzer.*;
import be.intersentia.elasticsearch.configuration.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  This class is responsible for transforming one or more classes annotated with various types of ElasticSearch
 *  Analysis annotations into a Map object the ElasticSearch client understands.
 */
public class AnalysisFactory {
    private static final Logger log = LogManager.getLogger(AnalysisFactory.class);

    public static Map<String, ?> createAnalysis(List<Class<?>> classes) {
        String label = StringUtils.join(classes, Class::getSimpleName, ", ");
        log.info("Creating ElasticSearch analysis for " + label);

        Analysis analysis = new Analysis();
        for (Class<?> clazz : classes) {
            log.trace(clazz.getSimpleName() + ": Inspecting annotations");
            addToAnalysis(analysis, clazz);
        }

        Map<String, Object> map = analysis.toMap();
        if (log.isDebugEnabled()) {
            log.debug("Returning analysis for " + StringUtils.prettyPrint(label, map));
        }
        return Collections.singletonMap("analysis", map);
    }

    private static void addToAnalysis(Analysis analysis, Class<?> clazz) {
        for (CharFilter charFilter : clazz.getAnnotationsByType(CharFilter.class)) {
            analysis.charFilters.put(charFilter.name(), createConfiguration(charFilter.type(), charFilter.properties()));
        }
        for (Tokenizer tokenizer : clazz.getAnnotationsByType(Tokenizer.class)) {
            analysis.tokenizers.put(tokenizer.name(), createConfiguration(tokenizer.type(), tokenizer.properties()));
        }
        for (Filter filter : clazz.getAnnotationsByType(Filter.class)) {
            analysis.filters.put(filter.name(), createConfiguration(filter.type(), filter.properties()));
        }
        for (Analyzer analyzer : clazz.getAnnotationsByType(Analyzer.class)) {
            analysis.analyzers.put(analyzer.name(), createConfiguration(analyzer.type(), analyzer.properties()));
        }
        for (CustomAnalyzer customAnalyzer : clazz.getAnnotationsByType(CustomAnalyzer.class)) {
            Map<String, Object> elementMap = new HashMap<>();
            elementMap.put("char_filter", customAnalyzer.charFilters());
            elementMap.put("tokenizer", customAnalyzer.tokenizer());
            elementMap.put("filter", customAnalyzer.filters());
            elementMap.put("position_increment_gap", customAnalyzer.positionIncrementGap());
            analysis.analyzers.put(customAnalyzer.name(), elementMap);
        }
        for (CustomNormalizer customNormalizer : clazz.getAnnotationsByType(CustomNormalizer.class)) {
            Map<String, Object> elementMap = new HashMap<>();
            elementMap.put("char_filter", customNormalizer.charFilters());
            elementMap.put("filter", customNormalizer.filters());
            analysis.normalizers.put(customNormalizer.name(), elementMap);
        }
    }

    private static Map<String, Object> createConfiguration(String type, Property[] properties) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        for(Property property : properties) {
            map.put(property.key(), property.value());
        }
        return map;
    }

    private static class Analysis {
        private final Map<String, Object> charFilters = new HashMap<>();
        private final Map<String, Object> tokenizers = new HashMap<>();
        private final Map<String, Object> filters = new HashMap<>();
        private final Map<String, Object> analyzers = new HashMap<>();
        private final Map<String, Object> normalizers = new HashMap<>();

        private Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (!charFilters.isEmpty()) map.put("char_filter", charFilters);
            if (!tokenizers.isEmpty()) map.put("tokenizer", tokenizers);
            if (!filters.isEmpty()) map.put("filter", filters);
            if (!analyzers.isEmpty()) map.put("analyzer", analyzers);
            if (!normalizers.isEmpty()) map.put("normalizer", normalizers);
            return map;
        }
    }
}