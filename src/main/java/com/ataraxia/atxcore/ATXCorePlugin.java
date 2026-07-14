package com.ataraxia.atxcore;

import com.ataraxia.atxcore.api.ATXCoreApi;
import com.ataraxia.atxcore.api.registry.CoreRegistries;
import com.ataraxia.atxcore.command.ATXCoreCommand;
import com.ataraxia.atxcore.integration.IntegrationManager;
import com.ataraxia.atxcore.mechanic.builtin.BuiltinMechanics;
import com.ataraxia.atxcore.placeholder.PlaceholderService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ATXCorePlugin extends JavaPlugin implements ATXCoreApi {
    private CoreRegistries registries;
    private PlaceholderService placeholders;
    private IntegrationManager integrations;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("guide.yml", false);

        this.registries = new CoreRegistries();
        this.placeholders = new PlaceholderService(this);
        this.integrations = new IntegrationManager(this, placeholders);

        BuiltinMechanics.registerAll(this, registries, placeholders);
        integrations.load();

        Bukkit.getServicesManager().register(ATXCoreApi.class, this, this, ServicePriority.Normal);
        Objects.requireNonNull(getCommand("atxcore")).setExecutor(new ATXCoreCommand(this));

        getLogger().info("ATXCore enabled with " + registries.totalSize() + " registered mechanics.");
    }

    @Override
    public void onDisable() {
        if (integrations != null) {
            integrations.unload();
        }
        Bukkit.getServicesManager().unregister(ATXCoreApi.class, this);
    }

    public void reloadCore() {
        reloadConfig();
        integrations.reload();
    }

    @Override
    public CoreRegistries registries() {
        return registries;
    }

    @Override
    public PlaceholderService placeholders() {
        return placeholders;
    }

    @Override
    public IntegrationManager integrations() {
        return integrations;
    }

    public boolean debug() {
        return getConfig().getBoolean("settings.debug", false);
    }
}
