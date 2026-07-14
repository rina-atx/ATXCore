package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.filter.Filter;

import java.util.function.Predicate;

public final class SimpleFilter implements Filter<ExecutionContext> {
    private final String id;
    private final Predicate<ExecutionContext> predicate;

    public SimpleFilter(String id, Predicate<ExecutionContext> predicate) {
        this.id = id;
        this.predicate = predicate;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean allow(ExecutionContext context) {
        return predicate.test(context);
    }
}
