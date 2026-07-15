package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.mutator.Mutator;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class BuiltinMutators {
    private BuiltinMutators() {
    }

    public static void register(CoreRegistry<Mutator<?>> registry) {
        registry.register(new SimpleMutator("target_actor", context ->
                context.actor().map(actor -> context.toBuilder().activeEntity(actor).location(actor.getLocation()).build()).orElse(context)));
        registry.register(new SimpleMutator("target_attacker", context ->
                context.actor().map(actor -> context.toBuilder().activeEntity(actor).location(actor.getLocation()).build()).orElse(context)));
        registry.register(new SimpleMutator("target_self", context ->
                context.actor().map(actor -> context.toBuilder().activeEntity(actor).location(actor.getLocation()).build()).orElse(context)));
        registry.register(new SimpleMutator("target_victim", context ->
                context.target().map(target -> context.toBuilder().activeEntity(target).location(target.getLocation()).build()).orElse(context)));
        registry.register(new SimpleMutator("target_target", context ->
                context.target().map(target -> context.toBuilder().activeEntity(target).location(target.getLocation()).build()).orElse(context)));
        registry.register(new SimpleMutator("target_trigger_location", context ->
                context.toBuilder().location(context.location().orElse(null)).build()));
        registry.register(new SimpleMutator("actor_to_data", context -> {
            context.actor().ifPresent(actor -> {
                context.data().put("actor_uuid", actor.getUniqueId().toString());
                context.data().put("actor_type", actor.getType().name());
                if (actor instanceof org.bukkit.entity.Player player) {
                    context.data().put("actor", player.getName());
                }
            });
            return context;
        }));
        registry.register(new SimpleMutator("target_to_data", context -> {
            context.target().ifPresent(target -> {
                context.data().put("target_uuid", target.getUniqueId().toString());
                context.data().put("target_type", target.getType().name());
                if (target instanceof org.bukkit.entity.Player player) {
                    context.data().put("target", player.getName());
                }
            });
            return context;
        }));
        registry.register(new SimpleMutator("set_data", context -> {
            context.stringData("key").ifPresent(key -> context.data().put(key, BuiltinValue.string(context, "value", "")));
            return context;
        }));
        registry.register(new SimpleMutator("remove_data", context -> {
            context.data().remove(BuiltinValue.string(context, "key", ""));
            return context;
        }));
        registry.register(new SimpleMutator("clear_data", context -> {
            context.data().clear();
            return context;
        }));
        registry.register(new SimpleMutator("copy_data", context -> {
            String from = BuiltinValue.string(context, "from", "");
            String to = BuiltinValue.string(context, "to", "");
            if (context.data().containsKey(from)) {
                context.data().put(to, context.data().get(from));
            }
            return context;
        }));
        registry.register(new SimpleMutator("append_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, context.stringData(key).orElse("") + BuiltinValue.string(context, "append", ""));
            return context;
        }));
        registry.register(new SimpleMutator("prepend_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, BuiltinValue.string(context, "prepend", "") + context.stringData(key).orElse(""));
            return context;
        }));
        registry.register(new SimpleMutator("uppercase_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, context.stringData(key).orElse("").toUpperCase(Locale.ROOT));
            return context;
        }));
        registry.register(new SimpleMutator("lowercase_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, context.stringData(key).orElse("").toLowerCase(Locale.ROOT));
            return context;
        }));
        registry.register(new SimpleMutator("increment_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            double current = parse(context.stringData(key).orElse("0"));
            context.data().put(key, current + BuiltinValue.decimal(context, "amount", 1));
            return context;
        }));
        registry.register(new SimpleMutator("multiply_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            double current = parse(context.stringData(key).orElse("0"));
            context.data().put(key, current * BuiltinValue.decimal(context, "amount", 1));
            return context;
        }));
        registry.register(new SimpleMutator("clamp_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            double value = parse(context.stringData(key).orElse("0"));
            context.data().put(key, Math.max(BuiltinValue.decimal(context, "min", value), Math.min(BuiltinValue.decimal(context, "max", value), value)));
            return context;
        }));
        registry.register(new SimpleMutator("set_message", context -> {
            context.data().put("message", BuiltinValue.string(context, "message", ""));
            return context;
        }));
        registry.register(new SimpleMutator("set_command", context -> {
            context.data().put("command", BuiltinValue.string(context, "command", ""));
            return context;
        }));
        registry.register(new SimpleMutator("set_amount", context -> {
            context.data().put("amount", BuiltinValue.string(context, "amount", "1"));
            return context;
        }));
        registry.register(new SimpleMutator("set_world", context -> {
            context.data().put("world", BuiltinValue.string(context, "world", ""));
            return context;
        }));
        registry.register(new SimpleMutator("set_coordinates", context -> {
            context.data().put("x", BuiltinValue.string(context, "x", "0"));
            context.data().put("y", BuiltinValue.string(context, "y", "0"));
            context.data().put("z", BuiltinValue.string(context, "z", "0"));
            return context;
        }));
        registry.register(new SimpleMutator("data_if_absent", context -> {
            context.data().putIfAbsent(BuiltinValue.string(context, "key", ""), BuiltinValue.string(context, "value", ""));
            return context;
        }));
        registry.register(new SimpleMutator("toggle_boolean_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            boolean current = Boolean.parseBoolean(context.stringData(key).orElse("false"));
            context.data().put(key, String.valueOf(!current));
            return context;
        }));
        registry.register(new SimpleMutator("random_int_data", context -> {
            int min = BuiltinValue.integer(context, "min", 0);
            int max = BuiltinValue.integer(context, "max", 100);
            context.data().put(BuiltinValue.string(context, "key", "random"), ThreadLocalRandom.current().nextInt(Math.min(min, max), Math.max(min, max) + 1));
            return context;
        }));
        registry.register(new SimpleMutator("random_decimal_data", context -> {
            double min = BuiltinValue.decimal(context, "min", 0);
            double max = BuiltinValue.decimal(context, "max", 1);
            context.data().put(BuiltinValue.string(context, "key", "random"), ThreadLocalRandom.current().nextDouble(Math.min(min, max), Math.max(min, max)));
            return context;
        }));
        registry.register(new SimpleMutator("round_data", context -> {
            String key = BuiltinValue.string(context, "key", "");
            context.data().put(key, Math.round(parse(context.stringData(key).orElse("0"))));
            return context;
        }));
        registry.register(new SimpleMutator("player_to_data", context -> {
            context.player().ifPresent(player -> {
                context.data().put("player", player.getName());
                context.data().put("player_uuid", player.getUniqueId().toString());
                context.data().put("player_world", player.getWorld().getName());
            });
            return context;
        }));
        registry.register(new SimpleMutator("location_to_data", context -> {
            context.location().ifPresent(location -> {
                context.data().put("world", location.getWorld() == null ? "" : location.getWorld().getName());
                context.data().put("x", location.getX());
                context.data().put("y", location.getY());
                context.data().put("z", location.getZ());
                context.data().put("yaw", location.getYaw());
                context.data().put("pitch", location.getPitch());
            });
            return context;
        }));
        registry.register(new SimpleMutator("attribute_to_data", context -> {
            String key = BuiltinValue.string(context, "key", "attribute");
            context.entity()
                    .flatMap(entity -> BuiltinAttribute.value(entity, BuiltinValue.string(context, "attribute", "")))
                    .ifPresent(value -> context.data().put(key, value));
            return context;
        }));
        registry.register(new SimpleMutator("stat_to_data", context -> {
            String key = BuiltinValue.string(context, "key", "stat");
            context.player().ifPresent(player -> {
                try {
                    Statistic statistic = Statistic.valueOf(BuiltinValue.string(context, "stat", "").toUpperCase(Locale.ROOT));
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    String entityName = BuiltinValue.string(context, "entity_type", "");
                    if (material != null) {
                        context.data().put(key, player.getStatistic(statistic, material));
                    } else if (!entityName.isBlank()) {
                        context.data().put(key, player.getStatistic(statistic, EntityType.valueOf(entityName.toUpperCase(Locale.ROOT))));
                    } else {
                        context.data().put(key, player.getStatistic(statistic));
                    }
                } catch (IllegalArgumentException ignored) {
                }
            });
            return context;
        }));
        registry.register(new SimpleMutator("vault_balance_to_data", context -> {
            String key = BuiltinValue.string(context, "key", "balance");
            context.player().ifPresent(player -> com.ataraxia.atxcore.ATXCorePlugin.getPlugin(com.ataraxia.atxcore.ATXCorePlugin.class)
                    .integrations().vault()
                    .flatMap(vault -> vault.economy().map(economy -> economy.getBalance(player)))
                    .ifPresent(balance -> context.data().put(key, balance)));
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
