package com.deepoptimisations.config;

import com.deepoptimisations.DeepOptimisations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeepOptConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("deepoptimisations.json");

    private static DeepOptConfig INSTANCE = new DeepOptConfig();

    public boolean aiThrottleEnabled = true;
    public double aiFullDistance = 32.0;
    public double aiReduceDistance = 64.0;
    public int aiSkipTicks = 2;

    public boolean movementThrottleEnabled = true;
    public double movementFullDistance = 48.0;
    public double movementReduceDistance = 96.0;
    public int movementSkipTicks = 4;

    public static DeepOptConfig get() {
        return INSTANCE;
    }

    public static void load() {
        if (Files.exists(PATH)) {
            try {
                INSTANCE = GSON.fromJson(Files.readString(PATH), DeepOptConfig.class);
            } catch (Exception e) {
                DeepOptimisations.LOGGER.error("[DeepOptimisations] Failed to read config, using defaults", e);
            }
        }
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            DeepOptimisations.LOGGER.error("[DeepOptimisations] Failed to save config", e);
        }
    }
}
