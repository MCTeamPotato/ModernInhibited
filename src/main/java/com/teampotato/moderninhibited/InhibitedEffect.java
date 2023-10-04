package com.teampotato.moderninhibited;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class InhibitedEffect extends StatusEffect {
    protected InhibitedEffect() {
        super(StatusEffectCategory.HARMFUL, 16711680);
    }

    public boolean isBeneficial() {
        return false;
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof ServerPlayerEntity serverPlayer) serverPlayer.changeGameMode(GameMode.SURVIVAL);
        super.onRemoved(entity, attributes, amplifier);
    }
}