package be.intersentia.elasticsearch.configuration.annotation.mapping;

/**
 * The index_prefixes parameter enables the indexing of term prefixes to speed up prefix searches.
 * For more information see: https://www.elastic.co/guide/en/elasticsearch/reference/7.2/index-prefixes.html
 */
public @interface IndexPrefixes {

    /**
     * The minimum prefix length to index. Must be greater than 0, and defaults to 2. The value is inclusive.
     */
    int minChars() default 2;

    /**
     * The maximum prefix length to index. Must be less than 20, and defaults to 5. The value is inclusive.
     */
    int maxChars() default 5;
}