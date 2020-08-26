package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.MappingParser;
import be.intersentia.elasticsearch.configuration.parser.SearchAsYouTypeMappingParser;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The search_as_you_type field type is a text-like field that is optimized to provide out-of-the-box support for
 * queries that serve an as-you-type completion use case. It creates a series of subfields that are analyzed to index
 * terms that can be efficiently matched by a query that partially matches the entire indexed text value. Both prefix
 * completion (i.e matching terms starting at the beginning of the input) and infix completion (i.e. matching terms at
 * any position within the input) are supported.
 *
 * For more details see: https://www.elastic.co/guide/en/elasticsearch/reference/7.2/search-as-you-type.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(SearchAsYouTypeMappings.class)
@MappingParser(SearchAsYouTypeMappingParser.class)
public @interface SearchAsYouTypeMapping {

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
     * The largest shingle size to index the input with and create subfields for, creating one subfield for each shingle
     * size between 2 and maxShingleSize. Accepts integer values between 2 and 4 inclusive. This option defaults to 3.
     */
    int maxShingleSize() default 3;

    /**
     * The analyzer which should be used for analyzed string fields, both at index-time and at search-time (unless
     * overridden by the search_analyzer). Defaults to the standard analyzer.
     */
    String analyzer() default "DEFAULT";

    /**
     * The copy_to parameter allows you to create custom _all fields. In other words, the values of multiple fields can
     * be copied into a group field, which can then be queried as a single field. For instance, the first_name and
     * last_name fields can be copied to the full_name field.
     */
    String[] copyTo() default {};

    /**
     * Should the field be searchable? Accepts true (default) or false.
     */
    boolean index() default true;

    /**
     * What information should be stored in the index, for search and highlighting purposes. Defaults to positions.
     */
    IndexOptions indexOptions() default IndexOptions.DEFAULT;

    /**
     * Whether field-length should be taken into account when scoring queries. Accepts true or false. This option
     * configures the root field and shingle subfields, where its default is true. It does not configure the prefix
     * subfield, where it it false.
     */
    boolean norms() default true;

    /**
     * Whether the field value should be stored and retrievable separately from the _source field. Accepts true or
     * false (default). This option only configures the root field, and does not configure any subfields.
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
     * Which scoring algorithm or similarity should be used. Defaults to BM25.
     */
    String similarity() default "DEFAULT";

    /**
     * Whether term vectors should be stored for an analyzed field. Defaults to no. This option configures the root
     * field and shingle subfields, but not the prefix subfield.
     */
    TermVectorOptions termVector() default TermVectorOptions.NO;
}