package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.effect.Effect;

import java.util.function.Consumer;

public final class SimpleEffect implements Effect<ExecutionContext> {
    private final String id;
    private final Consumer<ExecutionContext> action;

    public SimpleEffect(String id, Consumer<ExecutionContext> action) {
        this.id = id;
        this.action = action;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void execute(ExecutionContext context) {
        action.accept(context);
    }
}
