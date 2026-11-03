package com.deepoptimisations.mixin;

import com.deepoptimisations.config.DeepOptConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMovementMixin {

    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void deepoptimisations$throttleMovement(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof PlayerEntity) return;

        DeepOptConfig cfg = DeepOptConfig.get();
        if (!cfg.movementThrottleEnabled) return;
        if (!(self.getWorld() instanceof ServerWorld serverWorld)) return;

        double closest = serverWorld.getPlayers().stream()
                .filter(p -> !p.isSpectator())
                .mapToDouble(p -> p.squaredDistanceTo(self))
                .min()
                .orElse(Double.MAX_VALUE);

        double fullSq = cfg.movementFullDistance * cfg.movementFullDistance;
        double reduceSq = cfg.movementReduceDistance * cfg.movementReduceDistance;

        if (closest > reduceSq) {
            ci.cancel();
        } else if (closest > fullSq && (serverWorld.getTime() % cfg.movementSkipTicks != 0)) {
            ci.cancel();
        }
    }
}
