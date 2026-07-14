package com.ataraxia.atxcore.placeholder;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlaceholderService {
    private static final Pattern BRACED = Pattern.compile("\\{([a-zA-Z0-9_:-]+)(?:\\.([^}]+))?}");
    private final ATXCorePlugin plugin;
    private final Map<String, PlaceholderProvider> providers = new LinkedHashMap<>();

    public PlaceholderService(ATXCorePlugin plugin) {
        this.plugin = plugin;
    }

    public void register(PlaceholderProvider provider) {
        providers.put(provider.id().toLowerCase(Locale.ROOT), provider);
    }

    public String apply(String input, ExecutionContext context) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        Matcher matcher = BRACED.matcher(input);
        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            String id = matcher.group(1).toLowerCase(Locale.ROOT);
            String argument = matcher.group(2) == null ? "" : matcher.group(2);
            PlaceholderProvider provider = providers.get(id);
            String replacement = provider == null
                    ? plugin.getConfig().getString("placeholders.unknown-value", "")
                    : provider.resolve(argument, context);
            matcher.appendReplacement(output, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(output);
        return ChatColor.translateAlternateColorCodes('&', output.toString());
    }

    public String applyExternal(OfflinePlayer player, String input) {
        if (BukkitPlaceholderBridge.available()) {
            return BukkitPlaceholderBridge.setPlaceholders(player, input);
        }
        return input;
    }

    public Map<String, PlaceholderProvider> providers() {
        return providers;
    }
}
