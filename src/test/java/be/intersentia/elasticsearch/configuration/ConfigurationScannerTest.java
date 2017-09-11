package be.intersentia.elasticsearch.configuration;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConfigurationScannerTest {
    private static Logger log = Logger.getLogger(ConfigurationScannerTest.class);


    @Test
    public void testScan() throws Exception {
        ConfigurationScanner scanner = ConfigurationScanner.scan("be.intersentia.elasticsearch");

        assertThat(scanner.searchableClasses.size(), is(2));
    }

    @Test
    public void testConfigure() throws Exception {
        ConfigurationScanner scanner = ConfigurationScanner.scan("be.intersentia.elasticsearch");
        List<CreateIndexResult> indices = scanner.configure();

        indices.forEach(i -> log.info(i.getMapping()));
        assertThat(indices.size(), is(1));
        assertThat(indices.get(0).getMapping().size(), is(2));

    }
}
