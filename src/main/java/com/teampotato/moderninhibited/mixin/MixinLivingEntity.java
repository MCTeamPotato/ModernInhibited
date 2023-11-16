package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.ModernInhibited;
import com.teampotato.moderninhibited.api.IServerPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Inject(method = "onStatusEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/attribute/AttributeContainer;I)V"))
    private void onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
        if (((LivingEntity)(Object)this) instanceof ServerPlayerEntity serverPlayer && effect.getEffectType().equals(ModernInhibited.INHIBITED) && serverPlayer.interactionManager.getGameMode().equals(GameMode.ADVENTURE)) {
            serverPlayer.changeGameMode(GameMode.SURVIVAL);
            ((IServerPlayerEntity)serverPlayer).modernInhibited$setTickCount(29);
        }
    }
}
