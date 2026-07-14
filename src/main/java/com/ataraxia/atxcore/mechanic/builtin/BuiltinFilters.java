package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.filter.Filter;
import org.bukkit.Material;

import java.util.Locale;

public final class BuiltinFilters {
    private BuiltinFilters() {
    }

    public static void register(CoreRegistry<Filter<?>> registry) {
        registry.register(new SimpleFilter("atxcore:player_only", context -> context.player().isPresent()));
        registry.register(new SimpleFilter("atxcore:entity_only", context -> context.entity().isPresent()));
        registry.register(new SimpleFilter("atxcore:location_only", context -> context.location().isPresent()));
        registry.register(new SimpleFilter("atxcore:world", context ->
                context.world().map(value -> value.getName().equalsIgnoreCase(BuiltinValue.string(context, "world", ""))).orElse(false)));
        registry.register(new SimpleFilter("atxcore:not_world", context ->
                context.world().map(value -> !value.getName().equalsIgnoreCase(BuiltinValue.string(context, "world", ""))).orElse(true)));
        registry.register(new SimpleFilter("atxcore:permission", context ->
                context.sender().map(sender -> sender.hasPermission(BuiltinValue.string(context, "permission", ""))).orElse(false)));
        registry.register(new SimpleFilter("atxcore:no_permission", context ->
                context.sender().map(sender -> !sender.hasPermission(BuiltinValue.string(context, "permission", ""))).orElse(true)));
        registry.register(new SimpleFilter("atxcore:gamemode", context ->
                context.player().map(player -> player.getGameMode().name().equalsIgnoreCase(BuiltinValue.string(context, "mode", ""))).orElse(false)));
        registry.register(new SimpleFilter("atxcore:survival", context ->
                context.player().map(player -> player.getGameMode().name().equals("SURVIVAL")).orElse(false)));
        registry.register(new SimpleFilter("atxcore:creative", context ->
                context.player().map(player -> player.getGameMode().name().equals("CREATIVE")).orElse(false)));
        registry.register(new SimpleFilter("atxcore:has_item", context ->
                context.player().map(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().contains(material, Math.max(1, BuiltinValue.integer(context, "amount", 1)));
                }).orElse(false)));
        registry.register(new SimpleFilter("atxcore:holding_item", context ->
                context.player().map(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().getItemInMainHand().getType() == material;
                }).orElse(false)));
        registry.register(new SimpleFilter("atxcore:health_range", context ->
                context.player().map(player -> player.getHealth() >= BuiltinValue.decimal(context, "min", 0)
                        && player.getHealth() <= BuiltinValue.decimal(context, "max", 20)).orElse(false)));
        registry.register(new SimpleFilter("atxcore:level_range", context ->
                context.player().map(player -> player.getLevel() >= BuiltinValue.integer(context, "min", 0)
                        && player.getLevel() <= BuiltinValue.integer(context, "max", Integer.MAX_VALUE)).orElse(false)));
        registry.register(new SimpleFilter("atxcore:biome", context ->
                context.location().map(location -> location.getBlock().getBiome().name().equalsIgnoreCase(BuiltinValue.string(context, "biome", ""))).orElse(false)));
        registry.register(new SimpleFilter("atxcore:dimension", context ->
                context.world().map(world -> world.getEnvironment().name().equalsIgnoreCase(BuiltinValue.string(context, "environment", ""))).orElse(false)));
        registry.register(new SimpleFilter("atxcore:data_exists", context ->
                context.data().containsKey(BuiltinValue.string(context, "key", ""))));
        registry.register(new SimpleFilter("atxcore:data_equals", context ->
                context.stringData(BuiltinValue.string(context, "key", "")).map(value -> value.equalsIgnoreCase(BuiltinValue.string(context, "value", ""))).orElse(false)));
        registry.register(new SimpleFilter("atxcore:random_percent", context ->
                Math.random() * 100D <= BuiltinValue.decimal(context, "percent", 100)));
        registry.register(new SimpleFilter("atxcore:name_contains", context ->
                context.player().map(player -> player.getName().toLowerCase(Locale.ROOT).contains(BuiltinValue.string(context, "text", "").toLowerCase(Locale.ROOT))).orElse(false)));
    }
}
