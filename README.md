# ATXCore

ATXCore is a Paper/Bukkit core plugin for future ATX plugins. It exposes registries and services for:

- Effects
- Triggers
- Mutators
- Filters
- Conditions
- Placeholders
- Integrations

See the [ATXCore Wiki](https://github.com/rina-atx/ATXCore/wiki) for addon-plugin tutorials, the ATXRunes integration example, and the full built-in effects, triggers, conditions, filters, mutators, and placeholders catalog.

Future plugins should depend on `ATXCore` in `plugin.yml`, then fetch the API from Bukkit services:

```java
ATXCoreApi api = Bukkit.getServicesManager().load(ATXCoreApi.class);
```

## Maven

```xml
<dependency>
    <groupId>com.ataraxia</groupId>
    <artifactId>atxcore</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

## Example Registration

```java
api.effects().register(new Effect<ExecutionContext>() {
    @Override
    public String id() {
        return "myplugin:give_xp";
    }

    @Override
    public void execute(ExecutionContext context) {
        context.player().ifPresent(player -> player.giveExp(10));
    }
});
```

Use namespaced IDs such as `myplugin:double_damage` so future additions do not collide.

## Addon YAML Tutorial

ATXCore includes an Eco-style loader for addon plugins that want to store mechanic pipelines in YAML. Use `data:` for each step's local parameters:

```yaml
chains:
  bleed-hit:
    trigger: "melee_attack"
    conditions:
      - id: "actor_health_above"
        data:
          amount: 20
    mutators:
      - id: "target_victim"
    filters:
      - id: "not_target_entity_type"
        data:
          type: "minecraft:creeper"
    effects:
      - id: "damage"
        data:
          amount: 4
      - id: "sound"
        data:
          sound: "ENTITY_PLAYER_ATTACK_CRIT"
          volume: 1
          pitch: 1
```

Load and start a triggered pipeline from an addon plugin:

```java
TriggeredPipeline pipeline = api.mechanicLoader()
    .loadTriggeredPipeline(config.getConfigurationSection("chains.bleed-hit"));
pipeline.start();
```

The current actor/target model is:

- `actor`: the entity that caused the trigger, such as the attacker.
- `target`: the entity the trigger is about, such as the victim.
- `active target`: the entity effects currently hit after mutators run.

For ATXRunes configs, use `args:` instead of `data:` inside `rune-types.yml`. The same mechanic IDs are used; only the wrapper name changes.
