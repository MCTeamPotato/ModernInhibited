package com.teampotato.moderninhibited;


import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class InhibitedEffect extends StatusEffect {
    protected InhibitedEffect() {
        super(StatusEffectCategory.HARMFUL, 16711680);
    }

    public boolean isBeneficial() {
        return false;
    }
}