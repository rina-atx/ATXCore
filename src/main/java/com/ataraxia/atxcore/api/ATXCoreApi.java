package com.ataraxia.atxcore.api;

import com.ataraxia.atxcore.api.registry.CoreRegistries;
import com.ataraxia.atxcore.integration.IntegrationManager;
import com.ataraxia.atxcore.mechanic.config.EcoStyleMechanicLoader;
import com.ataraxia.atxcore.placeholder.PlaceholderService;

public interface ATXCoreApi {
    CoreRegistries registries();

    PlaceholderService placeholders();

    IntegrationManager integrations();

    EcoStyleMechanicLoader mechanicLoader();
}
