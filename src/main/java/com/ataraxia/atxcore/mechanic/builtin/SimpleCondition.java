package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.condition.Condition;
import com.ataraxia.atxcore.mechanic.condition.ConditionResult;

import java.util.function.Function;

public final class SimpleCondition implements Condition<ExecutionContext> {
    private final String id;
    private final Function<ExecutionContext, ConditionResult> test;

    public SimpleCondition(String id, Function<ExecutionContext, ConditionResult> test) {
        this.id = id;
        this.test = test;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public ConditionResult test(ExecutionContext context) {
        return test.apply(context);
    }
}
