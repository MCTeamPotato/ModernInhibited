package com.teampotato.moderninhibited;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InhibitedEffect extends Effect {
    protected InhibitedEffect() {
        super(EffectType.HARMFUL, 16711680);
    }

    public boolean isBeneficial() {
        return false;
    }
}
