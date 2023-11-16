package com.teampotato.moderninhibited;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class InhibitedEffect extends StatusEffect {
    protected InhibitedEffect() {
        super(StatusEffectCategory.HARMFUL, 16711680);
    }

    public boolean isBeneficial() {
        return false;
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof ServerPlayerEntity serverPlayer && serverPlayer.isCreative()) serverPlayer.removeStatusEffect(this);
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {}
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {}
    public boolean canApplyUpdateEffect(int duration, int amplifier){ return false;}
}