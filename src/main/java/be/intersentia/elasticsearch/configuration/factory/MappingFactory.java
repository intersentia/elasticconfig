package be.intersentia.elasticsearch.configuration.factory;

import be.intersentia.elasticsearch.configuration.annotation.templates.DynamicTemplate;
import be.intersentia.elasticsearch.configuration.parser.mapping.*;
import be.intersentia.elasticsearch.configuration.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  This class is responsible for transforming one or more classes annotated with various types of ElasticSearch Mapping
 *  annotations into a Map object the ElasticSearch client understands.
 */
@SuppressWarnings("unused")
public class MappingFactory {
    private static final Logger log = LogManager.getLogger(MappingFactory.class);
    private static Map<String, AtomicInteger> recursiveCounts = new HashMap<>();

    public static Map<String, ?> createMapping(List<Class<?>> classes, boolean disableDynamicProperties) {
        return createMapping(classes, disableDynamicProperties, null, null);
    }

    public static Map<String, ?> createMapping(List<Class<?>> classes, boolean disableDynamicProperties, String parent) {
        return createMapping(classes, disableDynamicProperties, parent, null);
    }

    public static Map<String, ?> createMapping(List<Class<?>> classes, boolean disableDynamicProperties, Class<?> parentClass) {
        return createMapping(classes, disableDynamicProperties, null, parentClass);
    }

    public static Map<String, ?> createMapping(List<Class<?>> classes, boolean disableDynamicProperties, String parent,
                                               Class<?> parentClass) {
        String label = StringUtils.join(classes, Class::getSimpleName, ", ");
        log.info("Creating ElasticSearch mapping for " + label);
        Map<String, Object> map = new HashMap<>();
        if (disableDynamicProperties) {
            map.put("dynamic", "strict");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(parent)) {
            map.put("_parent", Collections.singletonMap("type", parent));
        }

        List<Map<String, Object>> templatesList = new ArrayList<>();
        Map<String, Object> propertiesMap = new HashMap<>();

        for (Class<?> clazz : classes) {
            log.trace(clazz.getSimpleName() + ": Inspecting annotations");
            Annotation[] annotations = clazz.getDeclaredAnnotations();

            addTemplates(templatesList, clazz, annotations);
            addMapping(propertiesMap, clazz, null, annotations);

            for (Field field : getFields(clazz)) {
                log.trace(clazz.getSimpleName() + '.' + field.getName() + ": Inspecting annotations");
                annotations = field.getDeclaredAnnotations();
                String fullName = (parentClass == null ? "root" : parentClass.getSimpleName())
                        + "." + clazz.getSimpleName() + '.' + field.getName();

                if (isRecursive(field, clazz)) {
                    AtomicInteger count = recursiveCounts.getOrDefault(fullName, new AtomicInteger(0));
                    count.incrementAndGet();
                    recursiveCounts.put(fullName, count);
                    if (count.intValue() < 5) addMapping(propertiesMap, clazz, field, annotations);
                    else {
                        log.warn("ignoring " + fullName);
                        recursiveCounts.put(fullName, new AtomicInteger(0));
                    }
                } else addMapping(propertiesMap, clazz, field, annotations);
            }
        }

        if (!templatesList.isEmpty()) {
            map.put("dynamic_templates", templatesList);
        }
        if (!propertiesMap.isEmpty()) {
            map.put("properties", propertiesMap);
        }

        if (log.isDebugEnabled()) {
            log.debug("Returning mapping for " + StringUtils.prettyPrint(label, map));
        }
        return map;
    }

    private static boolean isRecursive(Field field, Class<?> clazz) {
        boolean result = false;
        try {
            Type type = field.getGenericType();
            if (type.getClass().equals(clazz)) result = true;
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                for (Type t : pt.getActualTypeArguments()) {
                    if (t.equals(clazz)) {
                        result = true;
                        break;
                    }
                }
            }
        } catch(Exception e) {
            log.error(e);
        }
        return result;
    }

    private static void addTemplates(List<Map<String, Object>> templatesList, Class<?> clazz, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            log.trace(clazz.getSimpleName() + ": Inspecting @" + annotation.annotationType().getSimpleName());
            try {
                MultipleTemplateParserConfiguration multipleParserConfiguration = annotation.annotationType()
                        .getAnnotation(MultipleTemplateParserConfiguration.class);
                if (multipleParserConfiguration != null) {
                    for (Annotation subAnnotation : (Annotation[]) getValue(annotation, "value")) {
                        addTemplate(templatesList, clazz, subAnnotation);
                    }
                } else {
                    addTemplate(templatesList, clazz, annotation);
                }
            } catch (Exception e) {
                throw new IllegalStateException("Could not create MappingParser for template "
                        + clazz.getSimpleName(), e);
            }
        }
    }

    private static void addTemplate(List<Map<String, Object>> templatesList, Class<?> clazz, Annotation annotation)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        TemplateParserConfiguration mappingConfiguration = annotation.annotationType()
                .getAnnotation(TemplateParserConfiguration.class);
        if (mappingConfiguration != null) {
            DynamicTemplate template = getValue(annotation, "template");
            Annotation mapping = getValue(annotation, "mapping");
            AbstractMappingParser parser = mappingConfiguration.parser().getConstructor(Class.class, Field.class,
                    mapping.annotationType()).newInstance(clazz, null, mapping);
            Map<String, Object> templateMap = new HashMap<>();
            new TemplateParser(clazz, template, parser, mapping).addTemplate(templateMap);
            templatesList.add(templateMap);
        }
    }

    /**
     * Returns all non-transient, non-static fields of the given class, including inherited fields.
     */
    private static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * Loop over all annotations of a field, determine which ElasticSearch Mapping annotations are present,
     * and which MappingParsers can be used to read them.
     */
    private static void addMapping(Map<String, Object> map, Class<?> clazz, Field field,
                                                Annotation[] annotations) {
        AbstractMappingParser<?> baseParser = null;
        List<AbstractMappingParser<?>> nestedParsers = new ArrayList<>();
        for (Annotation annotation : annotations) {
            log.trace(getName(clazz, field) + ": Inspecting @" + annotation.annotationType().getSimpleName());
            try {
                List<AbstractMappingParser<?>> annotationParsers = getParsers(clazz, field, annotation);
                for (AbstractMappingParser<?> parser : annotationParsers) {
                    if (field == null) {
                        parser.addMapping(map, nestedParsers, false);
                    } else if (parser.hasDefault()) {
                        if (baseParser != null) {
                            throw new IllegalStateException("More than one DEFAULT Mapping found for field "
                                    + getName(clazz, field));
                        }
                        baseParser = parser;
                    } else {
                        nestedParsers.add(parser);
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("Could not create MappingParser for mapped field "
                        + getName(clazz, field), e);
            }
        }
        if (baseParser == null && !nestedParsers.isEmpty()) {
            throw new IllegalStateException("At least one DEFAULT Mapping is required for field " + getName(clazz, field));
        }
        if (baseParser != null) {
            baseParser.addMapping(map, nestedParsers, false);
        }
    }

    private static List<AbstractMappingParser<?>> getParsers(Class<?> clazz, Field field, Annotation annotation)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<AbstractMappingParser<?>> parsers = new ArrayList<>();
        MappingParserConfiguration mappingConfiguration = annotation.annotationType()
                .getAnnotation(MappingParserConfiguration.class);
        if (mappingConfiguration != null) {
            log.trace(getName(clazz, field) + ": Parsing with " + mappingConfiguration.parser().getSimpleName());
            parsers.add(mappingConfiguration.parser().getConstructor(Class.class, Field.class,
                    annotation.annotationType()).newInstance(clazz, field, annotation));
        }
        MultipleMappingParserConfiguration multipleParserConfiguration = annotation.annotationType()
                .getAnnotation(MultipleMappingParserConfiguration.class);
        if (multipleParserConfiguration != null) {
            log.trace(getName(clazz, field) + ": Parsing multiple with " + multipleParserConfiguration.parser().getSimpleName());
            parsers.add(multipleParserConfiguration.parser().getConstructor(Class.class, Field.class,
                    annotation.annotationType()).newInstance(clazz, field, annotation));
        }
        return parsers;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getValue(Annotation annotation, String member) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (T) annotation.annotationType().getDeclaredMethod(member).invoke(annotation);
    }

    private static String getName(Class<?> clazz, Field field) {
        return (field == null ? clazz.getSimpleName() : clazz.getSimpleName() + '.' + field.getName());
    }
}
