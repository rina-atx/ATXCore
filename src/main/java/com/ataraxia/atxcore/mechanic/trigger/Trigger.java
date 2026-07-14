package com.ataraxia.atxcore.mechanic.trigger;

import com.ataraxia.atxcore.api.Identified;

public interface Trigger extends Identified {
    void start(TriggerHandler handler);

    void stop();
}
