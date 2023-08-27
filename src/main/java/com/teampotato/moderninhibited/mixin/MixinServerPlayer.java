package com.teampotato.moderninhibited.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.moderninhibited.ModernInhibited;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer extends PlayerEntity {

    public MixinServerPlayer(World pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Shadow public abstract boolean isCreative();
    @Shadow public abstract boolean isSpectator();


    @Shadow @Final public PlayerInteractionManager gameMode;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.isCreative() || this.isSpectator()) return;
        if (this.hasEffect(ModernInhibited.INHIBITED.get()) && this.gameMode.isSurvival()) {
            this.setGameMode(GameType.ADVENTURE);
        } else if (!this.hasEffect(ModernInhibited.INHIBITED.get()) && this.gameMode.getGameModeForPlayer().equals(GameType.ADVENTURE)) {
            this.setGameMode(GameType.SURVIVAL);
        }
    }
}