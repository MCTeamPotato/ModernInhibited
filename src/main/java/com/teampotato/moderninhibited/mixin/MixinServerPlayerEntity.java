package com.teampotato.moderninhibited.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.moderninhibited.ModernInhibited;
import com.teampotato.moderninhibited.api.IChunk;
import com.teampotato.moderninhibited.api.IServerPlayerEntity;
import com.teampotato.moderninhibited.api.IStructure;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IServerPlayerEntity {
    @Shadow public abstract ServerWorld getServerWorld();

    @Shadow @Final public ServerPlayerInteractionManager interactionManager;

    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

    @Shadow public abstract boolean isSpectator();

    @Shadow public abstract boolean isCreative();

    public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Unique
    private int modernInhibited$tickCount;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (ModernInhibited.initialized) {
            ServerWorld serverWorld = this.getServerWorld();
            boolean hasInhibited = this.hasStatusEffect(ModernInhibited.INHIBITED);
            if (hasInhibited && this.interactionManager.getGameMode().equals(GameMode.SURVIVAL)) this.changeGameMode(GameMode.ADVENTURE);
            this.modernInhibited$setTickCount(this.modernInhibited$getTickCount() + 1);
            if (hasInhibited || this.isSpectator() || this.isCreative() || this.modernInhibited$getTickCount() % 30 != 0) return;
            this.modernInhibited$setTickCount(0);
            BlockPos blockPos = this.getBlockPos();
            Chunk chunk = serverWorld.getChunk(blockPos);
            for (Structure structure : ((IChunk)chunk).modernInhibited$getAvailableFeatures()) {
                if (!serverWorld.getStructureAccessor().getStructureAt(blockPos, structure).equals(StructureStart.DEFAULT) && ((IStructure)structure).modernInhibited$shouldBeEffectedByInhibited()) {
                    this.addStatusEffect(new StatusEffectInstance(ModernInhibited.INHIBITED, 200, 0, false, ModernInhibited.SHOULD_PARTICLE.get(), ModernInhibited.SHOW_ICON.get()));
                    this.changeGameMode(GameMode.ADVENTURE);
                    break;
                }
            }
        }
    }

    @Override
    public void modernInhibited$setTickCount(int tickCount) {
        this.modernInhibited$tickCount = tickCount;
    }

    @Override
    public int modernInhibited$getTickCount() {
        return this.modernInhibited$tickCount;
    }
}
