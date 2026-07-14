package com.ataraxia.atxcore.integration;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.placeholder.ATXPlaceholderExpansion;
import com.ataraxia.atxcore.placeholder.PlaceholderService;
import org.bukkit.Bukkit;

public final class PlaceholderApiIntegration implements Integration {
    private final ATXCorePlugin plugin;
    private final PlaceholderService placeholders;
    private ATXPlaceholderExpansion expansion;

    public PlaceholderApiIntegration(ATXCorePlugin plugin, PlaceholderService placeholders) {
        this.plugin = plugin;
        this.placeholders = placeholders;
    }

    @Override
    public String id() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void enable() {
        expansion = new ATXPlaceholderExpansion(plugin, placeholders);
        expansion.register();
        plugin.getLogger().info("Hooked PlaceholderAPI.");
    }

    @Override
    public void disable() {
        if (expansion != null) {
            expansion.unregister();
            expansion = null;
        }
    }
}
