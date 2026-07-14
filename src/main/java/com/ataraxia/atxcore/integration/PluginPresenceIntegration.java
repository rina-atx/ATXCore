package com.ataraxia.atxcore.integration;

import com.ataraxia.atxcore.ATXCorePlugin;
import org.bukkit.Bukkit;

public final class PluginPresenceIntegration implements Integration {
    private final ATXCorePlugin plugin;
    private final String pluginName;
    private boolean enabled;

    public PluginPresenceIntegration(ATXCorePlugin plugin, String pluginName) {
        this.plugin = plugin;
        this.pluginName = pluginName;
    }

    @Override
    public String id() {
        return pluginName;
    }

    @Override
    public boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

    @Override
    public void enable() {
        enabled = true;
        plugin.getLogger().info("Detected " + pluginName + ".");
    }

    @Override
    public void disable() {
        enabled = false;
    }

    public boolean enabled() {
        return enabled;
    }
}
