package be.intersentia.elasticsearch.configuration;

import be.intersentia.elasticsearch.configuration.annotation.Index;
import be.intersentia.elasticsearch.configuration.factory.AnalysisFactory;
import be.intersentia.elasticsearch.configuration.factory.MappingFactory;
import io.github.classgraph.ClassGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigurationScanner {
    private static final Logger log = LogManager.getLogger(ConfigurationScanner.class);

    private Map<Index, Class<?>> indices = new HashMap<>();

    public static ConfigurationScanner scan(String packageName) {
        ConfigurationScanner instance = new ConfigurationScanner();
        new ClassGraph()
                .whitelistPackages(packageName)
                .enableAnnotationInfo()
                .enableClassInfo()
                .scan()
                .getClassesWithAnnotation(Index.class.getName())
                .loadClasses()
                .forEach((c) -> {
                    log.debug("Found a searchable class: " + c.getName());
                    instance.indices.put(c.getAnnotation(Index.class), c);
                });
        return instance;
    }

    public Set<Index> getIndices() {
        return indices.keySet();
    }

    public List<CreateIndexResult> configure() {
        return indices.entrySet().stream().map((entry) -> {
            final Index indexInfo = entry.getKey();
            final Class<?> clazz = entry.getValue();
            return new CreateIndexResult(
                    indexInfo.value().equals("DEFAULT") ? clazz.getSimpleName() : indexInfo.value(),
                    AnalysisFactory.createAnalysis(clazz),
                    MappingFactory.createMapping(clazz, indexInfo.disableDynamicProperties(), indexInfo.parent()));
        }).collect(Collectors.toList());
    }
}
