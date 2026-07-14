package com.ataraxia.atxcore.mechanic;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ExecutionContext {
    private final CommandSender sender;
    private final Player player;
    private final Entity entity;
    private final Location location;
    private final Event event;
    private final Map<String, Object> data;

    private ExecutionContext(Builder builder) {
        this.sender = builder.sender;
        this.player = builder.player;
        this.entity = builder.entity;
        this.location = builder.location;
        this.event = builder.event;
        this.data = new HashMap<>(builder.data);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<CommandSender> sender() {
        return Optional.ofNullable(sender);
    }

    public Optional<Player> player() {
        return Optional.ofNullable(player);
    }

    public Optional<Entity> entity() {
        return Optional.ofNullable(entity);
    }

    public Optional<Location> location() {
        return Optional.ofNullable(location);
    }

    public Optional<World> world() {
        if (location != null) {
            return Optional.ofNullable(location.getWorld());
        }
        if (player != null) {
            return Optional.of(player.getWorld());
        }
        if (entity != null) {
            return Optional.of(entity.getWorld());
        }
        return Optional.empty();
    }

    public Optional<Event> event() {
        return Optional.ofNullable(event);
    }

    public Map<String, Object> data() {
        return data;
    }

    public Optional<String> stringData(String key) {
        Object value = data.get(key);
        return value == null ? Optional.empty() : Optional.of(String.valueOf(value));
    }

    public static final class Builder {
        private CommandSender sender;
        private Player player;
        private Entity entity;
        private Location location;
        private Event event;
        private final Map<String, Object> data = new HashMap<>();

        public Builder sender(CommandSender sender) {
            this.sender = sender;
            return this;
        }

        public Builder player(Player player) {
            this.player = player;
            this.sender = player;
            this.entity = player;
            this.location = player.getLocation();
            return this;
        }

        public Builder entity(Entity entity) {
            this.entity = entity;
            this.location = entity.getLocation();
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder event(Event event) {
            this.event = event;
            return this;
        }

        public Builder data(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public ExecutionContext build() {
            return new ExecutionContext(this);
        }
    }
}
