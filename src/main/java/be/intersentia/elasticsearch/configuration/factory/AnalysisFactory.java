package be.intersentia.elasticsearch.configuration.factory;

import be.intersentia.elasticsearch.configuration.annotation.analyzer.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  This class is responsible for transforming a class annotated with various types of ElasticSearch Analysis interfaces
 *  into a Map object the ElasticSearch client understands.
 */
public class AnalysisFactory {
    private static final Logger log = LogManager.getLogger(AnalysisFactory.class);

    public static Map<String, ?> createAnalysis(Class<?> clazz) {
        log.info("Creating ElasticSearch analysis for " + clazz.getSimpleName());

        log.trace(clazz.getSimpleName() + ": Inspecting annotations");
        Map<String, Object> map = new HashMap<>();
        createConfiguration(map, clazz.getAnnotationsByType(CharFilter.class));
        createConfiguration(map, clazz.getAnnotationsByType(Tokenizer.class));
        createConfiguration(map, clazz.getAnnotationsByType(Filter.class));
        createConfiguration(map, clazz.getAnnotationsByType(Analyzer.class),
                clazz.getAnnotationsByType(CustomAnalyzer.class));
        createConfiguration(map, clazz.getAnnotationsByType(CustomNormalizer.class));

        ByteArrayOutputStream is = new ByteArrayOutputStream();
        if (log.isDebugEnabled()) {
            PrintStream stream = new PrintStream(is);
            MapUtils.verbosePrint(stream, clazz.getSimpleName(), map);
            stream.close();
            log.debug("Returning analysis for " + is.toString());
        }
        return Collections.singletonMap("analysis", map);
    }

    private static void createConfiguration(Map<String, Object> parentMap, CharFilter[] charFilters) {
        if (charFilters.length > 0) {
            Map<String, Object> map = new HashMap<>();
            for (CharFilter charFilter : charFilters) {
                map.put(charFilter.name(), createConfiguration(charFilter.type(), charFilter.properties()));
            }
            parentMap.put("char_filter", map);
        }
    }

    private static void createConfiguration(Map<String, Object> parentMap, Tokenizer[] tokenizers) {
        Map<String, Object> map = new HashMap<>();
        for (Tokenizer tokenizer : tokenizers) {
            map.put(tokenizer.name(), createConfiguration(tokenizer.type(), tokenizer.properties()));
        }
            parentMap.put("tokenizer", map);
    }

    private static void createConfiguration(Map<String, Object> parentMap, Filter[] filters) {
        if (filters.length > 0) {
            Map<String, Object> map = new HashMap<>();
            for (Filter filter : filters) {
                map.put(filter.name(), createConfiguration(filter.type(), filter.properties()));
            }
            parentMap.put("filter", map);
        }
    }

    private static void createConfiguration(Map<String, Object> parentMap, Analyzer[] analyzers,
                                            CustomAnalyzer[] customAnalyzers) {
        if (analyzers.length > 0 || customAnalyzers.length > 0) {
            Map<String, Object> map = new HashMap<>();
            for (Analyzer analyzer : analyzers) {
                map.put(analyzer.name(), createConfiguration(analyzer.type(), analyzer.properties()));
            }
            for (CustomAnalyzer customAnalyzer : customAnalyzers) {
                Map<String, Object> elementMap = new HashMap<>();
                elementMap.put("char_filter", customAnalyzer.charFilters());
                elementMap.put("tokenizer", customAnalyzer.tokenizer());
                elementMap.put("filter", customAnalyzer.filters());
                elementMap.put("position_increment_gap", customAnalyzer.positionIncrementGap());
                map.put(customAnalyzer.name(), elementMap);
            }
            parentMap.put("analyzer", map);
        }
    }

    private static void createConfiguration(Map<String, Object> parentMap, CustomNormalizer[] customNormalizers) {
        if (customNormalizers.length > 0) {
            Map<String, Object> map = new HashMap<>();
            for (CustomNormalizer customNormalizer : customNormalizers) {
                Map<String, Object> elementMap = new HashMap<>();
                elementMap.put("char_filter", customNormalizer.charFilters());
                elementMap.put("filter", customNormalizer.filters());
                map.put(customNormalizer.name(), elementMap);
            }
            parentMap.put("normalizer", map);
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
}