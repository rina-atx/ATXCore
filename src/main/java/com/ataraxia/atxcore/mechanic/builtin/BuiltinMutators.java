package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.mutator.Mutator;

import java.util.Locale;

public final class BuiltinMutators {
    private BuiltinMutators() {
    }

    public static void register(CoreRegistry<Mutator<?>> registry) {
        registry.register(new SimpleMutator("atxcore:data", context -> {
            context.stringData("key").ifPresent(key -> context.data().put(key, BuiltinValue.string(context, "value", "")));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:remove_data", context -> {
            context.data().remove(BuiltinValue.string(context, "key", ""));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:clear_data", context -> {
            context.data().clear();
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:copy_data", context -> {
            String from = BuiltinValue.string(context, "from", "");
            String to = BuiltinValue.string(context, "to", "");
            if (context.data().containsKey(from)) {
                context.data().put(to, context.data().get(from));
            }
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:append_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, context.stringData(key).orElse("") + BuiltinValue.string(context, "append", ""));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:prepend_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, BuiltinValue.string(context, "prepend", "") + context.stringData(key).orElse(""));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:uppercase_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, context.stringData(key).orElse("").toUpperCase(Locale.ROOT));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:lowercase_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, context.stringData(key).orElse("").toLowerCase(Locale.ROOT));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:increment_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            double current = parse(context.stringData(key).orElse("0"));
            context.data().put(key, current + BuiltinValue.decimal(context, "amount", 1));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:multiply_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            double current = parse(context.stringData(key).orElse("0"));
            context.data().put(key, current * BuiltinValue.decimal(context, "amount", 1));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:clamp_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            double value = parse(context.stringData(key).orElse("0"));
            context.data().put(key, Math.max(BuiltinValue.decimal(context, "min", value), Math.min(BuiltinValue.decimal(context, "max", value), value)));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:set_message", context -> {
            context.data().put("message", BuiltinValue.string(context, "message", ""));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:set_command", context -> {
            context.data().put("command", BuiltinValue.string(context, "command", ""));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:set_amount", context -> {
            context.data().put("amount", BuiltinValue.string(context, "amount", "1"));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:set_world", context -> {
            context.data().put("world", BuiltinValue.string(context, "world", ""));
            return context;
        }));
        registry.register(new SimpleMutator("atxcore:set_coordinates", context -> {
            context.data().put("x", BuiltinValue.string(context, "x", "0"));
            context.data().put("y", BuiltinValue.string(context, "y", "0"));
            context.data().put("z", BuiltinValue.string(context, "z", "0"));
            return context;
        }));
    }

    private static double parse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }
}
