package com.ataraxia.atxcore.mechanic.filter;

import com.ataraxia.atxcore.api.Identified;
import com.ataraxia.atxcore.mechanic.ExecutionContext;

public interface Filter<C extends ExecutionContext> extends Identified {
    boolean allow(C context);
}
