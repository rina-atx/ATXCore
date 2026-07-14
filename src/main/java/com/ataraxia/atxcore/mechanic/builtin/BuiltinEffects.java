package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.effect.Effect;
import com.ataraxia.atxcore.placeholder.PlaceholderService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Locale;

public final class BuiltinEffects {
    private BuiltinEffects() {
    }

    public static void register(ATXCorePlugin plugin, CoreRegistry<Effect<?>> registry, PlaceholderService placeholders) {
        registry.register(new SimpleEffect("atxcore:message", context ->
                context.sender().ifPresent(sender -> sender.sendMessage(placeholders.apply(BuiltinValue.string(context, "message", ""), context)))));
        registry.register(new SimpleEffect("atxcore:console_command", context -> {
            String command = placeholders.apply(BuiltinValue.string(context, "command", ""), context);
            if (!command.isBlank()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }));
        registry.register(new SimpleEffect("atxcore:sound", context -> playSound(plugin, context)));

        registry.register(new SimpleEffect("atxcore:broadcast", context ->
                Bukkit.broadcastMessage(placeholders.apply(BuiltinValue.string(context, "message", ""), context))));
        registry.register(new SimpleEffect("atxcore:action_bar", context ->
                context.player().ifPresent(player -> player.sendActionBar(Component.text(placeholders.apply(BuiltinValue.string(context, "message", ""), context))))));
        registry.register(new SimpleEffect("atxcore:title", context ->
                context.player().ifPresent(player -> player.showTitle(Title.title(
                        Component.text(placeholders.apply(BuiltinValue.string(context, "title", ""), context)),
                        Component.text(placeholders.apply(BuiltinValue.string(context, "subtitle", ""), context)))))));
        registry.register(new SimpleEffect("atxcore:clear_title", context ->
                context.player().ifPresent(player -> {
                    player.clearTitle();
                    player.resetTitle();
                })));
        registry.register(new SimpleEffect("atxcore:player_command", context ->
                context.player().ifPresent(player -> player.performCommand(placeholders.apply(BuiltinValue.string(context, "command", ""), context)))));
        registry.register(new SimpleEffect("atxcore:op_command", context -> context.player().ifPresent(player -> {
            boolean wasOp = player.isOp();
            try {
                player.setOp(true);
                player.performCommand(placeholders.apply(BuiltinValue.string(context, "command", ""), context));
            } finally {
                player.setOp(wasOp);
            }
        })));
        registry.register(new SimpleEffect("atxcore:teleport", context -> context.player().ifPresent(player -> {
            Location location = BuiltinValue.location(context);
            if (location != null) {
                player.teleport(location);
            }
        })));
        registry.register(new SimpleEffect("atxcore:teleport_world_spawn", context -> context.player().ifPresent(player ->
                context.world().ifPresent(world -> player.teleport(world.getSpawnLocation())))));
        registry.register(new SimpleEffect("atxcore:heal", context ->
                context.player().ifPresent(player -> player.setHealth(Math.min(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue(), BuiltinValue.decimal(context, "amount", player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue()))))));
        registry.register(new SimpleEffect("atxcore:add_health", context ->
                context.player().ifPresent(player -> player.setHealth(Math.min(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth() + BuiltinValue.decimal(context, "amount", 1))))));
        registry.register(new SimpleEffect("atxcore:damage", context ->
                context.player().ifPresent(player -> player.damage(BuiltinValue.decimal(context, "amount", 1)))));
        registry.register(new SimpleEffect("atxcore:kill", context ->
                context.player().ifPresent(player -> player.setHealth(0))));
        registry.register(new SimpleEffect("atxcore:feed", context -> context.player().ifPresent(player -> {
            player.setFoodLevel(Math.min(20, BuiltinValue.integer(context, "amount", 20)));
            player.setSaturation((float) Math.min(20, BuiltinValue.decimal(context, "saturation", 20)));
        })));
        registry.register(new SimpleEffect("atxcore:add_food", context ->
                context.player().ifPresent(player -> player.setFoodLevel(Math.min(20, player.getFoodLevel() + BuiltinValue.integer(context, "amount", 1))))));
        registry.register(new SimpleEffect("atxcore:set_saturation", context ->
                context.player().ifPresent(player -> player.setSaturation(BuiltinValue.floating(context, "amount", 5)))));
        registry.register(new SimpleEffect("atxcore:extinguish", context ->
                context.entity().ifPresent(entity -> entity.setFireTicks(0))));
        registry.register(new SimpleEffect("atxcore:set_fire_ticks", context ->
                context.entity().ifPresent(entity -> entity.setFireTicks(BuiltinValue.integer(context, "ticks", 100)))));
        registry.register(new SimpleEffect("atxcore:add_potion", context -> context.player().ifPresent(player -> {
            PotionEffectType type = PotionEffectType.getByName(BuiltinValue.string(context, "type", "SPEED").toUpperCase(Locale.ROOT));
            if (type != null) {
                player.addPotionEffect(new PotionEffect(type, BuiltinValue.integer(context, "duration", 200), BuiltinValue.integer(context, "amplifier", 0)));
            }
        })));
        registry.register(new SimpleEffect("atxcore:remove_potion", context -> context.player().ifPresent(player -> {
            PotionEffectType type = PotionEffectType.getByName(BuiltinValue.string(context, "type", "SPEED").toUpperCase(Locale.ROOT));
            if (type != null) {
                player.removePotionEffect(type);
            }
        })));
        registry.register(new SimpleEffect("atxcore:clear_potions", context ->
                context.player().ifPresent(player -> player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType())))));
        registry.register(new SimpleEffect("atxcore:give_item", context -> context.player().ifPresent(player -> {
            Material material = Material.matchMaterial(BuiltinValue.string(context, "material", "STONE"));
            if (material != null) {
                player.getInventory().addItem(new ItemStack(material, Math.max(1, BuiltinValue.integer(context, "amount", 1))));
            }
        })));
        registry.register(new SimpleEffect("atxcore:take_item", context -> context.player().ifPresent(player -> {
            Material material = Material.matchMaterial(BuiltinValue.string(context, "material", "STONE"));
            if (material != null) {
                player.getInventory().removeItem(new ItemStack(material, Math.max(1, BuiltinValue.integer(context, "amount", 1))));
            }
        })));
        registry.register(new SimpleEffect("atxcore:clear_inventory", context ->
                context.player().ifPresent(player -> player.getInventory().clear())));
        registry.register(new SimpleEffect("atxcore:set_gamemode", context -> context.player().ifPresent(player -> {
            try {
                player.setGameMode(GameMode.valueOf(BuiltinValue.string(context, "mode", "SURVIVAL").toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Unknown gamemode: " + BuiltinValue.string(context, "mode", ""));
            }
        })));
        registry.register(new SimpleEffect("atxcore:set_flying", context ->
                context.player().ifPresent(player -> player.setFlying(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("atxcore:set_allow_flight", context ->
                context.player().ifPresent(player -> player.setAllowFlight(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("atxcore:set_exp", context ->
                context.player().ifPresent(player -> player.setExp(Math.max(0, Math.min(1, BuiltinValue.floating(context, "amount", 0)))))));
        registry.register(new SimpleEffect("atxcore:give_exp", context ->
                context.player().ifPresent(player -> player.giveExp(BuiltinValue.integer(context, "amount", 1)))));
        registry.register(new SimpleEffect("atxcore:set_level", context ->
                context.player().ifPresent(player -> player.setLevel(Math.max(0, BuiltinValue.integer(context, "level", 0))))));
        registry.register(new SimpleEffect("atxcore:add_level", context ->
                context.player().ifPresent(player -> player.setLevel(Math.max(0, player.getLevel() + BuiltinValue.integer(context, "amount", 1))))));
        registry.register(new SimpleEffect("atxcore:spawn_particle", context -> {
            Location location = context.location().orElse(null);
            if (location != null && location.getWorld() != null) {
                try {
                    Particle particle = Particle.valueOf(BuiltinValue.string(context, "particle", "HAPPY_VILLAGER").toUpperCase(Locale.ROOT));
                    location.getWorld().spawnParticle(particle, location, BuiltinValue.integer(context, "count", 10));
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().warning("Unknown particle: " + BuiltinValue.string(context, "particle", ""));
                }
            }
        }));
        registry.register(new SimpleEffect("atxcore:strike_lightning_effect", context ->
                context.location().ifPresent(location -> location.getWorld().strikeLightningEffect(location))));
        registry.register(new SimpleEffect("atxcore:strike_lightning", context ->
                context.location().ifPresent(location -> location.getWorld().strikeLightning(location))));
        registry.register(new SimpleEffect("atxcore:explosion", context ->
                context.location().ifPresent(location -> location.getWorld().createExplosion(location, BuiltinValue.floating(context, "power", 0), BuiltinValue.bool(context, "fire", false), BuiltinValue.bool(context, "break_blocks", false)))));
        registry.register(new SimpleEffect("atxcore:set_time", context ->
                context.world().ifPresent(world -> world.setTime(BuiltinValue.integer(context, "time", 0)))));
        registry.register(new SimpleEffect("atxcore:set_weather_clear", context -> context.world().ifPresent(world -> {
            world.setStorm(false);
            world.setThundering(false);
        })));
        registry.register(new SimpleEffect("atxcore:set_weather_rain", context -> context.world().ifPresent(world -> {
            world.setStorm(true);
            world.setThundering(false);
        })));
        registry.register(new SimpleEffect("atxcore:set_weather_thunder", context -> context.world().ifPresent(world -> {
            world.setStorm(true);
            world.setThundering(true);
        })));
        registry.register(new SimpleEffect("atxcore:play_world_sound", context -> {
            Location location = context.location().orElse(null);
            if (location != null && location.getWorld() != null) {
                try {
                    location.getWorld().playSound(location, Sound.valueOf(BuiltinValue.string(context, "sound", "ENTITY_PLAYER_LEVELUP")), BuiltinValue.floating(context, "volume", 1), BuiltinValue.floating(context, "pitch", 1));
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().warning("Unknown sound: " + BuiltinValue.string(context, "sound", ""));
                }
            }
        }));
        registry.register(new SimpleEffect("atxcore:stop_sound", context -> context.player().ifPresent(player -> {
            String sound = BuiltinValue.string(context, "sound", "");
            if (!sound.isBlank()) {
                player.stopSound(sound);
            }
        })));
        registry.register(new SimpleEffect("atxcore:set_velocity", context ->
                context.entity().ifPresent(entity -> entity.setVelocity(new Vector(BuiltinValue.decimal(context, "x", 0), BuiltinValue.decimal(context, "y", 0), BuiltinValue.decimal(context, "z", 0))))));
        registry.register(new SimpleEffect("atxcore:launch_up", context ->
                context.entity().ifPresent(entity -> entity.setVelocity(entity.getVelocity().setY(BuiltinValue.decimal(context, "power", 1))))));
        registry.register(new SimpleEffect("atxcore:close_inventory", context ->
                context.player().ifPresent(player -> player.closeInventory())));
        registry.register(new SimpleEffect("atxcore:open_workbench", context ->
                context.player().ifPresent(player -> player.openWorkbench(player.getLocation(), true))));
        registry.register(new SimpleEffect("atxcore:kick", context ->
                context.player().ifPresent(player -> player.kick(Component.text(placeholders.apply(BuiltinValue.string(context, "reason", "Kicked"), context))))));
        registry.register(new SimpleEffect("atxcore:set_display_name", context ->
                context.player().ifPresent(player -> player.setDisplayName(placeholders.apply(BuiltinValue.string(context, "name", player.getName()), context)))));
        registry.register(new SimpleEffect("atxcore:reset_display_name", context ->
                context.player().ifPresent(player -> player.setDisplayName(player.getName()))));
        registry.register(new SimpleEffect("atxcore:set_player_list_name", context ->
                context.player().ifPresent(player -> player.setPlayerListName(placeholders.apply(BuiltinValue.string(context, "name", player.getName()), context)))));
        registry.register(new SimpleEffect("atxcore:set_glowing", context ->
                context.entity().ifPresent(entity -> entity.setGlowing(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("atxcore:set_invulnerable", context ->
                context.entity().ifPresent(entity -> entity.setInvulnerable(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("atxcore:spawn_entity", context -> {
            Location location = context.location().orElse(null);
            if (location != null && location.getWorld() != null) {
                try {
                    EntityType type = EntityType.valueOf(BuiltinValue.string(context, "type", "ZOMBIE").toUpperCase(Locale.ROOT));
                    location.getWorld().spawnEntity(location, type);
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().warning("Unknown entity type: " + BuiltinValue.string(context, "type", ""));
                }
            }
        }));
        registry.register(new SimpleEffect("atxcore:delay_console_command", context -> {
            int delay = Math.max(1, BuiltinValue.integer(context, "delay", 20));
            String command = placeholders.apply(BuiltinValue.string(context, "command", ""), context);
            if (!command.isBlank()) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), delay);
            }
        }));
        registry.register(new SimpleEffect("atxcore:delayed_message", context -> {
            int delay = Math.max(1, BuiltinValue.integer(context, "delay", 20));
            String message = placeholders.apply(BuiltinValue.string(context, "message", ""), context);
            context.sender().ifPresent(sender -> Bukkit.getScheduler().runTaskLater(plugin, () -> sender.sendMessage(message), delay));
        }));
        registry.register(new SimpleEffect("atxcore:repeat_console_command", context -> {
            int times = Math.max(1, BuiltinValue.integer(context, "times", 1));
            String command = placeholders.apply(BuiltinValue.string(context, "command", ""), context);
            for (int i = 0; i < times && !command.isBlank(); i++) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), (long) i * Math.max(1, BuiltinValue.integer(context, "interval", 20)));
            }
        }));
    }

    private static void playSound(ATXCorePlugin plugin, ExecutionContext context) {
        String soundName = BuiltinValue.string(context, "sound", "ENTITY_PLAYER_LEVELUP");
        context.player().ifPresent(player -> {
            try {
                player.playSound(player.getLocation(), Sound.valueOf(soundName), BuiltinValue.floating(context, "volume", 1), BuiltinValue.floating(context, "pitch", 1));
            } catch (IllegalArgumentException exception) {
                plugin.getLogger().warning("Unknown sound: " + soundName);
            }
        });
    }
}
