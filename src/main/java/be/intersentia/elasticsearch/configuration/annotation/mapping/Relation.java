package be.intersentia.elasticsearch.configuration.annotation.mapping;

/**
 * The Relation annotation specifies a single parent/child relation.
 * For more information see: https://www.elastic.co/guide/en/elasticsearch/reference/6.0/parent-join.html
 */
public @interface Relation {

    /**
     * The parent of the defined relationship.
     */
    String parent();

    /**
     * The child or children of the defined relationship.
     */
    String[] child();
}