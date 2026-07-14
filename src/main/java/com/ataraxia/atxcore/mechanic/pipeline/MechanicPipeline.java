package com.ataraxia.atxcore.mechanic.pipeline;

import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.condition.Condition;
import com.ataraxia.atxcore.mechanic.effect.Effect;
import com.ataraxia.atxcore.mechanic.filter.Filter;
import com.ataraxia.atxcore.mechanic.mutator.Mutator;

import java.util.ArrayList;
import java.util.List;

public final class MechanicPipeline {
    private final List<Condition<ExecutionContext>> conditions = new ArrayList<>();
    private final List<Filter<ExecutionContext>> filters = new ArrayList<>();
    private final List<Mutator<ExecutionContext>> mutators = new ArrayList<>();
    private final List<Effect<ExecutionContext>> effects = new ArrayList<>();

    public MechanicPipeline condition(Condition<ExecutionContext> condition) {
        conditions.add(condition);
        return this;
    }

    public MechanicPipeline filter(Filter<ExecutionContext> filter) {
        filters.add(filter);
        return this;
    }

    public MechanicPipeline mutator(Mutator<ExecutionContext> mutator) {
        mutators.add(mutator);
        return this;
    }

    public MechanicPipeline effect(Effect<ExecutionContext> effect) {
        effects.add(effect);
        return this;
    }

    public void run(ExecutionContext initialContext) {
        ExecutionContext context = initialContext;
        for (Condition<ExecutionContext> condition : conditions) {
            if (!condition.test(context).passed()) {
                return;
            }
        }
        for (Filter<ExecutionContext> filter : filters) {
            if (!filter.allow(context)) {
                return;
            }
        }
        for (Mutator<ExecutionContext> mutator : mutators) {
            context = mutator.mutate(context);
        }
        for (Effect<ExecutionContext> effect : effects) {
            effect.execute(context);
        }
    }
}
