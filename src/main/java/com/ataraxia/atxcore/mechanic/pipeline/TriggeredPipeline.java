package com.ataraxia.atxcore.mechanic.pipeline;

import com.ataraxia.atxcore.mechanic.trigger.Trigger;

public final class TriggeredPipeline {
    private final Trigger trigger;
    private final MechanicPipeline pipeline;
    private boolean running;

    public TriggeredPipeline(Trigger trigger, MechanicPipeline pipeline) {
        this.trigger = trigger;
        this.pipeline = pipeline;
    }

    public void start() {
        if (running) {
            return;
        }
        trigger.start(pipeline::run);
        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        trigger.stop();
        running = false;
    }

    public boolean running() {
        return running;
    }
}
