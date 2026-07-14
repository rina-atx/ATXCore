package com.ataraxia.atxcore.api.registry;

import com.ataraxia.atxcore.mechanic.condition.Condition;
import com.ataraxia.atxcore.mechanic.effect.Effect;
import com.ataraxia.atxcore.mechanic.filter.Filter;
import com.ataraxia.atxcore.mechanic.mutator.Mutator;
import com.ataraxia.atxcore.mechanic.trigger.Trigger;

public final class CoreRegistries {
    private final CoreRegistry<Effect<?>> effects = new CoreRegistry<>("effect");
    private final CoreRegistry<Trigger> triggers = new CoreRegistry<>("trigger");
    private final CoreRegistry<Mutator<?>> mutators = new CoreRegistry<>("mutator");
    private final CoreRegistry<Filter<?>> filters = new CoreRegistry<>("filter");
    private final CoreRegistry<Condition<?>> conditions = new CoreRegistry<>("condition");

    public CoreRegistry<Effect<?>> effects() {
        return effects;
    }

    public CoreRegistry<Trigger> triggers() {
        return triggers;
    }

    public CoreRegistry<Mutator<?>> mutators() {
        return mutators;
    }

    public CoreRegistry<Filter<?>> filters() {
        return filters;
    }

    public CoreRegistry<Condition<?>> conditions() {
        return conditions;
    }

    public int totalSize() {
        return effects.size() + triggers.size() + mutators.size() + filters.size() + conditions.size();
    }
}
