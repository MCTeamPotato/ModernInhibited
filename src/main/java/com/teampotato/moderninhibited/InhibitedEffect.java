package com.teampotato.moderninhibited;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InhibitedEffect extends MobEffect {
    protected InhibitedEffect() {
        super(MobEffectCategory.HARMFUL, 16711680);
    }

    public boolean isBeneficial() {
        return false;
    }
}
