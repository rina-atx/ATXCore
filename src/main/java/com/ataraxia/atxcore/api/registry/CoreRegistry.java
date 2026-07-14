package com.ataraxia.atxcore.api.registry;

import com.ataraxia.atxcore.api.Identified;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class CoreRegistry<T extends Identified> {
    private final String name;
    private final Map<String, T> values = new LinkedHashMap<>();

    public CoreRegistry(String name) {
        this.name = name;
    }

    public void register(T value) {
        String id = normalize(value.id());
        if (id.isBlank()) {
            throw new IllegalArgumentException(name + " id cannot be blank.");
        }
        if (values.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate " + name + " id: " + id);
        }
        values.put(id, value);
    }

    public Optional<T> get(String id) {
        return Optional.ofNullable(values.get(normalize(id)));
    }

    public Collection<T> values() {
        return values.values();
    }

    public int size() {
        return values.size();
    }

    private String normalize(String id) {
        return id.toLowerCase(Locale.ROOT).trim();
    }
}
