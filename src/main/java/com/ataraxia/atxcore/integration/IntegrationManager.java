package com.ataraxia.atxcore.integration;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.placeholder.PlaceholderService;

import java.util.LinkedHashMap;
import java.util.Map;

public final class IntegrationManager {
    private final ATXCorePlugin plugin;
    private final Map<String, Integration> integrations = new LinkedHashMap<>();

    public IntegrationManager(ATXCorePlugin plugin, PlaceholderService placeholders) {
        this.plugin = plugin;
        register(new PlaceholderApiIntegration(plugin, placeholders));
        register(new PluginPresenceIntegration(plugin, "Vault"));
    }

    public void register(Integration integration) {
        integrations.put(integration.id(), integration);
    }

    public void load() {
        integrations.values().forEach(this::load);
    }

    public void reload() {
        unload();
        load();
    }

    public void unload() {
        integrations.values().forEach(Integration::disable);
    }

    public Map<String, Integration> integrations() {
        return integrations;
    }

    private void load(Integration integration) {
        if (!plugin.getConfig().getBoolean("integrations." + integration.id() + ".enabled", true)) {
            return;
        }
        if (integration.available()) {
            integration.enable();
        }
    }
}
