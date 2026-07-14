package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.effect.Effect;
import com.ataraxia.atxcore.placeholder.PlaceholderService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Locale;

public final class BuiltinEffects {
    private BuiltinEffects() {
    }

    public static void register(ATXCorePlugin plugin, CoreRegistry<Effect<?>> registry, PlaceholderService placeholders) {
        registry.register(new SimpleEffect("message", context ->
                context.sender().ifPresent(sender -> sender.sendMessage(placeholders.apply(BuiltinValue.string(context, "message", ""), context)))));
        registry.register(new SimpleEffect("console_command", context -> {
            String command = placeholders.apply(BuiltinValue.string(context, "command", ""), context);
            if (!command.isBlank()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }));
        registry.register(new SimpleEffect("sound", context -> playSound(plugin, context)));

        registry.register(new SimpleEffect("broadcast", context ->
                Bukkit.broadcastMessage(placeholders.apply(BuiltinValue.string(context, "message", ""), context))));
        registry.register(new SimpleEffect("action_bar", context ->
                context.player().ifPresent(player -> player.sendActionBar(Component.text(placeholders.apply(BuiltinValue.string(context, "message", ""), context))))));
        registry.register(new SimpleEffect("title", context ->
                context.player().ifPresent(player -> player.showTitle(Title.title(
                        Component.text(placeholders.apply(BuiltinValue.string(context, "title", ""), context)),
                        Component.text(placeholders.apply(BuiltinValue.string(context, "subtitle", ""), context)))))));
        registry.register(new SimpleEffect("clear_title", context ->
                context.player().ifPresent(player -> {
                    player.clearTitle();
                    player.resetTitle();
                })));
        registry.register(new SimpleEffect("player_command", context ->
                context.player().ifPresent(player -> player.performCommand(placeholders.apply(BuiltinValue.string(context, "command", ""), context)))));
        registry.register(new SimpleEffect("op_command", context -> context.player().ifPresent(player -> {
            boolean wasOp = player.isOp();
            try {
                player.setOp(true);
                player.performCommand(placeholders.apply(BuiltinValue.string(context, "command", ""), context));
            } finally {
                player.setOp(wasOp);
            }
        })));
        registry.register(new SimpleEffect("teleport", context -> context.player().ifPresent(player -> {
            Location location = BuiltinValue.location(context);
            if (location != null) {
                player.teleport(location);
            }
        })));
        registry.register(new SimpleEffect("teleport_world_spawn", context -> context.player().ifPresent(player ->
                context.world().ifPresent(world -> player.teleport(world.getSpawnLocation())))));
        registry.register(new SimpleEffect("heal", context ->
                context.player().ifPresent(player -> player.setHealth(Math.min(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue(), BuiltinValue.decimal(context, "amount", player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue()))))));
        registry.register(new SimpleEffect("add_health", context ->
                context.player().ifPresent(player -> player.setHealth(Math.min(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth() + BuiltinValue.decimal(context, "amount", 1))))));
        registry.register(new SimpleEffect("damage", context ->
                context.player().ifPresent(player -> player.damage(BuiltinValue.decimal(context, "amount", 1)))));
        registry.register(new SimpleEffect("kill", context ->
                context.player().ifPresent(player -> player.setHealth(0))));
        registry.register(new SimpleEffect("feed", context -> context.player().ifPresent(player -> {
            player.setFoodLevel(Math.min(20, BuiltinValue.integer(context, "amount", 20)));
            player.setSaturation((float) Math.min(20, BuiltinValue.decimal(context, "saturation", 20)));
        })));
        registry.register(new SimpleEffect("add_food", context ->
                context.player().ifPresent(player -> player.setFoodLevel(Math.min(20, player.getFoodLevel() + BuiltinValue.integer(context, "amount", 1))))));
        registry.register(new SimpleEffect("set_saturation", context ->
                context.player().ifPresent(player -> player.setSaturation(BuiltinValue.floating(context, "amount", 5)))));
        registry.register(new SimpleEffect("extinguish", context ->
                context.entity().ifPresent(entity -> entity.setFireTicks(0))));
        registry.register(new SimpleEffect("set_fire_ticks", context ->
                context.entity().ifPresent(entity -> entity.setFireTicks(BuiltinValue.integer(context, "ticks", 100)))));
        registry.register(new SimpleEffect("add_potion", context -> context.player().ifPresent(player -> {
            PotionEffectType type = PotionEffectType.getByName(BuiltinValue.string(context, "type", "SPEED").toUpperCase(Locale.ROOT));
            if (type != null) {
                player.addPotionEffect(new PotionEffect(type, BuiltinValue.integer(context, "duration", 200), BuiltinValue.integer(context, "amplifier", 0)));
            }
        })));
        registry.register(new SimpleEffect("remove_potion", context -> context.player().ifPresent(player -> {
            PotionEffectType type = PotionEffectType.getByName(BuiltinValue.string(context, "type", "SPEED").toUpperCase(Locale.ROOT));
            if (type != null) {
                player.removePotionEffect(type);
            }
        })));
        registry.register(new SimpleEffect("clear_potions", context ->
                context.player().ifPresent(player -> player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType())))));
        registry.register(new SimpleEffect("give_item", context -> context.player().ifPresent(player -> {
            Material material = Material.matchMaterial(BuiltinValue.string(context, "material", "STONE"));
            if (material != null) {
                player.getInventory().addItem(new ItemStack(material, Math.max(1, BuiltinValue.integer(context, "amount", 1))));
            }
        })));
        registry.register(new SimpleEffect("take_item", context -> context.player().ifPresent(player -> {
            Material material = Material.matchMaterial(BuiltinValue.string(context, "material", "STONE"));
            if (material != null) {
                player.getInventory().removeItem(new ItemStack(material, Math.max(1, BuiltinValue.integer(context, "amount", 1))));
            }
        })));
        registry.register(new SimpleEffect("clear_inventory", context ->
                context.player().ifPresent(player -> player.getInventory().clear())));
        registry.register(new SimpleEffect("set_gamemode", context -> context.player().ifPresent(player -> {
            try {
                player.setGameMode(GameMode.valueOf(BuiltinValue.string(context, "mode", "SURVIVAL").toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Unknown gamemode: " + BuiltinValue.string(context, "mode", ""));
            }
        })));
        registry.register(new SimpleEffect("set_flying", context ->
                context.player().ifPresent(player -> player.setFlying(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("set_allow_flight", context ->
                context.player().ifPresent(player -> player.setAllowFlight(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("set_exp", context ->
                context.player().ifPresent(player -> player.setExp(Math.max(0, Math.min(1, BuiltinValue.floating(context, "amount", 0)))))));
        registry.register(new SimpleEffect("give_exp", context ->
                context.player().ifPresent(player -> player.giveExp(BuiltinValue.integer(context, "amount", 1)))));
        registry.register(new SimpleEffect("set_level", context ->
                context.player().ifPresent(player -> player.setLevel(Math.max(0, BuiltinValue.integer(context, "level", 0))))));
        registry.register(new SimpleEffect("add_level", context ->
                context.player().ifPresent(player -> player.setLevel(Math.max(0, player.getLevel() + BuiltinValue.integer(context, "amount", 1))))));
        registry.register(new SimpleEffect("spawn_particle", context -> {
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
        registry.register(new SimpleEffect("strike_lightning_effect", context ->
                context.location().ifPresent(location -> location.getWorld().strikeLightningEffect(location))));
        registry.register(new SimpleEffect("strike_lightning", context ->
                context.location().ifPresent(location -> location.getWorld().strikeLightning(location))));
        registry.register(new SimpleEffect("explosion", context ->
                context.location().ifPresent(location -> location.getWorld().createExplosion(location, BuiltinValue.floating(context, "power", 0), BuiltinValue.bool(context, "fire", false), BuiltinValue.bool(context, "break_blocks", false)))));
        registry.register(new SimpleEffect("set_time", context ->
                context.world().ifPresent(world -> world.setTime(BuiltinValue.integer(context, "time", 0)))));
        registry.register(new SimpleEffect("set_weather_clear", context -> context.world().ifPresent(world -> {
            world.setStorm(false);
            world.setThundering(false);
        })));
        registry.register(new SimpleEffect("set_weather_rain", context -> context.world().ifPresent(world -> {
            world.setStorm(true);
            world.setThundering(false);
        })));
        registry.register(new SimpleEffect("set_weather_thunder", context -> context.world().ifPresent(world -> {
            world.setStorm(true);
            world.setThundering(true);
        })));
        registry.register(new SimpleEffect("play_world_sound", context -> {
            Location location = context.location().orElse(null);
            if (location != null && location.getWorld() != null) {
                try {
                    location.getWorld().playSound(location, Sound.valueOf(BuiltinValue.string(context, "sound", "ENTITY_PLAYER_LEVELUP")), BuiltinValue.floating(context, "volume", 1), BuiltinValue.floating(context, "pitch", 1));
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().warning("Unknown sound: " + BuiltinValue.string(context, "sound", ""));
                }
            }
        }));
        registry.register(new SimpleEffect("stop_sound", context -> context.player().ifPresent(player -> {
            String sound = BuiltinValue.string(context, "sound", "");
            if (!sound.isBlank()) {
                player.stopSound(sound);
            }
        })));
        registry.register(new SimpleEffect("set_velocity", context ->
                context.entity().ifPresent(entity -> entity.setVelocity(new Vector(BuiltinValue.decimal(context, "x", 0), BuiltinValue.decimal(context, "y", 0), BuiltinValue.decimal(context, "z", 0))))));
        registry.register(new SimpleEffect("launch_up", context ->
                context.entity().ifPresent(entity -> entity.setVelocity(entity.getVelocity().setY(BuiltinValue.decimal(context, "power", 1))))));
        registry.register(new SimpleEffect("close_inventory", context ->
                context.player().ifPresent(player -> player.closeInventory())));
        registry.register(new SimpleEffect("open_workbench", context ->
                context.player().ifPresent(player -> player.openWorkbench(player.getLocation(), true))));
        registry.register(new SimpleEffect("kick", context ->
                context.player().ifPresent(player -> player.kick(Component.text(placeholders.apply(BuiltinValue.string(context, "reason", "Kicked"), context))))));
        registry.register(new SimpleEffect("set_display_name", context ->
                context.player().ifPresent(player -> player.setDisplayName(placeholders.apply(BuiltinValue.string(context, "name", player.getName()), context)))));
        registry.register(new SimpleEffect("reset_display_name", context ->
                context.player().ifPresent(player -> player.setDisplayName(player.getName()))));
        registry.register(new SimpleEffect("set_player_list_name", context ->
                context.player().ifPresent(player -> player.setPlayerListName(placeholders.apply(BuiltinValue.string(context, "name", player.getName()), context)))));
        registry.register(new SimpleEffect("set_glowing", context ->
                context.entity().ifPresent(entity -> entity.setGlowing(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("set_invulnerable", context ->
                context.entity().ifPresent(entity -> entity.setInvulnerable(BuiltinValue.bool(context, "value", true)))));
        registry.register(new SimpleEffect("spawn_entity", context -> {
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
        registry.register(new SimpleEffect("delay_console_command", context -> {
            int delay = Math.max(1, BuiltinValue.integer(context, "delay", 20));
            String command = placeholders.apply(BuiltinValue.string(context, "command", ""), context);
            if (!command.isBlank()) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), delay);
            }
        }));
        registry.register(new SimpleEffect("delayed_message", context -> {
            int delay = Math.max(1, BuiltinValue.integer(context, "delay", 20));
            String message = placeholders.apply(BuiltinValue.string(context, "message", ""), context);
            context.sender().ifPresent(sender -> Bukkit.getScheduler().runTaskLater(plugin, () -> sender.sendMessage(message), delay));
        }));
        registry.register(new SimpleEffect("repeat_console_command", context -> {
            int times = Math.max(1, BuiltinValue.integer(context, "times", 1));
            String command = placeholders.apply(BuiltinValue.string(context, "command", ""), context);
            for (int i = 0; i < times && !command.isBlank(); i++) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), (long) i * Math.max(1, BuiltinValue.integer(context, "interval", 20)));
            }
        }));
        registry.register(new SimpleEffect("set_max_health", context -> setAttribute(context, "GENERIC_MAX_HEALTH", BuiltinValue.decimal(context, "amount", 20))));
        registry.register(new SimpleEffect("add_max_health", context -> addAttribute(context, "GENERIC_MAX_HEALTH", BuiltinValue.decimal(context, "amount", 1))));
        registry.register(new SimpleEffect("reset_max_health", context -> resetAttribute(context, "GENERIC_MAX_HEALTH")));
        registry.register(new SimpleEffect("set_attack_damage", context -> setAttribute(context, "GENERIC_ATTACK_DAMAGE", BuiltinValue.decimal(context, "amount", 1))));
        registry.register(new SimpleEffect("add_attack_damage", context -> addAttribute(context, "GENERIC_ATTACK_DAMAGE", BuiltinValue.decimal(context, "amount", 1))));
        registry.register(new SimpleEffect("set_attack_speed", context -> setAttribute(context, "GENERIC_ATTACK_SPEED", BuiltinValue.decimal(context, "amount", 4))));
        registry.register(new SimpleEffect("set_movement_speed", context -> setAttribute(context, "GENERIC_MOVEMENT_SPEED", BuiltinValue.decimal(context, "amount", 0.1))));
        registry.register(new SimpleEffect("add_movement_speed", context -> addAttribute(context, "GENERIC_MOVEMENT_SPEED", BuiltinValue.decimal(context, "amount", 0.01))));
        registry.register(new SimpleEffect("set_armor", context -> setAttribute(context, "GENERIC_ARMOR", BuiltinValue.decimal(context, "amount", 0))));
        registry.register(new SimpleEffect("add_armor", context -> addAttribute(context, "GENERIC_ARMOR", BuiltinValue.decimal(context, "amount", 1))));
        registry.register(new SimpleEffect("set_armor_toughness", context -> setAttribute(context, "GENERIC_ARMOR_TOUGHNESS", BuiltinValue.decimal(context, "amount", 0))));
        registry.register(new SimpleEffect("set_knockback_resistance", context -> setAttribute(context, "GENERIC_KNOCKBACK_RESISTANCE", BuiltinValue.decimal(context, "amount", 0))));
        registry.register(new SimpleEffect("set_luck", context -> setAttribute(context, "GENERIC_LUCK", BuiltinValue.decimal(context, "amount", 0))));
        registry.register(new SimpleEffect("set_attribute", context -> setAttribute(context, BuiltinValue.string(context, "attribute", ""), BuiltinValue.decimal(context, "amount", 0))));
        registry.register(new SimpleEffect("add_attribute", context -> addAttribute(context, BuiltinValue.string(context, "attribute", ""), BuiltinValue.decimal(context, "amount", 1))));
        registry.register(new SimpleEffect("reset_attribute", context -> resetAttribute(context, BuiltinValue.string(context, "attribute", ""))));
        registry.register(new SimpleEffect("set_absorption", context ->
                context.player().ifPresent(player -> player.setAbsorptionAmount(Math.max(0, BuiltinValue.decimal(context, "amount", 0))))));
        registry.register(new SimpleEffect("add_absorption", context ->
                context.player().ifPresent(player -> player.setAbsorptionAmount(Math.max(0, player.getAbsorptionAmount() + BuiltinValue.decimal(context, "amount", 1))))));
        registry.register(new SimpleEffect("set_exhaustion", context ->
                context.player().ifPresent(player -> player.setExhaustion(Math.max(0, BuiltinValue.floating(context, "amount", 0))))));
        registry.register(new SimpleEffect("add_exhaustion", context ->
                context.player().ifPresent(player -> player.setExhaustion(Math.max(0, player.getExhaustion() + BuiltinValue.floating(context, "amount", 1))))));
        registry.register(new SimpleEffect("set_remaining_air", context ->
                context.player().ifPresent(player -> player.setRemainingAir(Math.max(0, BuiltinValue.integer(context, "ticks", 300))))));
        registry.register(new SimpleEffect("set_freeze_ticks", context ->
                context.player().ifPresent(player -> player.setFreezeTicks(Math.max(0, BuiltinValue.integer(context, "ticks", 0))))));
        registry.register(new SimpleEffect("set_no_damage_ticks", context ->
                context.player().ifPresent(player -> player.setNoDamageTicks(Math.max(0, BuiltinValue.integer(context, "ticks", 0))))));
        registry.register(new SimpleEffect("increment_stat", context -> changeStatistic(plugin, context, StatisticAction.INCREMENT)));
        registry.register(new SimpleEffect("decrement_stat", context -> changeStatistic(plugin, context, StatisticAction.DECREMENT)));
        registry.register(new SimpleEffect("set_stat", context -> changeStatistic(plugin, context, StatisticAction.SET)));
        registry.register(new SimpleEffect("reset_stat", context -> {
            context.data().put("amount", "0");
            changeStatistic(plugin, context, StatisticAction.SET);
        }));
        registry.register(new SimpleEffect("vault_deposit", context -> context.player().ifPresent(player ->
                plugin.integrations().vault().flatMap(vault -> vault.economy()).ifPresent(economy ->
                        economy.depositPlayer(player, Math.max(0, BuiltinValue.decimal(context, "amount", 0)))))));
        registry.register(new SimpleEffect("vault_withdraw", context -> context.player().ifPresent(player ->
                plugin.integrations().vault().flatMap(vault -> vault.economy()).ifPresent(economy ->
                        economy.withdrawPlayer(player, Math.max(0, BuiltinValue.decimal(context, "amount", 0)))))));
        registry.register(new SimpleEffect("vault_set_balance", context -> context.player().ifPresent(player ->
                plugin.integrations().vault().flatMap(vault -> vault.economy()).ifPresent(economy -> {
                    double target = Math.max(0, BuiltinValue.decimal(context, "amount", 0));
                    double current = economy.getBalance(player);
                    if (target > current) {
                        economy.depositPlayer(player, target - current);
                    } else if (target < current) {
                        economy.withdrawPlayer(player, current - target);
                    }
                }))));
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

    private static void setAttribute(ExecutionContext context, String attributeName, double amount) {
        context.player().ifPresent(player -> attribute(player, attributeName, instance -> {
            instance.setBaseValue(Math.max(0, amount));
            if (attributeName.equalsIgnoreCase("GENERIC_MAX_HEALTH") && player.getHealth() > instance.getValue()) {
                player.setHealth(instance.getValue());
            }
        }));
    }

    private static void addAttribute(ExecutionContext context, String attributeName, double amount) {
        context.player().ifPresent(player -> attribute(player, attributeName, instance -> {
            instance.setBaseValue(Math.max(0, instance.getBaseValue() + amount));
            if (attributeName.equalsIgnoreCase("GENERIC_MAX_HEALTH") && player.getHealth() > instance.getValue()) {
                player.setHealth(instance.getValue());
            }
        }));
    }

    private static void resetAttribute(ExecutionContext context, String attributeName) {
        context.player().ifPresent(player -> attribute(player, attributeName, instance -> instance.setBaseValue(instance.getDefaultValue())));
    }

    private static void attribute(org.bukkit.entity.Player player, String attributeName, java.util.function.Consumer<AttributeInstance> action) {
        try {
            Attribute attribute = Attribute.valueOf(attributeName.toUpperCase(Locale.ROOT));
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance != null) {
                action.accept(instance);
            }
        } catch (RuntimeException ignored) {
        }
    }

    private static void changeStatistic(ATXCorePlugin plugin, ExecutionContext context, StatisticAction action) {
        context.player().ifPresent(player -> {
            try {
                Statistic statistic = Statistic.valueOf(BuiltinValue.string(context, "stat", "").toUpperCase(Locale.ROOT));
                int amount = Math.max(0, BuiltinValue.integer(context, "amount", 1));
                Material material = Material.matchMaterial(BuiltinValue.string(context, "material", ""));
                String entityName = BuiltinValue.string(context, "entity_type", "");
                if (material != null) {
                    applyStatistic(player, statistic, material, action, amount);
                } else if (!entityName.isBlank()) {
                    applyStatistic(player, statistic, EntityType.valueOf(entityName.toUpperCase(Locale.ROOT)), action, amount);
                } else {
                    applyStatistic(player, statistic, action, amount);
                }
            } catch (IllegalArgumentException exception) {
                plugin.getLogger().warning("Invalid statistic data: " + BuiltinValue.string(context, "stat", ""));
            }
        });
    }

    private static void applyStatistic(org.bukkit.entity.Player player, Statistic statistic, StatisticAction action, int amount) {
        if (action == StatisticAction.SET) {
            player.setStatistic(statistic, amount);
        } else if (action == StatisticAction.INCREMENT) {
            player.incrementStatistic(statistic, amount);
        } else {
            player.decrementStatistic(statistic, amount);
        }
    }

    private static void applyStatistic(org.bukkit.entity.Player player, Statistic statistic, Material material, StatisticAction action, int amount) {
        if (action == StatisticAction.SET) {
            player.setStatistic(statistic, material, amount);
        } else if (action == StatisticAction.INCREMENT) {
            player.incrementStatistic(statistic, material, amount);
        } else {
            player.decrementStatistic(statistic, material, amount);
        }
    }

    private static void applyStatistic(org.bukkit.entity.Player player, Statistic statistic, EntityType entityType, StatisticAction action, int amount) {
        if (action == StatisticAction.SET) {
            player.setStatistic(statistic, entityType, amount);
        } else if (action == StatisticAction.INCREMENT) {
            player.incrementStatistic(statistic, entityType, amount);
        } else {
            player.decrementStatistic(statistic, entityType, amount);
        }
    }

    private enum StatisticAction {
        INCREMENT,
        DECREMENT,
        SET
    }
}
