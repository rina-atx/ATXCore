# ATXCore

ATXCore is a Paper/Bukkit core plugin for future ATX plugins. It exposes registries and services for:

- Effects
- Triggers
- Mutators
- Filters
- Conditions
- Placeholders
- Integrations

See [MECHANICS.md](MECHANICS.md) for the full built-in effects, conditions, filters, mutators, and placeholders catalog.

The hosted mechanics reference is available at <https://rina-atx.github.io/ATXCore/>.

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
