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
    private final Entity actor;
    private final Entity target;
    private final Location location;
    private final Event event;
    private final Map<String, Object> data;
    private final Map<String, Object> parameters;

    private ExecutionContext(Builder builder) {
        this.sender = builder.sender;
        this.player = builder.player;
        this.entity = builder.entity;
        this.actor = builder.actor;
        this.target = builder.target;
        this.location = builder.location;
        this.event = builder.event;
        this.data = new HashMap<>(builder.data);
        this.parameters = new HashMap<>(builder.parameters);
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

    public Optional<Entity> actor() {
        return Optional.ofNullable(actor);
    }

    public Optional<Player> actorPlayer() {
        return actor instanceof Player player ? Optional.of(player) : Optional.empty();
    }

    public Optional<Entity> target() {
        return Optional.ofNullable(target);
    }

    public Optional<Player> targetPlayer() {
        return target instanceof Player player ? Optional.of(player) : Optional.empty();
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

    public Map<String, Object> parameters() {
        return parameters;
    }

    public Optional<String> stringData(String key) {
        Object value = data.get(key);
        return value == null ? Optional.empty() : Optional.of(String.valueOf(value));
    }

    public Optional<String> parameter(String key) {
        Object value = parameters.get(key);
        return value == null ? Optional.empty() : Optional.of(String.valueOf(value));
    }

    public Optional<String> value(String key) {
        return parameter(key).or(() -> stringData(key));
    }

    public ExecutionContext withParameters(Map<String, Object> parameters) {
        return toBuilder().clearParameters().parameters(parameters).build();
    }

    public Builder toBuilder() {
        return new Builder()
                .sender(sender)
                .activeEntity(entity)
                .actor(actor)
                .target(target)
                .location(location)
                .event(event)
                .data(data)
                .parameters(parameters);
    }

    public static final class Builder {
        private CommandSender sender;
        private Player player;
        private Entity entity;
        private Entity actor;
        private Entity target;
        private Location location;
        private Event event;
        private final Map<String, Object> data = new HashMap<>();
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder sender(CommandSender sender) {
            this.sender = sender;
            return this;
        }

        public Builder player(Player player) {
            return actor(player).target(player).activeEntity(player).sender(player);
        }

        public Builder entity(Entity entity) {
            if (this.actor == null) {
                this.actor = entity;
            }
            if (this.target == null) {
                this.target = entity;
            }
            activeEntity(entity);
            return this;
        }

        public Builder activeEntity(Entity entity) {
            this.entity = entity;
            this.player = entity instanceof Player activePlayer ? activePlayer : null;
            this.location = entity == null ? this.location : entity.getLocation();
            return this;
        }

        public Builder actor(Entity actor) {
            this.actor = actor;
            if (actor instanceof Player actorPlayer && this.sender == null) {
                this.sender = actorPlayer;
            }
            return this;
        }

        public Builder target(Entity target) {
            this.target = target;
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

        public Builder data(Map<String, Object> data) {
            this.data.putAll(data);
            return this;
        }

        public Builder parameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public Builder clearParameters() {
            this.parameters.clear();
            return this;
        }

        public ExecutionContext build() {
            return new ExecutionContext(this);
        }
    }
}
