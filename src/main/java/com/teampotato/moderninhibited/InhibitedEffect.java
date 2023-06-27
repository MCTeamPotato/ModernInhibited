package com.teampotato.moderninhibited;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.GameType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InhibitedEffect extends Effect {
    protected InhibitedEffect() {
        super(EffectType.HARMFUL, 16711680);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof PlayerEntity && !((PlayerEntity) pLivingEntity).isCreative() && !pLivingEntity.isSpectator()) {
            ((PlayerEntity) pLivingEntity).setGameMode(GameType.ADVENTURE);
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration > 0;
    }

    public boolean isBeneficial() {
        return false;
    }
}
