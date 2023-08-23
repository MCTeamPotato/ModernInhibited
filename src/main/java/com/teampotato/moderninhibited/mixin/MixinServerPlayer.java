package com.teampotato.moderninhibited.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.moderninhibited.ModernInhibited;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player {
    public MixinServerPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Shadow public abstract boolean isCreative();
    @Shadow public abstract boolean isSpectator();
    @Shadow public abstract boolean setGameMode(GameType pGameMode);
    @Shadow @Final public ServerPlayerGameMode gameMode;


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