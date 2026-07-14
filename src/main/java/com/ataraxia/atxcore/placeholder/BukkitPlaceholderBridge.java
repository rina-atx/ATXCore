package com.ataraxia.atxcore.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

final class BukkitPlaceholderBridge {
    private BukkitPlaceholderBridge() {
    }

    static boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    static String setPlaceholders(OfflinePlayer player, String input) {
        return PlaceholderAPI.setPlaceholders(player, input);
    }
}
