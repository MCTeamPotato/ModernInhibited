package com.teampotato.moderninhibited.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.moderninhibited.ModernInhibited;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer extends PlayerEntity {
    public MixinServerPlayer(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    @Shadow public abstract boolean isCreative();
    @Shadow public abstract boolean isSpectator();
    @Shadow @Final public ServerPlayerInteractionManager interactionManager;
    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.isCreative() || this.isSpectator()) return;
        if (this.hasStatusEffect(ModernInhibited.inhibited) && this.interactionManager.isSurvivalLike()) {
            this.changeGameMode(GameMode.ADVENTURE);
        }
    }
}