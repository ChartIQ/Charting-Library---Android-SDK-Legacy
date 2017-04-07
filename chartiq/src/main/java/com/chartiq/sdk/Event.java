package com.chartiq.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

class Event {
    public String name;
    public Map<String, Object> properties = new LinkedHashMap<String, Object>();

    public Event(String name) {
        this.name = name;
    }

    public Event(String name, Map<String, Object> property) {
        this.name = name;
        this.properties = property;
    }

    public Event set(String key, Object value) {
        if (key != null && key.length() <= 256)
            properties.put(key, value);
        return this;
    }
}
