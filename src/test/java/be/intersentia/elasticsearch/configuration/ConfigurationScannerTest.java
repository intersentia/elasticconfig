package be.intersentia.elasticsearch.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConfigurationScannerTest {
    private static Logger log = LogManager.getLogger(ConfigurationScannerTest.class);

    @Test
    public void testScan() {
        ConfigurationScanner scanner = ConfigurationScanner.scan("be.intersentia.elasticsearch");
        List<CreateIndexResult> results = scanner.configure();
        assertThat(scanner.getIndices().size(), is(2));
        results.forEach(i -> log.info(i.getMapping()));
    }

    @Test
    public void testConfigure() {
        ConfigurationScanner scanner = ConfigurationScanner.scan("be.intersentia.elasticsearch");
        List<CreateIndexResult> results = scanner.configure();
        CreateIndexResult result1 = getResult(results, "TestModel");
        assertThat(result1.getMapping().size(), is(2));
        CreateIndexResult result2 = getResult(results, "TestModel2");
        assertThat(result2.getMapping().size(), is(3));
    }

    private CreateIndexResult getResult(List<CreateIndexResult> results, String index) {
        return results.stream()
                .filter((e) -> e.getIndex().equals(index))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No configuration for " + index));
    }
}
