package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.condition.Condition;
import com.ataraxia.atxcore.mechanic.condition.ConditionResult;
import com.ataraxia.atxcore.placeholder.PlaceholderService;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class BuiltinConditions {
    private BuiltinConditions() {
    }

    public static void register(CoreRegistry<Condition<?>> registry, PlaceholderService placeholders) {
        registry.register(new SimpleCondition("permission", context -> {
            String permission = BuiltinValue.string(context, "permission", "");
            return context.sender().filter(sender -> sender.hasPermission(permission))
                    .map(sender -> ConditionResult.pass())
                    .orElseGet(() -> ConditionResult.fail("Missing permission " + permission));
        }));
        registry.register(new SimpleCondition("chance", context ->
                ThreadLocalRandom.current().nextDouble() <= BuiltinValue.decimal(context, "chance", 1)
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Chance failed")));
        registry.register(new SimpleCondition("placeholder_equals", context -> {
            String left = placeholders.apply(BuiltinValue.string(context, "left", ""), context);
            String right = placeholders.apply(BuiltinValue.string(context, "right", ""), context);
            return left.equalsIgnoreCase(right) ? ConditionResult.pass() : ConditionResult.fail(left + " did not equal " + right);
        }));

        registry.register(new SimpleCondition("player_online", context -> context.player().isPresent() ? ConditionResult.pass() : ConditionResult.fail("No player")));
        registry.register(new SimpleCondition("actor_player", context -> context.actorPlayer().isPresent() ? ConditionResult.pass() : ConditionResult.fail("Actor is not a player")));
        registry.register(new SimpleCondition("target_player", context -> context.targetPlayer().isPresent() ? ConditionResult.pass() : ConditionResult.fail("Target is not a player")));
        registry.register(new SimpleCondition("actor_health_above", context ->
                context.actorPlayer().filter(player -> player.getHealth() > BuiltinValue.decimal(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Actor health too low")));
        registry.register(new SimpleCondition("target_health_above", context ->
                context.targetPlayer().filter(player -> player.getHealth() > BuiltinValue.decimal(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Target health too low")));
        registry.register(new SimpleCondition("actor_has_permission", context -> {
            String permission = BuiltinValue.string(context, "permission", "");
            return context.actorPlayer().filter(player -> player.hasPermission(permission)).isPresent()
                    ? ConditionResult.pass()
                    : ConditionResult.fail("Actor missing permission " + permission);
        }));
        registry.register(new SimpleCondition("world", context ->
                context.world().filter(world -> world.getName().equalsIgnoreCase(BuiltinValue.string(context, "world", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong world")));
        registry.register(new SimpleCondition("gamemode", context ->
                context.player().filter(player -> player.getGameMode().name().equalsIgnoreCase(BuiltinValue.string(context, "mode", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong gamemode")));
        registry.register(new SimpleCondition("health_above", context ->
                context.player().filter(player -> player.getHealth() > BuiltinValue.decimal(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Health too low")));
        registry.register(new SimpleCondition("health_below", context ->
                context.player().filter(player -> player.getHealth() < BuiltinValue.decimal(context, "amount", 20)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Health too high")));
        registry.register(new SimpleCondition("food_above", context ->
                context.player().filter(player -> player.getFoodLevel() > BuiltinValue.integer(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Food too low")));
        registry.register(new SimpleCondition("food_below", context ->
                context.player().filter(player -> player.getFoodLevel() < BuiltinValue.integer(context, "amount", 20)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Food too high")));
        registry.register(new SimpleCondition("level_at_least", context ->
                context.player().filter(player -> player.getLevel() >= BuiltinValue.integer(context, "level", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Level too low")));
        registry.register(new SimpleCondition("level_below", context ->
                context.player().filter(player -> player.getLevel() < BuiltinValue.integer(context, "level", 1)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Level too high")));
        registry.register(new SimpleCondition("has_item", context ->
                context.player().filter(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().contains(material, Math.max(1, BuiltinValue.integer(context, "amount", 1)));
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Missing item")));
        registry.register(new SimpleCondition("holding_item", context ->
                context.player().filter(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().getItemInMainHand().getType() == material;
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Not holding item")));
        registry.register(new SimpleCondition("sneaking", context ->
                context.player().filter(player -> player.isSneaking() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Sneaking state mismatch")));
        registry.register(new SimpleCondition("sprinting", context ->
                context.player().filter(player -> player.isSprinting() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Sprinting state mismatch")));
        registry.register(new SimpleCondition("flying", context ->
                context.player().filter(player -> player.isFlying() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Flying state mismatch")));
        registry.register(new SimpleCondition("op", context ->
                context.sender().filter(sender -> sender.isOp() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Operator state mismatch")));
        registry.register(new SimpleCondition("has_potion", context ->
                context.player().filter(player -> {
                    PotionEffectType type = PotionEffectType.getByName(BuiltinValue.string(context, "type", "SPEED").toUpperCase(Locale.ROOT));
                    return type != null && player.hasPotionEffect(type);
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Missing potion")));
        registry.register(new SimpleCondition("in_region_box", context ->
                context.location().filter(location ->
                        location.getX() >= Math.min(BuiltinValue.decimal(context, "x1", 0), BuiltinValue.decimal(context, "x2", 0))
                                && location.getX() <= Math.max(BuiltinValue.decimal(context, "x1", 0), BuiltinValue.decimal(context, "x2", 0))
                                && location.getY() >= Math.min(BuiltinValue.decimal(context, "y1", 0), BuiltinValue.decimal(context, "y2", 0))
                                && location.getY() <= Math.max(BuiltinValue.decimal(context, "y1", 0), BuiltinValue.decimal(context, "y2", 0))
                                && location.getZ() >= Math.min(BuiltinValue.decimal(context, "z1", 0), BuiltinValue.decimal(context, "z2", 0))
                                && location.getZ() <= Math.max(BuiltinValue.decimal(context, "z1", 0), BuiltinValue.decimal(context, "z2", 0))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Outside region")));
        registry.register(new SimpleCondition("data_exists", context ->
                context.data().containsKey(BuiltinValue.string(context, "key", ""))
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Data key missing")));
        registry.register(new SimpleCondition("data_equals", context -> {
            String actual = context.stringData(BuiltinValue.string(context, "key", "")).orElse("");
            String expected = BuiltinValue.string(context, "value", "");
            return actual.equalsIgnoreCase(expected) ? ConditionResult.pass() : ConditionResult.fail("Data mismatch");
        }));
        registry.register(new SimpleCondition("string_contains", context -> {
            String text = placeholders.apply(BuiltinValue.string(context, "text", ""), context).toLowerCase(Locale.ROOT);
            String needle = placeholders.apply(BuiltinValue.string(context, "contains", ""), context).toLowerCase(Locale.ROOT);
            return text.contains(needle) ? ConditionResult.pass() : ConditionResult.fail("Text missing value");
        }));
        registry.register(new SimpleCondition("number_greater", context ->
                BuiltinValue.decimal(context, "left", 0) > BuiltinValue.decimal(context, "right", 0)
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Number not greater")));
        registry.register(new SimpleCondition("number_less", context ->
                BuiltinValue.decimal(context, "left", 0) < BuiltinValue.decimal(context, "right", 0)
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Number not less")));
        registry.register(new SimpleCondition("biome", context ->
                context.location().filter(location -> location.getBlock().getBiome().name().equalsIgnoreCase(BuiltinValue.string(context, "biome", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong biome")));
        registry.register(new SimpleCondition("dimension", context ->
                context.world().filter(world -> world.getEnvironment().name().equalsIgnoreCase(BuiltinValue.string(context, "environment", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong dimension")));
        registry.register(new SimpleCondition("weather", context ->
                context.world().filter(world -> {
                    String weather = BuiltinValue.string(context, "weather", "clear").toLowerCase(Locale.ROOT);
                    return weather.equals("clear") ? !world.hasStorm() : weather.equals("thunder") ? world.isThundering() : world.hasStorm();
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Wrong weather")));
        registry.register(new SimpleCondition("time_between", context ->
                context.world().filter(world -> {
                    long time = world.getTime();
                    long min = BuiltinValue.integer(context, "min", 0);
                    long max = BuiltinValue.integer(context, "max", 24000);
                    return time >= min && time <= max;
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Time outside range")));
        registry.register(new SimpleCondition("attribute_at_least", context ->
                attributeValue(context, BuiltinValue.string(context, "attribute", "")).filter(value -> value >= BuiltinValue.decimal(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Attribute too low")));
        registry.register(new SimpleCondition("attribute_below", context ->
                attributeValue(context, BuiltinValue.string(context, "attribute", "")).filter(value -> value < BuiltinValue.decimal(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Attribute too high")));
        registry.register(new SimpleCondition("max_health_at_least", context ->
                attributeValue(context, "MAX_HEALTH").filter(value -> value >= BuiltinValue.decimal(context, "amount", 20)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Max health too low")));
        registry.register(new SimpleCondition("attack_damage_at_least", context ->
                attributeValue(context, "ATTACK_DAMAGE").filter(value -> value >= BuiltinValue.decimal(context, "amount", 1)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Attack damage too low")));
        registry.register(new SimpleCondition("movement_speed_at_least", context ->
                attributeValue(context, "MOVEMENT_SPEED").filter(value -> value >= BuiltinValue.decimal(context, "amount", 0.1)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Movement speed too low")));
        registry.register(new SimpleCondition("stat_at_least", context ->
                statisticValue(context).filter(value -> value >= BuiltinValue.integer(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Statistic too low")));
        registry.register(new SimpleCondition("stat_below", context ->
                statisticValue(context).filter(value -> value < BuiltinValue.integer(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Statistic too high")));
        registry.register(new SimpleCondition("vault_has_money", context -> context.player()
                .flatMap(player -> com.ataraxia.atxcore.ATXCorePlugin.getPlugin(com.ataraxia.atxcore.ATXCorePlugin.class).integrations().vault()
                        .flatMap(vault -> vault.economy().map(economy -> economy.has(player, BuiltinValue.decimal(context, "amount", 0)))))
                .filter(Boolean::booleanValue)
                .map(value -> ConditionResult.pass())
                .orElseGet(() -> ConditionResult.fail("Not enough money"))));
        registry.register(new SimpleCondition("vault_balance_at_least", context -> context.player()
                .flatMap(player -> com.ataraxia.atxcore.ATXCorePlugin.getPlugin(com.ataraxia.atxcore.ATXCorePlugin.class).integrations().vault()
                        .flatMap(vault -> vault.economy().map(economy -> economy.getBalance(player) >= BuiltinValue.decimal(context, "amount", 0))))
                .filter(Boolean::booleanValue)
                .map(value -> ConditionResult.pass())
                .orElseGet(() -> ConditionResult.fail("Balance too low"))));
    }

    private static java.util.Optional<Double> attributeValue(ExecutionContext context, String attributeName) {
        return context.entity().flatMap(entity -> BuiltinAttribute.value(entity, attributeName));
    }

    private static java.util.Optional<Integer> statisticValue(ExecutionContext context) {
        return context.player().flatMap(player -> {
            try {
                Statistic statistic = Statistic.valueOf(BuiltinValue.string(context, "stat", "").toUpperCase(Locale.ROOT));
                Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                String entityName = BuiltinValue.string(context, "entity_type", "");
                if (material != null) {
                    return java.util.Optional.of(player.getStatistic(statistic, material));
                }
                if (!entityName.isBlank()) {
                    return java.util.Optional.of(player.getStatistic(statistic, EntityType.valueOf(entityName.toUpperCase(Locale.ROOT))));
                }
                return java.util.Optional.of(player.getStatistic(statistic));
            } catch (IllegalArgumentException exception) {
                return java.util.Optional.empty();
            }
        });
    }
}
