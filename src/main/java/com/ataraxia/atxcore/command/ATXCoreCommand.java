package com.ataraxia.atxcore.command;

import com.ataraxia.atxcore.ATXCorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class ATXCoreCommand implements CommandExecutor {
    private final ATXCorePlugin plugin;

    public ATXCoreCommand(ATXCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("status")) {
            sendStatus(sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadCore();
            sender.sendMessage(ChatColor.GREEN + "ATXCore reloaded.");
            return true;
        }
        if (args[0].equalsIgnoreCase("registry")) {
            sender.sendMessage(ChatColor.GOLD + "Effects: " + plugin.registries().effects().size());
            sender.sendMessage(ChatColor.GOLD + "Triggers: " + plugin.registries().triggers().size());
            sender.sendMessage(ChatColor.GOLD + "Mutators: " + plugin.registries().mutators().size());
            sender.sendMessage(ChatColor.GOLD + "Filters: " + plugin.registries().filters().size());
            sender.sendMessage(ChatColor.GOLD + "Conditions: " + plugin.registries().conditions().size());
            sender.sendMessage(ChatColor.GOLD + "Placeholders: " + plugin.placeholders().providers().size());
            return true;
        }
        return false;
    }

    private void sendStatus(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "ATXCore " + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.GRAY + "Mechanics registered: " + plugin.registries().totalSize());
        plugin.integrations().integrations().values().forEach(integration ->
                sender.sendMessage(ChatColor.GRAY + integration.id() + ": " + (integration.available() ? "available" : "missing")));
    }
}
