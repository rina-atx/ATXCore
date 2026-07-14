package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.placeholder.PlaceholderProvider;

import java.util.function.Function;

public final class SimplePlaceholder implements PlaceholderProvider {
    private final String id;
    private final Function<ExecutionContext, String> resolver;

    public SimplePlaceholder(String id, Function<ExecutionContext, String> resolver) {
        this.id = id;
        this.resolver = resolver;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String resolve(String argument, ExecutionContext context) {
        return resolver.apply(context);
    }
}
