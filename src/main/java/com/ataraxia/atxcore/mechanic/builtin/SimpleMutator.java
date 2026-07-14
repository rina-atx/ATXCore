package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.mutator.Mutator;

import java.util.function.UnaryOperator;

public final class SimpleMutator implements Mutator<ExecutionContext> {
    private final String id;
    private final UnaryOperator<ExecutionContext> action;

    public SimpleMutator(String id, UnaryOperator<ExecutionContext> action) {
        this.id = id;
        this.action = action;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public ExecutionContext mutate(ExecutionContext context) {
        return action.apply(context);
    }
}
