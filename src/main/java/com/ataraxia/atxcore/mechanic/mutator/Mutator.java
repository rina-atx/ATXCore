package com.ataraxia.atxcore.mechanic.mutator;

import com.ataraxia.atxcore.api.Identified;
import com.ataraxia.atxcore.mechanic.ExecutionContext;

public interface Mutator<C extends ExecutionContext> extends Identified {
    C mutate(C context);
}
