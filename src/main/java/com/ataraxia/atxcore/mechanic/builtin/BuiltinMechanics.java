package com.ataraxia.atxcore.mechanic.builtin;

import com.ataraxia.atxcore.ATXCorePlugin;
import com.ataraxia.atxcore.api.registry.CoreRegistries;
import com.ataraxia.atxcore.placeholder.PlaceholderService;

public final class BuiltinMechanics {
    private BuiltinMechanics() {
    }

    public static void registerAll(ATXCorePlugin plugin, CoreRegistries registries, PlaceholderService placeholders) {
        BuiltinPlaceholders.register(plugin, placeholders);
        BuiltinEffects.register(plugin, registries.effects(), placeholders);
        BuiltinConditions.register(registries.conditions(), placeholders);
        BuiltinFilters.register(registries.filters());
        BuiltinMutators.register(registries.mutators());
        BuiltinTriggers.register(plugin, registries.triggers());
    }
}
