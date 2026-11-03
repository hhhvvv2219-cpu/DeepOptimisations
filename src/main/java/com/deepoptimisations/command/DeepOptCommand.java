package com.deepoptimisations.command;

import com.deepoptimisations.config.DeepOptConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class DeepOptCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deepopt")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("status")
                        .executes(ctx -> {
                            DeepOptConfig cfg = DeepOptConfig.get();
                            ctx.getSource().sendFeedback(() -> Text.literal(
                                    "AI throttle: " + cfg.aiThrottleEnabled
                                            + " (full " + cfg.aiFullDistance + ", reduce " + cfg.aiReduceDistance + ")"
                                            + " | Movement: " + cfg.movementThrottleEnabled
                                            + " (full " + cfg.movementFullDistance + ", reduce " + cfg.movementReduceDistance + ")"
                            ), false);
                            return 1;
                        }))
                .then(CommandManager.literal("reload")
                        .executes(ctx -> {
                            DeepOptConfig.load();
                            ctx.getSource().sendFeedback(() -> Text.literal("DeepOptimisations config reloaded."), false);
                            return 1;
                        }))
        );
    }
}
