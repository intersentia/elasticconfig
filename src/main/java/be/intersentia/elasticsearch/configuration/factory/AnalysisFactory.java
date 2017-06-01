package be.intersentia.elasticsearch.configuration.factory;

import be.intersentia.elasticsearch.configuration.annotation.analyzer.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

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
    private static final Logger log = Logger.getLogger(AnalysisFactory.class);

    public static Map<String, ?> createAnalysis(Class<?> clazz) {
        log.info("Creating ElasticSearch analysis for " + clazz.getSimpleName());
        Map<String, Object> map = new HashMap<String, Object>();

        log.trace(clazz.getSimpleName() + ": Inspecting annotations");
        Analysis analysis = clazz.getAnnotation(Analysis.class);
        if (analysis != null) {
            map = createAnalysis(analysis);
        }

        ByteArrayOutputStream is = new ByteArrayOutputStream();
        if (log.isDebugEnabled()) {
            PrintStream stream = new PrintStream(is);
            MapUtils.verbosePrint(stream, clazz.getSimpleName(), map);
            stream.close();
            log.debug("Returning analysis for " + is.toString());
        }
        return Collections.singletonMap("analysis", map);
    }

    private static Map<String, Object> createAnalysis(Analysis analysis) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ArrayUtils.isNotEmpty(analysis.charFilters())) {
            map.put("char_filter", createConfiguration(analysis.charFilters()));
        }
        if (ArrayUtils.isNotEmpty(analysis.tokenizers())) {
            map.put("tokenizer", createConfiguration(analysis.tokenizers()));
        }
        if (ArrayUtils.isNotEmpty(analysis.filters())) {
            map.put("filter", createConfiguration(analysis.filters()));
        }
        if (ArrayUtils.isNotEmpty(analysis.analyzers()) || ArrayUtils.isNotEmpty(analysis.customAnalyzers())) {
            map.put("analyzer", createConfiguration(analysis.analyzers(), analysis.customAnalyzers()));
        }
        if (ArrayUtils.isNotEmpty(analysis.customNormalizers())) {
            map.put("normalizer", createConfiguration(analysis.customNormalizers()));
        }
        return map;
    }

    private static Object createConfiguration(CharFilter[] charFilters) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (CharFilter charFilter : charFilters) {
            map.put(charFilter.name(), createConfiguration(charFilter.type(), charFilter.properties()));
        }
        return map;
    }

    private static Object createConfiguration(Tokenizer[] tokenizers) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Tokenizer tokenizer : tokenizers) {
            map.put(tokenizer.name(), createConfiguration(tokenizer.type(), tokenizer.properties()));
        }
        return map;
    }

    private static Object createConfiguration(Filter[] filters) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Filter filter : filters) {
            map.put(filter.name(), createConfiguration(filter.type(), filter.properties()));
        }
        return map;
    }

    private static Object createConfiguration(Analyzer[] analyzers, CustomAnalyzer[] customAnalyzers) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (analyzers != null) {
            for (Analyzer analyzer : analyzers) {
                map.put(analyzer.name(), createConfiguration(analyzer.type(), analyzer.properties()));
            }
        }
        if (customAnalyzers != null) {
            for (CustomAnalyzer customAnalyzer : customAnalyzers) {
                Map<String, Object> elementMap = new HashMap<String, Object>();
                elementMap.put("char_filter", customAnalyzer.charFilters());
                elementMap.put("tokenizer", customAnalyzer.tokenizer());
                elementMap.put("filter", customAnalyzer.filters());
                elementMap.put("position_increment_gap", customAnalyzer.positionIncrementGap());
                map.put(customAnalyzer.name(), elementMap);
            }
        }
        return map;
    }

    private static Object createConfiguration(CustomNormalizer[] customNormalizers) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (CustomNormalizer customNormalizer : customNormalizers) {
            Map<String, Object> elementMap = new HashMap<String, Object>();
            elementMap.put("char_filter", customNormalizer.charFilters());
            elementMap.put("filter", customNormalizer.filters());
            map.put(customNormalizer.name(), elementMap);
        }
        return map;
    }

    private static Object createConfiguration(String type, Property[] properties) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        for(Property property : properties) {
            map.put(property.key(), property.value());
        }
        return map;
    }
}