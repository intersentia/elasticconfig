package be.intersentia.elasticsearch.configuration;

import be.intersentia.elasticsearch.configuration.annotation.Index;
import be.intersentia.elasticsearch.configuration.annotation.analyzer.Analysis;
import be.intersentia.elasticsearch.configuration.factory.AnalysisFactory;
import be.intersentia.elasticsearch.configuration.factory.MappingFactory;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ClassInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigurationScanner {
    private static final Logger log = Logger.getLogger(ConfigurationScanner.class);

    final List<Class<?>> searchableClasses = new ArrayList<>();
    List<ClassInfo> classInfoStream = new ArrayList<>();
    Set<String> indices = new HashSet<>();

    public static ConfigurationScanner scan(String namespace) {
        ConfigurationScanner me = new ConfigurationScanner();
        return me.scanNamespace(namespace);
    }

    public ConfigurationScanner scanNamespace(String namespace) {
        final FastClasspathScanner scanner = new FastClasspathScanner(namespace)
                .matchClassesWithAnnotation(Index.class, c -> {
                    System.out.println("found a searchable-class: " + c.getName());
                    searchableClasses.add(c);
                });
        classInfoStream = scanner.scan()
                .getClassNameToClassInfo()
                .values()
                .stream()
                .filter(ci -> ci.hasAnnotation(Index.class.getName()))
                .collect(Collectors.toList());
        return this;
    }

    public List<CreateIndexResult> configure() throws Exception {
        classInfoStream.forEach(ci -> {
            Index s = getClass(ci).getAnnotation(Index.class);
            indices.add(s.index());
        });

        return indices.stream().map(index -> {
            List<ClassInfo> classInfoPerForIndex = classInfoStream.stream().filter(ci -> {
                Index s = getClass(ci).getAnnotation(Index.class);
                return s.index().equals(index);
            }).collect(Collectors.toList());

            final CreateIndexResult request = new CreateIndexResult(index);

            Optional<ClassInfo> analysis = classInfoPerForIndex.stream().filter(ci -> ci.hasAnnotation(Analysis.class.getName())).findFirst();
            analysis.ifPresent(a -> request.settings(AnalysisFactory.createAnalysis(getClass(a))));

            List<String> parents = new ArrayList<>(classInfoPerForIndex).stream()
                    .filter(ci -> {
                        Index s = getClass(ci).getAnnotation(Index.class);
                        return StringUtils.isNoneBlank(s.parent());
                    })
                    .map(ci -> getClass(ci).getSimpleName())
                    .collect(Collectors.toList());



            classInfoPerForIndex.forEach(ci -> {
                Index s = getClass(ci).getAnnotation(Index.class);
                request.
                        mapping(getClass(ci).getSimpleName(),
                                MappingFactory.createMapping(getClass(ci),
                                        s.disableDynamicProperties(),
                                        s.disableAllField(),
                                        Optional.ofNullable(s.parent()),
                                        Optional.empty()));
            });
            return request;
        }).collect(Collectors.toList());
    }

    private Class<?> getClass(ClassInfo ci) {
        try {
            return Class.forName(ci.getClassName());
        } catch (Exception e) {
            log.error("cannot find class", e);
        }
        return null;
    }
}
