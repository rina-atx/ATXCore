package com.ataraxia.atxcore.mechanic.effect;

import com.ataraxia.atxcore.api.Identified;
import com.ataraxia.atxcore.mechanic.ExecutionContext;

public interface Effect<C extends ExecutionContext> extends Identified {
    void execute(C context);
}
