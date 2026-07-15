package com.ataraxia.atxcore.mechanic.config;

import com.ataraxia.atxcore.api.ATXCoreApi;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.condition.Condition;
import com.ataraxia.atxcore.mechanic.effect.Effect;
import com.ataraxia.atxcore.mechanic.filter.Filter;
import com.ataraxia.atxcore.mechanic.mutator.Mutator;
import com.ataraxia.atxcore.mechanic.pipeline.MechanicPipeline;
import com.ataraxia.atxcore.mechanic.pipeline.TriggeredPipeline;
import com.ataraxia.atxcore.mechanic.trigger.Trigger;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class EcoStyleMechanicLoader {
    private final ATXCoreApi api;

    public EcoStyleMechanicLoader(ATXCoreApi api) {
        this.api = api;
    }

    public TriggeredPipeline loadTriggeredPipeline(ConfigurationSection section) {
        String triggerId = section.getString("trigger", "");
        Trigger trigger = api.registries().triggers().get(triggerId)
                .orElseThrow(() -> new MechanicConfigException("Unknown trigger: " + triggerId));
        return new TriggeredPipeline(trigger, loadPipeline(section));
    }

    public MechanicPipeline loadPipeline(ConfigurationSection section) {
        MechanicPipeline pipeline = new MechanicPipeline();
        addConditions(pipeline, section);
        addMutators(pipeline, section);
        addFilters(pipeline, section);
        addEffects(pipeline, section);
        return pipeline;
    }

    @SuppressWarnings("unchecked")
    private void addConditions(MechanicPipeline pipeline, ConfigurationSection section) {
        for (Step step : steps(section, "conditions")) {
            Condition<ExecutionContext> condition = (Condition<ExecutionContext>) api.registries().conditions().get(step.id())
                    .orElseThrow(() -> new MechanicConfigException("Unknown condition: " + step.id()));
            pipeline.condition(condition, step.parameters());
        }
    }

    @SuppressWarnings("unchecked")
    private void addMutators(MechanicPipeline pipeline, ConfigurationSection section) {
        for (Step step : steps(section, "mutators")) {
            Mutator<ExecutionContext> mutator = (Mutator<ExecutionContext>) api.registries().mutators().get(step.id())
                    .orElseThrow(() -> new MechanicConfigException("Unknown mutator: " + step.id()));
            pipeline.mutator(mutator, step.parameters());
        }
    }

    @SuppressWarnings("unchecked")
    private void addFilters(MechanicPipeline pipeline, ConfigurationSection section) {
        for (Step step : steps(section, "filters")) {
            Filter<ExecutionContext> filter = (Filter<ExecutionContext>) api.registries().filters().get(step.id())
                    .orElseThrow(() -> new MechanicConfigException("Unknown filter: " + step.id()));
            pipeline.filter(filter, step.parameters());
        }
    }

    @SuppressWarnings("unchecked")
    private void addEffects(MechanicPipeline pipeline, ConfigurationSection section) {
        for (Step step : steps(section, "effects")) {
            Effect<ExecutionContext> effect = (Effect<ExecutionContext>) api.registries().effects().get(step.id())
                    .orElseThrow(() -> new MechanicConfigException("Unknown effect: " + step.id()));
            pipeline.effect(effect, step.parameters());
        }
    }

    private List<Step> steps(ConfigurationSection section, String path) {
        return section.getMapList(path).stream()
                .map(this::step)
                .toList();
    }

    private Step step(Map<?, ?> raw) {
        Object id = raw.get("id");
        if (id == null) {
            id = raw.get("effect");
        }
        if (id == null) {
            id = raw.get("condition");
        }
        if (id == null) {
            id = raw.get("filter");
        }
        if (id == null) {
            id = raw.get("mutator");
        }
        if (id == null) {
            throw new MechanicConfigException("Mechanic step is missing id: " + raw);
        }
        Map<String, Object> parameters = new LinkedHashMap<>();
        Object data = raw.get("data");
        if (data instanceof Map<?, ?> dataMap) {
            dataMap.forEach((key, value) -> parameters.put(String.valueOf(key), value));
        }
        raw.forEach((key, value) -> {
            String stringKey = String.valueOf(key);
            if (!stringKey.equals("id")
                    && !stringKey.equals("effect")
                    && !stringKey.equals("condition")
                    && !stringKey.equals("filter")
                    && !stringKey.equals("mutator")
                    && !stringKey.equals("data")) {
                parameters.put(stringKey, value);
            }
        });
        return new Step(String.valueOf(id), parameters);
    }

    private record Step(String id, Map<String, Object> parameters) {
    }
}
