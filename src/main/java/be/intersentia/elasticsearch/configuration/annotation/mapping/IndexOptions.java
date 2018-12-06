package be.intersentia.elasticsearch.configuration.annotation.mapping;

/**
 * What information should be stored in the index, for search and highlighting purposes. Defaults to positions.
 */
public enum IndexOptions {

    /**
     * Analyzed string fields use positions as the default, and all other fields use docs as the default.
     */
    DEFAULT,

    /**
     * Only the doc number is indexed. Can answer the question Does this term exist in this field?
     */
    docs,

    /**
     * Doc number and term frequencies are indexed. Term frequencies are used to score repeated terms higher than
     * single terms.
     */
    freqs,

    /**
     * Doc number, term frequencies, and term positions (or order) are indexed. Positions can be used for proximity
     * or phrase queries.
     */
    positions,

    /**
     * Doc number, term frequencies, positions, and start and end character offsets (which map the term back to the
     * original string) are indexed. Offsets are used by the postings highlighter.
     */
    offsets
}