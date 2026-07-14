package com.ataraxia.atxcore.placeholder;

import com.ataraxia.atxcore.api.Identified;
import com.ataraxia.atxcore.mechanic.ExecutionContext;

public interface PlaceholderProvider extends Identified {
    String resolve(String argument, ExecutionContext context);
}
