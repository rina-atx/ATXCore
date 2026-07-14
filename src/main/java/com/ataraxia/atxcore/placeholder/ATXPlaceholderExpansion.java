package com.ataraxia.atxcore.placeholder;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ATXPlaceholderExpansion extends PlaceholderExpansion {
    private final ATXCorePlugin plugin;
    private final PlaceholderService placeholders;

    public ATXPlaceholderExpansion(ATXCorePlugin plugin, PlaceholderService placeholders) {
        this.plugin = plugin;
        this.placeholders = placeholders;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getConfig().getString("placeholders.prefix", "atxcore");
    }

    @Override
    public @NotNull String getAuthor() {
        return "Ataraxia";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        Player player = offlinePlayer == null ? null : offlinePlayer.getPlayer();
        ExecutionContext.Builder builder = ExecutionContext.builder();
        if (player != null) {
            builder.player(player);
        }
        String[] split = params.split("_", 2);
        String id = split[0];
        String argument = split.length > 1 ? split[1] : "";
        PlaceholderProvider provider = placeholders.providers().get(id);
        return provider == null ? "" : provider.resolve(argument, builder.build());
    }
}
