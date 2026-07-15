package com.ataraxia.atxcore.mechanic.builtin;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

final class BuiltinAttribute {
    private BuiltinAttribute() {
    }

    static Optional<Attribute> resolve(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }
        String normalized = input.trim().toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_')
                .replace("MINECRAFT:", "");
        return resolveExact(normalized)
                .or(() -> resolveExact("GENERIC_" + normalized))
                .or(() -> resolveExact(normalized.replace("GENERIC_", "")));
    }

    static Optional<AttributeInstance> instance(Entity entity, String input) {
        if (!(entity instanceof Attributable attributable)) {
            return Optional.empty();
        }
        return resolve(input).map(attributable::getAttribute);
    }

    static Optional<Double> value(Entity entity, String input) {
        return instance(entity, input).map(AttributeInstance::getValue);
    }

    static void set(Entity entity, String input, double value) {
        edit(entity, input, instance -> instance.setBaseValue(nonNegative(value)));
    }

    static void add(Entity entity, String input, double amount) {
        edit(entity, input, instance -> instance.setBaseValue(nonNegative(instance.getBaseValue() + amount)));
    }

    static void reset(Entity entity, String input) {
        edit(entity, input, instance -> instance.setBaseValue(instance.getDefaultValue()));
    }

    static void edit(Entity entity, String input, Consumer<AttributeInstance> action) {
        try {
            instance(entity, input).ifPresent(action);
        } catch (RuntimeException ignored) {
            // Bad values are ignored so malformed config cannot crash the trigger chain.
        }
    }

    private static Optional<Attribute> resolveExact(String name) {
        try {
            return Optional.of(Attribute.valueOf(name));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    private static double nonNegative(double value) {
        if (Double.isNaN(value) || value == Double.NEGATIVE_INFINITY) {
            return 0;
        }
        if (value == Double.POSITIVE_INFINITY) {
            return Double.MAX_VALUE;
        }
        return Math.max(0, value);
    }
}
