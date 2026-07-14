package com.ataraxia.atxcore.mechanic.trigger;

import com.ataraxia.atxcore.mechanic.ExecutionContext;

@FunctionalInterface
public interface TriggerHandler {
    void handle(ExecutionContext context);
}
