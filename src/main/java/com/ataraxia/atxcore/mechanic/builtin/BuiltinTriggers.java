package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.trigger.Trigger;
import com.ataraxia.atxcore.mechanic.trigger.TriggerHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

public final class BuiltinTriggers {
    private BuiltinTriggers() {
    }

    public static void register(ATXCorePlugin plugin, CoreRegistry<Trigger> registry) {
        registry.register(trigger(plugin, "player_join", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerJoinEvent event) {
                handler.handle(player(event.getPlayer(), event).data("join_message", event.getJoinMessage()).build());
            }
        }));
        registry.register(trigger(plugin, "player_quit", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerQuitEvent event) {
                handler.handle(player(event.getPlayer(), event).data("quit_message", event.getQuitMessage()).build());
            }
        }));
        registry.register(trigger(plugin, "player_kick", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerKickEvent event) {
                handler.handle(player(event.getPlayer(), event).data("reason", event.getReason()).build());
            }
        }));
        registry.register(trigger(plugin, "player_death", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerDeathEvent event) {
                handler.handle(player(event.getEntity(), event).data("death_message", event.getDeathMessage()).build());
            }
        }));
        registry.register(trigger(plugin, "player_respawn", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerRespawnEvent event) {
                handler.handle(player(event.getPlayer(), event).location(event.getRespawnLocation()).data("bed_spawn", event.isBedSpawn()).data("anchor_spawn", event.isAnchorSpawn()).build());
            }
        }));
        registry.register(trigger(plugin, "player_changed_world", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerChangedWorldEvent event) {
                handler.handle(player(event.getPlayer(), event).data("from_world", event.getFrom().getName()).data("to_world", event.getPlayer().getWorld().getName()).build());
            }
        }));
        registry.register(trigger(plugin, "player_move", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerMoveEvent event) {
                handler.handle(player(event.getPlayer(), event).location(event.getTo()).data("from_world", event.getFrom().getWorld().getName()).build());
            }
        }));
        registry.register(trigger(plugin, "player_teleport", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerTeleportEvent event) {
                handler.handle(player(event.getPlayer(), event).location(event.getTo()).data("cause", event.getCause().name()).build());
            }
        }));
        registry.register(trigger(plugin, "player_chat", handler -> new Listener() {
            @EventHandler
            public void onEvent(AsyncPlayerChatEvent event) {
                ExecutionContext context = player(event.getPlayer(), event).data("message", event.getMessage()).build();
                if (event.isAsynchronous()) {
                    plugin.getServer().getScheduler().runTask(plugin, () -> handler.handle(context));
                    return;
                }
                handler.handle(context);
            }
        }));
        registry.register(trigger(plugin, "player_command", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerCommandPreprocessEvent event) {
                handler.handle(player(event.getPlayer(), event).data("command", event.getMessage()).build());
            }
        }));
        registry.register(trigger(plugin, "player_interact", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerInteractEvent event) {
                ExecutionContext.Builder builder = player(event.getPlayer(), event).data("action", event.getAction().name());
                if (event.getClickedBlock() != null) {
                    builder.location(event.getClickedBlock().getLocation()).data("block_type", event.getClickedBlock().getType().name());
                }
                if (event.getItem() != null) {
                    builder.data("material", event.getItem().getType().name());
                }
                handler.handle(builder.build());
            }
        }));
        registry.register(trigger(plugin, "player_drop_item", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerDropItemEvent event) {
                handler.handle(player(event.getPlayer(), event).data("material", event.getItemDrop().getItemStack().getType().name()).data("amount", event.getItemDrop().getItemStack().getAmount()).build());
            }
        }));
        registry.register(trigger(plugin, "player_pickup_item", handler -> new Listener() {
            @EventHandler
            public void onEvent(EntityPickupItemEvent event) {
                if (event.getEntity() instanceof Player player) {
                    handler.handle(player(player, event).data("material", event.getItem().getItemStack().getType().name()).data("amount", event.getItem().getItemStack().getAmount()).build());
                }
            }
        }));
        registry.register(trigger(plugin, "player_consume_item", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerItemConsumeEvent event) {
                handler.handle(player(event.getPlayer(), event).data("material", event.getItem().getType().name()).build());
            }
        }));
        registry.register(trigger(plugin, "player_item_break", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerItemBreakEvent event) {
                handler.handle(player(event.getPlayer(), event).data("material", event.getBrokenItem().getType().name()).build());
            }
        }));
        registry.register(trigger(plugin, "player_swap_hands", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerSwapHandItemsEvent event) {
                handler.handle(player(event.getPlayer(), event).build());
            }
        }));
        registry.register(trigger(plugin, "player_level_change", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerLevelChangeEvent event) {
                handler.handle(player(event.getPlayer(), event).data("old_level", event.getOldLevel()).data("new_level", event.getNewLevel()).build());
            }
        }));
        registry.register(trigger(plugin, "player_exp_change", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerExpChangeEvent event) {
                handler.handle(player(event.getPlayer(), event).data("amount", event.getAmount()).build());
            }
        }));
        registry.register(trigger(plugin, "block_break", handler -> new Listener() {
            @EventHandler
            public void onEvent(BlockBreakEvent event) {
                handler.handle(player(event.getPlayer(), event).location(event.getBlock().getLocation()).data("block_type", event.getBlock().getType().name()).data("exp", event.getExpToDrop()).build());
            }
        }));
        registry.register(trigger(plugin, "block_place", handler -> new Listener() {
            @EventHandler
            public void onEvent(BlockPlaceEvent event) {
                handler.handle(player(event.getPlayer(), event).location(event.getBlock().getLocation()).data("block_type", event.getBlock().getType().name()).build());
            }
        }));
        registry.register(trigger(plugin, "entity_damage", handler -> new Listener() {
            @EventHandler
            public void onEvent(EntityDamageEvent event) {
                handler.handle(ExecutionContext.builder().entity(event.getEntity()).event(event).data("damage", event.getDamage()).data("final_damage", event.getFinalDamage()).data("cause", event.getCause().name()).build());
            }
        }));
        registry.register(trigger(plugin, "entity_damage_by_entity", handler -> new Listener() {
            @EventHandler
            public void onEvent(EntityDamageByEntityEvent event) {
                handler.handle(ExecutionContext.builder()
                        .actor(event.getDamager())
                        .target(event.getEntity())
                        .activeEntity(event.getEntity())
                        .event(event)
                        .location(event.getEntity().getLocation())
                        .data("actor_type", event.getDamager().getType().name())
                        .data("target_type", event.getEntity().getType().name())
                        .data("damager_type", event.getDamager().getType().name())
                        .data("damage", event.getDamage())
                        .data("final_damage", event.getFinalDamage())
                        .data("cause", event.getCause().name())
                        .build());
            }
        }));
        registry.register(trigger(plugin, "melee_attack", handler -> new Listener() {
            @EventHandler
            public void onEvent(EntityDamageByEntityEvent event) {
                if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                    return;
                }
                handler.handle(ExecutionContext.builder()
                        .actor(event.getDamager())
                        .target(event.getEntity())
                        .activeEntity(event.getDamager())
                        .event(event)
                        .location(event.getDamager().getLocation())
                        .data("actor_type", event.getDamager().getType().name())
                        .data("target_type", event.getEntity().getType().name())
                        .data("damage", event.getDamage())
                        .data("final_damage", event.getFinalDamage())
                        .data("cause", event.getCause().name())
                        .build());
            }
        }));
        registry.register(trigger(plugin, "projectile_attack", handler -> new Listener() {
            @EventHandler
            public void onEvent(EntityDamageByEntityEvent event) {
                if (!(event.getDamager() instanceof Projectile projectile) || !(projectile.getShooter() instanceof org.bukkit.entity.Entity shooter)) {
                    return;
                }
                handler.handle(ExecutionContext.builder()
                        .actor(shooter)
                        .target(event.getEntity())
                        .activeEntity(shooter)
                        .event(event)
                        .location(shooter.getLocation())
                        .data("actor_type", shooter.getType().name())
                        .data("target_type", event.getEntity().getType().name())
                        .data("projectile_type", projectile.getType().name())
                        .data("damage", event.getDamage())
                        .data("final_damage", event.getFinalDamage())
                        .data("cause", event.getCause().name())
                        .build());
            }
        }));
        registry.register(trigger(plugin, "entity_death", handler -> new Listener() {
            @EventHandler
            public void onEvent(EntityDeathEvent event) {
                handler.handle(ExecutionContext.builder().target(event.getEntity()).activeEntity(event.getEntity()).event(event).location(event.getEntity().getLocation()).data("target_type", event.getEntityType().name()).data("entity_type", event.getEntityType().name()).data("dropped_exp", event.getDroppedExp()).build());
            }
        }));
        registry.register(trigger(plugin, "food_level_change", handler -> new Listener() {
            @EventHandler
            public void onEvent(FoodLevelChangeEvent event) {
                if (event.getEntity() instanceof Player player) {
                    handler.handle(player(player, event).data("food", event.getFoodLevel()).build());
                }
            }
        }));
        registry.register(trigger(plugin, "projectile_hit", handler -> new Listener() {
            @EventHandler
            public void onEvent(ProjectileHitEvent event) {
                handler.handle(ExecutionContext.builder().entity(event.getEntity()).event(event).location(event.getEntity().getLocation()).data("projectile_type", event.getEntityType().name()).build());
            }
        }));
        registry.register(trigger(plugin, "entity_shoot_bow", handler -> new Listener() {
            @EventHandler
            public void onEvent(EntityShootBowEvent event) {
                handler.handle(ExecutionContext.builder().entity(event.getEntity()).event(event).location(event.getEntity().getLocation()).data("force", event.getForce()).build());
            }
        }));
        registry.register(trigger(plugin, "inventory_click", handler -> new Listener() {
            @EventHandler
            public void onEvent(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player player) {
                    ExecutionContext.Builder builder = player(player, event).data("slot", event.getSlot()).data("click", event.getClick().name());
                    if (event.getCurrentItem() != null) {
                        builder.data("material", event.getCurrentItem().getType().name()).data("amount", event.getCurrentItem().getAmount());
                    }
                    handler.handle(builder.build());
                }
            }
        }));
        registry.register(trigger(plugin, "inventory_open", handler -> new Listener() {
            @EventHandler
            public void onEvent(InventoryOpenEvent event) {
                if (event.getPlayer() instanceof Player player) {
                    handler.handle(player(player, event).data("inventory_type", event.getInventory().getType().name()).build());
                }
            }
        }));
        registry.register(trigger(plugin, "inventory_close", handler -> new Listener() {
            @EventHandler
            public void onEvent(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player player) {
                    handler.handle(player(player, event).data("inventory_type", event.getInventory().getType().name()).build());
                }
            }
        }));
        registry.register(trigger(plugin, "player_fish", handler -> new Listener() {
            @EventHandler
            public void onEvent(PlayerFishEvent event) {
                handler.handle(player(event.getPlayer(), event).data("state", event.getState().name()).build());
            }
        }));
    }

    private static Trigger trigger(ATXCorePlugin plugin, String id, ListenerFactory factory) {
        return new Trigger() {
            private Listener listener;

            @Override
            public String id() {
                return id;
            }

            @Override
            public void start(TriggerHandler handler) {
                stop();
                listener = factory.create(handler);
                plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            }

            @Override
            public void stop() {
                if (listener != null) {
                    HandlerList.unregisterAll(listener);
                    listener = null;
                }
            }
        };
    }

    private static ExecutionContext.Builder player(Player player, org.bukkit.event.Event event) {
        return ExecutionContext.builder()
                .player(player)
                .event(event)
                .data("event", event.getEventName());
    }

    @FunctionalInterface
    private interface ListenerFactory {
        Listener create(TriggerHandler handler);
    }
}

