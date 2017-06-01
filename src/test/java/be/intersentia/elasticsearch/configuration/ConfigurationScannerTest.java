package be.intersentia.elasticsearch.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConfigurationScannerTest {


    @Test
    public void testScan() throws Exception {
        ConfigurationScanner scanner = ConfigurationScanner.scan("be.intersentia.elasticsearch");

        assertThat(scanner.searchableClasses.size(), is(2));
    }

    @Test
    public void testConfigure() throws Exception {
        ConfigurationScanner scanner = ConfigurationScanner.scan("be.intersentia.elasticsearch");
        List<CreateIndexResult> indices = scanner.configure();

        assertThat(indices.size(), is(1));
        assertThat(indices.get(0).getMapping().size(), is(2));
    }
}
