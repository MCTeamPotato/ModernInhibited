package com.teampotato.moderninhibited;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InhibitedEffect extends Effect {

    protected InhibitedEffect() {
        super(EffectType.HARMFUL, 16711680);
    }

    public static final InhibitedEffect INSTANCE = new InhibitedEffect();

    @Override
    public boolean isBeneficial() {
        return false;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    @Override public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {}
    @Override public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {}
}
