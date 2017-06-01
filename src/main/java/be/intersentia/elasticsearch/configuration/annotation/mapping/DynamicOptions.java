package be.intersentia.elasticsearch.configuration.annotation.mapping;

/**
 * What information should be stored in the index, for search and highlighting purposes. Defaults to positions.
 */
public enum DynamicOptions {
    /**
     * Newly detected fields are added to the mapping. (default)
     */
    TRUE,

    /**
     * Newly detected fields are ignored. New fields must be added explicitly.
     */
    FALSE,

    /**
     * If new fields are detected, an exception is thrown and the document is rejected.
     */
    STRICT
}