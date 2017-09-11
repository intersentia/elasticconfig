package be.intersentia.elasticsearch.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CreateIndexResult {
    private final String index;
    private  Map settings;
    private  Map<String,Map> mapping = new TreeMap<>();

    public CreateIndexResult(String index) {
        this.index = index;
    }


    public String getIndex() {
        return index;
    }

    public Map getSettings() {
        return settings;
    }

    public CreateIndexResult settings(Map settings) {
        this.settings = settings;
        return this;
    }

    public CreateIndexResult mapping(String type, Map mapping) {
        this.mapping.put(type, mapping);
        return this;
    }

    public Map<String, Map> getMapping() {
        return mapping;
    }
}
