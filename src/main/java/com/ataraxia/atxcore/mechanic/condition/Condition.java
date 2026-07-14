package com.ataraxia.atxcore.mechanic.condition;

import com.ataraxia.atxcore.api.Identified;
import com.ataraxia.atxcore.mechanic.ExecutionContext;

public interface Condition<C extends ExecutionContext> extends Identified {
    ConditionResult test(C context);
}
