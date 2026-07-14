package com.ataraxia.atxcore.integration;

import com.ataraxia.atxcore.ATXCorePlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;

public final class VaultIntegration implements Integration {
    private final ATXCorePlugin plugin;
    private Economy economy;

    public VaultIntegration(ATXCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String id() {
        return "Vault";
    }

    @Override
    public boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    @Override
    public void enable() {
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            plugin.getLogger().warning("Vault found, but no economy provider is registered.");
            return;
        }
        economy = provider.getProvider();
        plugin.getLogger().info("Hooked Vault economy: " + economy.getName());
    }

    @Override
    public void disable() {
        economy = null;
    }

    public Optional<Economy> economy() {
        return Optional.ofNullable(economy);
    }
}
