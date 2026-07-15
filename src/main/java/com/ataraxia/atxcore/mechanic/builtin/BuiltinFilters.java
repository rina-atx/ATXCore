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
        registry.register(new SimpleFilter("player_only", context -> context.player().isPresent()));
        registry.register(new SimpleFilter("entity_only", context -> context.entity().isPresent()));
        registry.register(new SimpleFilter("location_only", context -> context.location().isPresent()));
        registry.register(new SimpleFilter("target_player_only", context -> context.targetPlayer().isPresent()));
        registry.register(new SimpleFilter("actor_player_only", context -> context.actorPlayer().isPresent()));
        registry.register(new SimpleFilter("target_entity_type", context ->
                context.target().map(entity -> entity.getType().name().equals(BuiltinValue.minecraftKey(BuiltinValue.string(context, "type", "")))).orElse(false)));
        registry.register(new SimpleFilter("not_target_entity_type", context ->
                context.target().map(entity -> !entity.getType().name().equals(BuiltinValue.minecraftKey(BuiltinValue.string(context, "type", "")))).orElse(true)));
        registry.register(new SimpleFilter("actor_entity_type", context ->
                context.actor().map(entity -> entity.getType().name().equals(BuiltinValue.minecraftKey(BuiltinValue.string(context, "type", "")))).orElse(false)));
        registry.register(new SimpleFilter("not_actor_entity_type", context ->
                context.actor().map(entity -> !entity.getType().name().equals(BuiltinValue.minecraftKey(BuiltinValue.string(context, "type", "")))).orElse(true)));
        registry.register(new SimpleFilter("target_not_creeper", context ->
                context.target().map(entity -> !entity.getType().name().equals("CREEPER")).orElse(true)));
        registry.register(new SimpleFilter("world", context ->
                context.world().map(value -> value.getName().equalsIgnoreCase(BuiltinValue.string(context, "world", ""))).orElse(false)));
        registry.register(new SimpleFilter("not_world", context ->
                context.world().map(value -> !value.getName().equalsIgnoreCase(BuiltinValue.string(context, "world", ""))).orElse(true)));
        registry.register(new SimpleFilter("permission", context ->
                context.sender().map(sender -> sender.hasPermission(BuiltinValue.string(context, "permission", ""))).orElse(false)));
        registry.register(new SimpleFilter("no_permission", context ->
                context.sender().map(sender -> !sender.hasPermission(BuiltinValue.string(context, "permission", ""))).orElse(true)));
        registry.register(new SimpleFilter("gamemode", context ->
                context.player().map(player -> player.getGameMode().name().equalsIgnoreCase(BuiltinValue.string(context, "mode", ""))).orElse(false)));
        registry.register(new SimpleFilter("survival", context ->
                context.player().map(player -> player.getGameMode().name().equals("SURVIVAL")).orElse(false)));
        registry.register(new SimpleFilter("creative", context ->
                context.player().map(player -> player.getGameMode().name().equals("CREATIVE")).orElse(false)));
        registry.register(new SimpleFilter("has_item", context ->
                context.player().map(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().contains(material, Math.max(1, BuiltinValue.integer(context, "amount", 1)));
                }).orElse(false)));
        registry.register(new SimpleFilter("holding_item", context ->
                context.player().map(player -> {
                    Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                    return material != null && player.getInventory().getItemInMainHand().getType() == material;
                }).orElse(false)));
        registry.register(new SimpleFilter("health_range", context ->
                context.player().map(player -> player.getHealth() >= BuiltinValue.decimal(context, "min", 0)
                        && player.getHealth() <= BuiltinValue.decimal(context, "max", 20)).orElse(false)));
        registry.register(new SimpleFilter("level_range", context ->
                context.player().map(player -> player.getLevel() >= BuiltinValue.integer(context, "min", 0)
                        && player.getLevel() <= BuiltinValue.integer(context, "max", Integer.MAX_VALUE)).orElse(false)));
        registry.register(new SimpleFilter("biome", context ->
                context.location().map(location -> location.getBlock().getBiome().name().equalsIgnoreCase(BuiltinValue.string(context, "biome", ""))).orElse(false)));
        registry.register(new SimpleFilter("dimension", context ->
                context.world().map(world -> world.getEnvironment().name().equalsIgnoreCase(BuiltinValue.string(context, "environment", ""))).orElse(false)));
        registry.register(new SimpleFilter("data_exists", context ->
                context.data().containsKey(BuiltinValue.string(context, "key", ""))));
        registry.register(new SimpleFilter("data_equals", context ->
                context.stringData(BuiltinValue.string(context, "key", "")).map(value -> value.equalsIgnoreCase(BuiltinValue.string(context, "value", ""))).orElse(false)));
        registry.register(new SimpleFilter("random_percent", context ->
                Math.random() * 100D <= BuiltinValue.decimal(context, "percent", 100)));
        registry.register(new SimpleFilter("name_contains", context ->
                context.player().map(player -> player.getName().toLowerCase(Locale.ROOT).contains(BuiltinValue.string(context, "text", "").toLowerCase(Locale.ROOT))).orElse(false)));
        registry.register(new SimpleFilter("attribute_range", context ->
                context.entity().flatMap(entity -> BuiltinAttribute.value(entity, BuiltinValue.string(context, "attribute", "")))
                        .map(value -> value >= BuiltinValue.decimal(context, "min", Double.NEGATIVE_INFINITY)
                        && value <= BuiltinValue.decimal(context, "max", Double.POSITIVE_INFINITY)).orElse(false)));
        registry.register(new SimpleFilter("vault_has_money", context -> context.player()
                .flatMap(player -> com.ataraxia.atxcore.ATXCorePlugin.getPlugin(com.ataraxia.atxcore.ATXCorePlugin.class).integrations().vault()
                        .flatMap(vault -> vault.economy().map(economy -> economy.has(player, BuiltinValue.decimal(context, "amount", 0)))))
                .orElse(false)));
    }
}
