package com.deepoptimisations;

import com.deepoptimisations.command.DeepOptCommand;
import com.deepoptimisations.config.DeepOptConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepOptimisations implements ModInitializer {
    public static final String MOD_ID = "deepoptimisations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        DeepOptConfig.load();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                DeepOptCommand.register(dispatcher));
        LOGGER.info("[DeepOptimisations] Loaded. AI throttle: {}, Movement throttle: {}",
                DeepOptConfig.get().aiThrottleEnabled,
                DeepOptConfig.get().movementThrottleEnabled);
    }
}
