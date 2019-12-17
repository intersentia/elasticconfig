package be.intersentia.elasticsearch.configuration.model;

import be.intersentia.elasticsearch.configuration.annotation.mapping.*;
import be.intersentia.elasticsearch.configuration.factory.MappingFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestModel3 implements Serializable {
    @KeywordMapping(index = false, store = true)
    private Integer page;

    @TextMapping(store = true, termVector = TermVectorOptions.WITH_POSITIONS_OFFSETS)
    @TextMapping(mappingName = "quote", analyzer = "default_quote", termVector = TermVectorOptions.WITH_POSITIONS_OFFSETS)
    private String content;

    @Test
    public void testSubmapping() {
        Map<String, ?> mapping = MappingFactory.createMapping(Collections.singletonList(TestModel3.class),
                false, "TestModel2", TestModel2.class);
        assertThat(mapping.toString(), Matchers.containsString("quote"));
    }
}
