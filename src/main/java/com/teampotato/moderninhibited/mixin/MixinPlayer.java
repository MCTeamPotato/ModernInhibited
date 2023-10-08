package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.ModernInhibited;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isSpectator();

    @Shadow public abstract boolean isCreative();

    @Unique
    private int mi$tickCount;

    @Inject(method = "tick", at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraft/entity/player/PlayerEntity;updateWaterSubmersionState()Z", shift = At.Shift.AFTER))
    private void onTick(CallbackInfo ci) {
        mi$tickCount++;
        if (this.hasStatusEffect(ModernInhibited.inhibited) || this.isSpectator() || this.isCreative() || mi$tickCount % 40 != 0) return;
        BlockPos blockPosition = this.getBlockPos();
        Chunk chunkAccess = this.getWorld().getChunk(blockPosition);
        for (Structure structure : chunkAccess.getStructureReferences().keySet()) {
            if (this.getWorld() instanceof ServerWorld serverLevel) {
                StructureStart structureStart = serverLevel.getStructureAccessor().getStructureAt(blockPosition, structure);
                if (!structureStart.equals(StructureStart.DEFAULT)) {
                    Identifier id = this.getWorld().getRegistryManager().get(RegistryKeys.STRUCTURE).getId(structure);
                    if (id != null && ModernInhibited.validStructures.get().contains(id.toString())) {
                        this.addStatusEffect(new StatusEffectInstance(ModernInhibited.inhibited, 200, 0, false, ModernInhibited.shouldParticle.get(), ModernInhibited.showIcon.get()));
                        break;
                    }
                }
            }
        }
    }
}