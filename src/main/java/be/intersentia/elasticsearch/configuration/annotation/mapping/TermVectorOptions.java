package be.intersentia.elasticsearch.configuration.annotation.mapping;

/**
 * Whether term vectors should be stored for an analyzed field.
 */
public enum TermVectorOptions {
    /**
     * No term vectors are stored. (default)
     */
    NO,

    /**
     * Just the terms in the field are stored.
     */
    YES,

    /**
     * Terms and positions are stored.
     */
    WITH_POSITIONS,

    /**
     * Terms and character offsets are stored.
     */
    WITH_OFFSETS,

    /**
     * Terms, positions, and character offsets are stored.
     */
    WITH_POSITIONS_OFFSETS;
}