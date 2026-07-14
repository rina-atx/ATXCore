package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.condition.Condition;
import com.ataraxia.atxcore.mechanic.condition.ConditionResult;
import com.ataraxia.atxcore.placeholder.PlaceholderService;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class BuiltinConditions {
    private BuiltinConditions() {
    }

    public static void register(CoreRegistry<Condition<?>> registry, PlaceholderService placeholders) {
        registry.register(new SimpleCondition("atxcore:permission", context -> {
            String permission = BuiltinValue.string(context, "permission", "");
            return context.sender().filter(sender -> sender.hasPermission(permission))
                    .map(sender -> ConditionResult.pass())
                    .orElseGet(() -> ConditionResult.fail("Missing permission " + permission));
        }));
        registry.register(new SimpleCondition("atxcore:chance", context ->
                ThreadLocalRandom.current().nextDouble() <= BuiltinValue.decimal(context, "chance", 1)
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Chance failed")));
        registry.register(new SimpleCondition("atxcore:placeholder_equals", context -> {
            String left = placeholders.apply(BuiltinValue.string(context, "left", ""), context);
            String right = placeholders.apply(BuiltinValue.string(context, "right", ""), context);
            return left.equalsIgnoreCase(right) ? ConditionResult.pass() : ConditionResult.fail(left + " did not equal " + right);
        }));

        registry.register(new SimpleCondition("atxcore:player_online", context -> context.player().isPresent() ? ConditionResult.pass() : ConditionResult.fail("No player")));
        registry.register(new SimpleCondition("atxcore:world", context ->
                context.world().filter(world -> world.getName().equalsIgnoreCase(BuiltinValue.string(context, "world", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong world")));
        registry.register(new SimpleCondition("atxcore:gamemode", context ->
                context.player().filter(player -> player.getGameMode().name().equalsIgnoreCase(BuiltinValue.string(context, "mode", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong gamemode")));
        registry.register(new SimpleCondition("atxcore:health_above", context ->
                context.player().filter(player -> player.getHealth() > BuiltinValue.decimal(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Health too low")));
        registry.register(new SimpleCondition("atxcore:health_below", context ->
                context.player().filter(player -> player.getHealth() < BuiltinValue.decimal(context, "amount", 20)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Health too high")));
        registry.register(new SimpleCondition("atxcore:food_above", context ->
                context.player().filter(player -> player.getFoodLevel() > BuiltinValue.integer(context, "amount", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Food too low")));
        registry.register(new SimpleCondition("atxcore:food_below", context ->
                context.player().filter(player -> player.getFoodLevel() < BuiltinValue.integer(context, "amount", 20)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Food too high")));
        registry.register(new SimpleCondition("atxcore:level_at_least", context ->
                context.player().filter(player -> player.getLevel() >= BuiltinValue.integer(context, "level", 0)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Level too low")));
        registry.register(new SimpleCondition("atxcore:level_below", context ->
                context.player().filter(player -> player.getLevel() < BuiltinValue.integer(context, "level", 1)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Level too high")));
        registry.register(new SimpleCondition("atxcore:has_item", context ->
                context.player().filter(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().contains(material, Math.max(1, BuiltinValue.integer(context, "amount", 1)));
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Missing item")));
        registry.register(new SimpleCondition("atxcore:holding_item", context ->
                context.player().filter(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().getItemInMainHand().getType() == material;
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Not holding item")));
        registry.register(new SimpleCondition("atxcore:sneaking", context ->
                context.player().filter(player -> player.isSneaking() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Sneaking state mismatch")));
        registry.register(new SimpleCondition("atxcore:sprinting", context ->
                context.player().filter(player -> player.isSprinting() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Sprinting state mismatch")));
        registry.register(new SimpleCondition("atxcore:flying", context ->
                context.player().filter(player -> player.isFlying() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Flying state mismatch")));
        registry.register(new SimpleCondition("atxcore:op", context ->
                context.sender().filter(sender -> sender.isOp() == BuiltinValue.bool(context, "value", true)).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Operator state mismatch")));
        registry.register(new SimpleCondition("atxcore:has_potion", context ->
                context.player().filter(player -> {
                    PotionEffectType type = PotionEffectType.getByName(BuiltinValue.string(context, "type", "SPEED").toUpperCase(Locale.ROOT));
                    return type != null && player.hasPotionEffect(type);
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Missing potion")));
        registry.register(new SimpleCondition("atxcore:in_region_box", context ->
                context.location().filter(location ->
                        location.getX() >= Math.min(BuiltinValue.decimal(context, "x1", 0), BuiltinValue.decimal(context, "x2", 0))
                                && location.getX() <= Math.max(BuiltinValue.decimal(context, "x1", 0), BuiltinValue.decimal(context, "x2", 0))
                                && location.getY() >= Math.min(BuiltinValue.decimal(context, "y1", 0), BuiltinValue.decimal(context, "y2", 0))
                                && location.getY() <= Math.max(BuiltinValue.decimal(context, "y1", 0), BuiltinValue.decimal(context, "y2", 0))
                                && location.getZ() >= Math.min(BuiltinValue.decimal(context, "z1", 0), BuiltinValue.decimal(context, "z2", 0))
                                && location.getZ() <= Math.max(BuiltinValue.decimal(context, "z1", 0), BuiltinValue.decimal(context, "z2", 0))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Outside region")));
        registry.register(new SimpleCondition("atxcore:data_exists", context ->
                context.data().containsKey(BuiltinValue.string(context, "key", ""))
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Data key missing")));
        registry.register(new SimpleCondition("atxcore:data_equals", context -> {
            String actual = context.stringData(BuiltinValue.string(context, "key", "")).orElse("");
            String expected = BuiltinValue.string(context, "value", "");
            return actual.equalsIgnoreCase(expected) ? ConditionResult.pass() : ConditionResult.fail("Data mismatch");
        }));
        registry.register(new SimpleCondition("atxcore:string_contains", context -> {
            String text = placeholders.apply(BuiltinValue.string(context, "text", ""), context).toLowerCase(Locale.ROOT);
            String needle = placeholders.apply(BuiltinValue.string(context, "contains", ""), context).toLowerCase(Locale.ROOT);
            return text.contains(needle) ? ConditionResult.pass() : ConditionResult.fail("Text missing value");
        }));
        registry.register(new SimpleCondition("atxcore:number_greater", context ->
                BuiltinValue.decimal(context, "left", 0) > BuiltinValue.decimal(context, "right", 0)
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Number not greater")));
        registry.register(new SimpleCondition("atxcore:number_less", context ->
                BuiltinValue.decimal(context, "left", 0) < BuiltinValue.decimal(context, "right", 0)
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Number not less")));
        registry.register(new SimpleCondition("atxcore:biome", context ->
                context.location().filter(location -> location.getBlock().getBiome().name().equalsIgnoreCase(BuiltinValue.string(context, "biome", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong biome")));
        registry.register(new SimpleCondition("atxcore:dimension", context ->
                context.world().filter(world -> world.getEnvironment().name().equalsIgnoreCase(BuiltinValue.string(context, "environment", ""))).isPresent()
                        ? ConditionResult.pass()
                        : ConditionResult.fail("Wrong dimension")));
        registry.register(new SimpleCondition("atxcore:weather", context ->
                context.world().filter(world -> {
                    String weather = BuiltinValue.string(context, "weather", "clear").toLowerCase(Locale.ROOT);
                    return weather.equals("clear") ? !world.hasStorm() : weather.equals("thunder") ? world.isThundering() : world.hasStorm();
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Wrong weather")));
        registry.register(new SimpleCondition("atxcore:time_between", context ->
                context.world().filter(world -> {
                    long time = world.getTime();
                    long min = BuiltinValue.integer(context, "min", 0);
                    long max = BuiltinValue.integer(context, "max", 24000);
                    return time >= min && time <= max;
                }).isPresent() ? ConditionResult.pass() : ConditionResult.fail("Time outside range")));
    }
}
