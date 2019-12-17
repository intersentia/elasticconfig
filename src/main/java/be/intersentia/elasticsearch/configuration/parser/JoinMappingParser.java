package be.intersentia.elasticsearch.configuration.parser;

import be.intersentia.elasticsearch.configuration.annotation.mapping.Relation;
import be.intersentia.elasticsearch.configuration.annotation.mapping.JoinMapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for translating a JoinMapping to a Map object the ElasticSearch client understands.
 */
public class JoinMappingParser extends AbstractMappingParser<JoinMapping> {

    @SuppressWarnings("unused") // This constructor is called using reflection
    public JoinMappingParser(Class<?> clazz, Field field, JoinMapping annotation) {
        super(clazz, field, annotation);
    }

    @Override
    public String getFieldName() {
        return getFieldName(annotation.field());
    }

    @Override
    public String getMappingName() {
        return "DEFAULT";
    }

    @Override
    public String getType() {
        return "join";
    }

    @Override
    public void addMapping(Map<String, Object> mapping) {
        Map<String, List<String>> relations = new HashMap<>();
        for (Relation relation : annotation.relations()) {
            relations.put(relation.parent(), Arrays.asList(relation.child()));
        }
        mapping.put("relations", relations);
    }
}
