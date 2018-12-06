package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.mapping.MappingParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.mapping.TextMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines the field as requiring a Text mapping in ElasticSearch. A Text mapping is used to index full-text
 * values, such as the body of an email or the description of a product. These fields are analyzed, that is they are
 * passed through an analyzer to convert the string into a list of individual terms before being indexed. The analysis
 * process allows Elasticsearch to search for individual words within each full text field. Text fields are not used for
 * sorting and seldom used for aggregations (although the significant terms aggregation is a notable exception).
 *
 * If you need to index structured content such as email addresses, hostnames, status codes, or tags, it is likely that
 * you should rather use a keyword field.
 *
 * For more details see: https://www.elastic.co/guide/en/elasticsearch/reference/5.2/text.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MappingParserConfiguration(parser = TextMappingParser.class)
public @interface TextMapping {

    /**
     * The field() method allows you to specify the name of the Elastic Search field. When annotating a Java Class you
     * are required to provide a value for this method. When annotating a Java Field this method is optional: by default
     * the value is he same as the name of the annotated Java Field.
     */
    String field() default "DEFAULT";

    /**
     * The mappingName() method allows you to add multiple mapping annotations to the same field. No two mappings may
     * have the same name. There always needs to be exactly one annotation with the "default" name, which will be the
     * default mapping. All other annotations will be created as multi-fields underneath the default mapping.  This is
     * often useful to index the same field in different ways for different purposes. For instance, a String field could
     * be mapped as a TextMapping for full-text search, and as a KeywordMapping for sorting or aggregations.
     */
    String mappingName() default "DEFAULT";

    /**
     * The analyzer which should be used for analyzed string fields, both at index-time and at search-time (unless
     * overridden by the search_analyzer). Defaults to the standard analyzer.
     */
    String analyzer() default "DEFAULT";

    /**
     * Individual fields can be boosted automatically — count more towards the relevance score — at query time, with the
     * boost parameter. Accepts a floating point number, defaults to 1.0.
     */
    float boost() default 1.0f;

    /**
     * The copy_to parameter allows you to create custom _all fields. In other words, the values of multiple fields can
     * be copied into a group field, which can then be queried as a single field. For instance, the first_name and
     * last_name fields can be copied to the full_name field.
     */
    String[] copyTo() default {};

    /**
     * Should global ordinals be loaded eagerly on refresh? Accepts true or false (default). Enabling this is a good
     * idea on fields that are frequently used for (significant) terms aggregations.
     */
    boolean eagerGlobalOrdinals() default false;

    /**
     * Can the field use in-memory fieldData for sorting, aggregations, or scripting? Accepts true or false (default).
     */
    boolean fieldData() default false;

    /**
     * Expert settings which allow to decide which values to load in memory when fieldData is enabled. By default all
     * values are loaded.
     */
    FieldDataFrequencyFilter fieldDataFrequencyFilter() default @FieldDataFrequencyFilter();

    /**
     * Whether or not the field value should be included in the _all field? Defaults to false if index is set to false,
     * or if a parent object field sets includeInAll to false. Otherwise defaults to true.
     */
    OptionalBoolean includeInAll() default OptionalBoolean.DEFAULT;

    /**
     * Should the field be searchable? Accepts true (default) or false.
     */
    boolean index() default true;

    /**
     * What information should be stored in the index, for search and highlighting purposes. Defaults to positions.
     */
    IndexOptions indexOptions() default IndexOptions.DEFAULT;

    /**
     * Whether field-length should be taken into account when scoring queries. Accepts true (default) or false.
     */
    boolean norms() default true;

    /**
     * The number of fake term position which should be inserted between each element of an array of strings. Defaults
     * to 100. 100 was chosen because it prevents phrase queries with reasonably large slops (less than 100) from
     * matching terms across field values.
     */
    int positionIncrementGap() default 100;

    /**
     * Whether the field value should be stored and retrievable separately from the _source field. Accepts true or
     * false (default).
     */
    boolean store() default false;

    /**
     * The analyzer that should be used at search time on analyzed fields. Defaults to the analyzer setting.
     */
    String searchAnalyzer() default "DEFAULT";

    /**
     * The analyzer that should be used at search time when a phrase is encountered. Defaults to the search_analyzer
     * setting.
     */
    String searchQuoteAnalyzer() default "DEFAULT";

    /**
     * Which scoring algorithm or similarity should be used. Defaults to classic, which uses TF/IDF.
     */
    String similarity() default "DEFAULT";

    /**
     * Whether term vectors should be stored for an analyzed field. Defaults to no.
     */
    TermVectorOptions termVector() default TermVectorOptions.NO;

    /**
     * The frequency filter allows you to only load terms whose document frequency falls between a min and max value,
     * which can be expressed an absolute number (when the number is bigger than 1.0) or as a percentage (eg 0.01 is 1%
     * and 1.0 is 100%). Frequency is calculated per segment. Percentages are based on the number of docs which have a
     * value for the field, as opposed to all docs in the segment.
     */
    @interface FieldDataFrequencyFilter {
        float min() default 0;
        float max() default 0;
        int minSegmentSize() default 0;
    }
}