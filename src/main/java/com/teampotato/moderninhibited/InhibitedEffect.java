package com.teampotato.moderninhibited;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InhibitedEffect extends MobEffect {

    protected InhibitedEffect() {
        super(MobEffectCategory.HARMFUL, 16711680);
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }

    @Override public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {}
    @Override public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {}
}
