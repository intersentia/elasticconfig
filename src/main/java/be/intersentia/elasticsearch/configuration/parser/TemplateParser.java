package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.templates.DynamicTemplate;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for translating a DateMapping to a Map object the ElasticSearch client understands.
 */
public class TemplateParser {
    protected Class<?> clazz;
    protected DynamicTemplate template;
    protected AbstractMappingParser parser;
    protected Annotation mappingAnnotation;

    @SuppressWarnings("unused") // This constructor is called using reflection
    public TemplateParser(Class<?> clazz, DynamicTemplate template, AbstractMappingParser parser, Annotation mappingAnnotation) {
        this.clazz = clazz;
        this.template = template;
        this.parser = parser;
        this.mappingAnnotation = mappingAnnotation;
    }

    public void addTemplate(Map<String, Object> map) {
        Map<String, Object> annotationMap = new HashMap<>();
        if (!"DEFAULT".equals(template.matchMappingType())) {
            annotationMap.put("match_mapping_type", template.matchMappingType());
        }
        if (!"DEFAULT".equals(template.match())) {
            annotationMap.put("match", template.match());
        }
        if (!"DEFAULT".equals(template.unMatch())) {
            annotationMap.put("unmatch", template.unMatch());
        }
        if (!"DEFAULT".equals(template.pathMatch())) {
            annotationMap.put("path_match", template.pathMatch());
        }
        if (!"DEFAULT".equals(template.pathUnMatch())) {
            annotationMap.put("path_unmatch", template.pathUnMatch());
        }
        annotationMap.put("mapping", getMapping());
        map.put(template.name(), annotationMap);
    }

    private Map<String, Object> getMapping() {
        Map<String, Object> mappingMap = new HashMap<>();
        parser.addMapping(mappingMap, new ArrayList<>(), true);
        return (Map<String, Object>) mappingMap.get(parser.getMappingName(mappingAnnotation));
    }
}
