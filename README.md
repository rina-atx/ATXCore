# ATXCore

ATXCore is a Paper/Bukkit core plugin for future ATX plugins. It exposes registries and services for:

- Effects
- Triggers
- Mutators
- Filters
- Conditions
- Placeholders
- Integrations

See the [ATXCore Wiki](https://github.com/rina-atx/ATXCore/wiki) for tutorials, examples, and the full built-in effects, triggers, conditions, filters, mutators, and placeholders catalog.

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
