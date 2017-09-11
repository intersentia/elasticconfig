package be.intersentia.elasticsearch.configuration.factory;

import be.intersentia.elasticsearch.configuration.annotation.templates.DynamicTemplate;
import be.intersentia.elasticsearch.configuration.parser.mapping.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 *  This class is responsible for transforming a class annotated with various types of ElasticSearch Mapping interfaces
 *  into a Map object the ElasticSearch client understands.
 */
public class MappingFactory {
    private static final Logger log = Logger.getLogger(MappingFactory.class);
    static Map<String, AtomicInteger> recursiveCounts = new HashMap<>();

    public static Map<String, ?> createMapping(Class<?> clazz, boolean disableDynamicProperties, boolean disableAllField, Optional<String> parent, Optional<Class<?>> parentClass) {

        log.info("Creating ElasticSearch mapping for " + clazz.getSimpleName());
        Map<String, Object> map = new HashMap<String, Object>();
        if (disableDynamicProperties) {
            map.put("dynamic", "strict");
        }
        if (disableAllField) {
            map.put("_all", Collections.singletonMap("enabled", "false"));
        }

        parent.filter(StringUtils::isNoneEmpty).ifPresent(p -> map.put("_parent", Collections.singletonMap("type", p)));

        List<Map<String, Object>> templatesList = new ArrayList<Map<String, Object>>();
        Map<String, Object> propertiesMap = new HashMap<String, Object>();

        log.trace(clazz.getSimpleName() + ": Inspecting annotations");
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        addTemplates(templatesList, clazz, annotations);
        addMapping(propertiesMap, clazz, null, annotations);



        for (Field field : getFields(clazz)) {
            log.trace(clazz.getSimpleName() + '.' + field.getName() + ": Inspecting annotations");
            annotations = field.getDeclaredAnnotations();
            String fullName = parentClass.map(Class::getSimpleName).orElse("root") + "." + clazz.getSimpleName() + '.' + field.getName();

            boolean isRecursive = isRecurive(field, clazz);
            if (isRecursive) {
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

        if (!templatesList.isEmpty()) {
            map.put("dynamic_templates", templatesList);
        }
        if (!propertiesMap.isEmpty()) {
            map.put("properties", propertiesMap);
        }

        ByteArrayOutputStream is = new ByteArrayOutputStream();
        if (log.isDebugEnabled()) {
            PrintStream stream = new PrintStream(is);
            MapUtils.verbosePrint(stream, clazz.getSimpleName(), map);
            stream.close();
            log.debug("Returning mapping for " + is.toString());
        }
        return map;
    }

    private static boolean isRecurive(Field field, Class<?> clazz) {
        boolean result = false;
        try {
            Type type = field.getGenericType();
            if (type.getClass().equals(clazz)) result = true;
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                for (Type t : pt.getActualTypeArguments()) {
                    if (t.equals(clazz)) result = true;
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
            Map<String, Object> templateMap = new HashMap<String, Object>();
            new TemplateParser(clazz, template, parser, mapping).addTemplate(templateMap);
            templatesList.add(templateMap);
        }
    }

    /**
     * Returns all non-transient, non-static fields of the given class, including inherited fields.
     */
    private static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
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
        List<AbstractMappingParser<?>> nestedParsers = new ArrayList<AbstractMappingParser<?>>();
        for (Annotation annotation : annotations) {
            log.trace(getName(clazz, field) + ": Inspecting @" + annotation.annotationType().getSimpleName());
            try {
                List<AbstractMappingParser> annotationParsers = getParsers(clazz, field, annotation);
                for (AbstractMappingParser parser : annotationParsers) {
                    if (field == null) {
                        parser.addMapping(map, nestedParsers, false);
                    } else  if (parser.hasDefault()) {
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

    private static List<AbstractMappingParser> getParsers(Class<?> clazz, Field field, Annotation annotation)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<AbstractMappingParser> parsers = new ArrayList<AbstractMappingParser>();
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

    private static <T> T getValue(Annotation annotation, String member) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (T) annotation.annotationType().getDeclaredMethod(member).invoke(annotation);
    }

    private static String getName(Class<?> clazz, Field field) {
        return (field == null ? clazz.getSimpleName() : clazz.getSimpleName() + '.' + field.getName());
    }
}
