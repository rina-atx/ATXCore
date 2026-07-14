package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.placeholder.PlaceholderProvider;
import com.ataraxia.atxcore.placeholder.PlaceholderService;
import org.bukkit.Bukkit;

public final class BuiltinPlaceholders {
    private BuiltinPlaceholders() {
    }

    public static void register(PlaceholderService placeholders) {
        placeholders.register(new SimplePlaceholder("player", context -> context.player().map(player -> player.getName()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_uuid", context -> context.player().map(player -> player.getUniqueId().toString()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_display", context -> context.player().map(player -> player.getDisplayName()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_list_name", context -> context.player().map(player -> player.getPlayerListName()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_health", context -> context.player().map(player -> String.valueOf(player.getHealth())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_max_health", context -> context.player().map(player -> String.valueOf(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_food", context -> context.player().map(player -> String.valueOf(player.getFoodLevel())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_saturation", context -> context.player().map(player -> String.valueOf(player.getSaturation())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_level", context -> context.player().map(player -> String.valueOf(player.getLevel())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_exp", context -> context.player().map(player -> String.valueOf(player.getExp())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_gamemode", context -> context.player().map(player -> player.getGameMode().name()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_ping", context -> context.player().map(player -> String.valueOf(player.getPing())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_locale", context -> context.player().map(player -> player.locale().toLanguageTag()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_world", context -> context.player().map(player -> player.getWorld().getName()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_x", context -> context.player().map(player -> String.valueOf(player.getLocation().getBlockX())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_y", context -> context.player().map(player -> String.valueOf(player.getLocation().getBlockY())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_z", context -> context.player().map(player -> String.valueOf(player.getLocation().getBlockZ())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_biome", context -> context.player().map(player -> player.getLocation().getBlock().getBiome().name()).orElse("")));
        placeholders.register(new SimplePlaceholder("player_flying", context -> context.player().map(player -> String.valueOf(player.isFlying())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_sneaking", context -> context.player().map(player -> String.valueOf(player.isSneaking())).orElse("")));
        placeholders.register(new SimplePlaceholder("player_sprinting", context -> context.player().map(player -> String.valueOf(player.isSprinting())).orElse("")));

        placeholders.register(new SimplePlaceholder("entity_type", context -> context.entity().map(entity -> entity.getType().name()).orElse("")));
        placeholders.register(new SimplePlaceholder("entity_uuid", context -> context.entity().map(entity -> entity.getUniqueId().toString()).orElse("")));
        placeholders.register(new SimplePlaceholder("entity_world", context -> context.entity().map(entity -> entity.getWorld().getName()).orElse("")));

        placeholders.register(new SimplePlaceholder("world", context -> context.world().map(world -> world.getName()).orElse("")));
        placeholders.register(new SimplePlaceholder("world_time", context -> context.world().map(world -> String.valueOf(world.getTime())).orElse("")));
        placeholders.register(new SimplePlaceholder("world_full_time", context -> context.world().map(world -> String.valueOf(world.getFullTime())).orElse("")));
        placeholders.register(new SimplePlaceholder("world_environment", context -> context.world().map(world -> world.getEnvironment().name()).orElse("")));
        placeholders.register(new SimplePlaceholder("world_weather", context -> context.world().map(world -> world.isThundering() ? "thunder" : world.hasStorm() ? "rain" : "clear").orElse("")));
        placeholders.register(new SimplePlaceholder("world_players", context -> context.world().map(world -> String.valueOf(world.getPlayers().size())).orElse("")));

        placeholders.register(new SimplePlaceholder("x", context -> context.location().map(location -> String.valueOf(location.getBlockX())).orElse("")));
        placeholders.register(new SimplePlaceholder("y", context -> context.location().map(location -> String.valueOf(location.getBlockY())).orElse("")));
        placeholders.register(new SimplePlaceholder("z", context -> context.location().map(location -> String.valueOf(location.getBlockZ())).orElse("")));
        placeholders.register(new SimplePlaceholder("yaw", context -> context.location().map(location -> String.valueOf(location.getYaw())).orElse("")));
        placeholders.register(new SimplePlaceholder("pitch", context -> context.location().map(location -> String.valueOf(location.getPitch())).orElse("")));
        placeholders.register(new SimplePlaceholder("block_type", context -> context.location().map(location -> location.getBlock().getType().name()).orElse("")));
        placeholders.register(new SimplePlaceholder("biome", context -> context.location().map(location -> location.getBlock().getBiome().name()).orElse("")));

        placeholders.register(new SimplePlaceholder("server_online", context -> String.valueOf(Bukkit.getOnlinePlayers().size())));
        placeholders.register(new SimplePlaceholder("server_max", context -> String.valueOf(Bukkit.getMaxPlayers())));
        placeholders.register(new SimplePlaceholder("server_name", context -> Bukkit.getName()));
        placeholders.register(new SimplePlaceholder("server_version", context -> Bukkit.getVersion()));
        placeholders.register(new SimplePlaceholder("server_motd", context -> Bukkit.getMotd()));
        placeholders.register(new SimplePlaceholder("tps_1m", context -> String.format("%.2f", Bukkit.getTPS()[0])));
        placeholders.register(new SimplePlaceholder("tps_5m", context -> String.format("%.2f", Bukkit.getTPS()[1])));
        placeholders.register(new SimplePlaceholder("tps_15m", context -> String.format("%.2f", Bukkit.getTPS()[2])));

        placeholders.register(new PlaceholderProvider() {
            @Override
            public String id() {
                return "data";
            }

            @Override
            public String resolve(String argument, com.ataraxia.atxcore.mechanic.ExecutionContext context) {
                return context.stringData(argument).orElse("");
            }
        });
    }
}
