package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.mechanic.ExecutionContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Locale;

final class BuiltinValue {
    private BuiltinValue() {
    }

    static String string(ExecutionContext context, String key, String fallback) {
        return context.value(key).orElse(fallback);
    }

    static int integer(ExecutionContext context, String key, int fallback) {
        try {
            return Integer.parseInt(string(context, key, String.valueOf(fallback)));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    static double decimal(ExecutionContext context, String key, double fallback) {
        try {
            return Double.parseDouble(string(context, key, String.valueOf(fallback)));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    static float floating(ExecutionContext context, String key, float fallback) {
        try {
            return Float.parseFloat(string(context, key, String.valueOf(fallback)));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    static boolean bool(ExecutionContext context, String key, boolean fallback) {
        String value = string(context, key, String.valueOf(fallback)).toLowerCase(Locale.ROOT);
        if (value.equals("true") || value.equals("yes") || value.equals("1")) {
            return true;
        }
        if (value.equals("false") || value.equals("no") || value.equals("0")) {
            return false;
        }
        return fallback;
    }

    static String minecraftKey(String value) {
        if (value == null) {
            return "";
        }
        return value.trim()
                .toUpperCase(Locale.ROOT)
                .replace("ENTITY_MINECRAFT:", "")
                .replace("MINECRAFT:", "")
                .replace('-', '_')
                .replace(' ', '_');
    }

    static Location location(ExecutionContext context) {
        Location base = context.location().orElse(null);
        String worldName = string(context, "world", base == null || base.getWorld() == null ? "" : base.getWorld().getName());
        if (worldName.isBlank() || Bukkit.getWorld(worldName) == null) {
            return base;
        }
        double x = decimal(context, "x", base == null ? 0 : base.getX());
        double y = decimal(context, "y", base == null ? 0 : base.getY());
        double z = decimal(context, "z", base == null ? 0 : base.getZ());
        float yaw = floating(context, "yaw", base == null ? 0 : base.getYaw());
        float pitch = floating(context, "pitch", base == null ? 0 : base.getPitch());
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
