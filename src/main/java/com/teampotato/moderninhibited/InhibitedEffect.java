package com.teampotato.moderninhibited;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InhibitedEffect extends MobEffect {
    protected InhibitedEffect() {
        super(MobEffectCategory.HARMFUL, 16711680);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player && !((ServerPlayer) pLivingEntity).isCreative() && !pLivingEntity.isSpectator()) {
            ((ServerPlayer) pLivingEntity).setGameMode(GameType.ADVENTURE);
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration > 0;
    }

    public boolean isBeneficial() {
        return false;
    }
}
