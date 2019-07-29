package be.intersentia.elasticsearch.configuration;

import java.util.Map;

public class CreateIndexResult {
    private final String index;
    private  Map<String, ?> settings;
    private  Map<String, ?> mapping;

    public CreateIndexResult(String index, Map<String, ?> settings, Map<String, ?> mapping) {
        this.index = index;
        this.settings = settings;
        this.mapping = mapping;
    }

    public String getIndex() {
        return index;
    }

    public Map<String, ?> getSettings() {
        return settings;
    }

    public Map<String, ?> getMapping() {
        return mapping;
    }
}
