package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.api.registry.CoreRegistry;
import com.ataraxia.atxcore.mechanic.ExecutionContext;
import com.ataraxia.atxcore.mechanic.trigger.Trigger;
import com.ataraxia.atxcore.mechanic.trigger.TriggerHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class BuiltinTriggers {
    private BuiltinTriggers() {
    }

    public static void register(ATXCorePlugin plugin, CoreRegistry<Trigger> registry) {
        registry.register(new Trigger() {
            private Listener listener;

            @Override
            public String id() {
                return "player_join";
            }

            @Override
            public void start(TriggerHandler handler) {
                listener = new Listener() {
                    @EventHandler
                    public void onJoin(PlayerJoinEvent event) {
                        handler.handle(ExecutionContext.builder()
                                .player(event.getPlayer())
                                .event(event)
                                .build());
                    }
                };
                plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            }

            @Override
            public void stop() {
                if (listener != null) {
                    HandlerList.unregisterAll(listener);
                    listener = null;
                }
            }
        });
    }
}
