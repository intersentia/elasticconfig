package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.KeywordMappingParser;
import be.intersentia.elasticsearch.configuration.parser.MappingParserConfiguration;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines the field as requiring a Keyword mapping in ElasticSearch. A Keyword mapping is used to index
 * structured content such as email addresses, hostnames, status codes, zip codes or tags. They are typically used for
 * filtering (Find me all blog posts where status is published), for sorting, and for aggregations. Keyword fields are
 * only searchable by their exact value.
 *
 * If you need to index full text content such as email bodies or product descriptions, it is likely that you should
 * rather use a text field.
 *
 * For more details see: https://www.elastic.co/guide/en/elasticsearch/reference/5.2/keyword.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(KeywordMappings.class)
@MappingParserConfiguration(parser = KeywordMappingParser.class)
public @interface KeywordMapping {

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
     * Should the field be stored on disk in a column-stride fashion, so that it can later be used for sorting,
     * aggregations, or scripting? Accepts true (default) or false.
     */
    boolean docValues() default true;

    /**
     * Should global ordinals be loaded eagerly on refresh? Accepts true or false (default). Enabling this is a good
     * idea on fields that are frequently used for (significant) terms aggregations.
     */
    boolean eagerGlobalOrdinals() default false;

    /**
     * Do not index any string longer than this value. Defaults to 2147483647 so that all values would be accepted.
     */
    int ignoreAbove() default Integer.MAX_VALUE;

    /**
     * Whether or not the field value should be included in the _all field? Defaults to false if index is set to false,
     * or if a parent object field sets includeInAll to false. Otherwise defaults to true.
     * @deprecated Deleted in ElasticSearch 7.0
     */
    @Deprecated
    OptionalBoolean includeInAll() default OptionalBoolean.DEFAULT;

    /**
     * Should the field be searchable? Accepts true (default) or false.
     */
    boolean index() default true;

    /**
     *
     What information should be stored in the index, for scoring purposes. Defaults to docs but can also be set to freqs
     to take term frequency into account when computing scores.
     */
    IndexOptions indexOptions() default IndexOptions.DEFAULT;

    /**
     * Whether field-length should be taken into account when scoring queries. Accepts true or false (default).
     */
    boolean norms() default false;

    /**
     * The nullValue parameter allows you to replace explicit null values with the specified value so that it can be
     * indexed and searched. By default null values cannot be indexed or searched.
     */
    String nullValue() default "DEFAULT";

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
     * Which scoring algorithm or similarity should be used. Defaults to classic, which uses TF/IDF.
     */
    String similarity() default "DEFAULT";

    /**
     * How to pre-process the keyword prior to indexing. Defaults to null, meaning the keyword is kept as-is. This
     * functionality is experimental and may be changed or removed completely in a future release. Elastic will take a
     * best effort approach to fix any issues, but experimental features are not subject to the support SLA of official
     * GA features.
     */
    String normalizer() default "DEFAULT";

    /**
     * Whether full text queries should split the input on whitespace when building a query for this field. Accepts
     * true or false (default).
     * @since Added in ElasticSearch 7.0
     */
    OptionalBoolean splitQueriesOnWhitespace() default OptionalBoolean.FALSE;
}