package com.ataraxia.atxcore.integration;

import com.ataraxia.atxcore.api.Identified;

public interface Integration extends Identified {
    boolean available();

    void enable();

    void disable();
}
