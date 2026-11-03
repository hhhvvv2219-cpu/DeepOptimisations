package com.deepoptimisations.mixin;

import com.deepoptimisations.config.DeepOptConfig;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityAiMixin {

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    private void deepoptimisations$throttleAi(CallbackInfo ci) {
        DeepOptConfig cfg = DeepOptConfig.get();
        if (!cfg.aiThrottleEnabled) return;

        MobEntity self = (MobEntity) (Object) this;
        if (!(self.getWorld() instanceof ServerWorld serverWorld)) return;

        double closest = serverWorld.getPlayers().stream()
                .filter(p -> !p.isSpectator())
                .mapToDouble(p -> p.squaredDistanceTo(self))
                .min()
                .orElse(Double.MAX_VALUE);

        double fullSq = cfg.aiFullDistance * cfg.aiFullDistance;
        double reduceSq = cfg.aiReduceDistance * cfg.aiReduceDistance;

        if (closest > reduceSq) {
            ci.cancel();
        } else if (closest > fullSq && (serverWorld.getTime() % cfg.aiSkipTicks != 0)) {
            ci.cancel();
        }
    }
}
