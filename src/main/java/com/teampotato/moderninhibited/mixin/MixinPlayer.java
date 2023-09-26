package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.ModernInhibited;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    @Shadow public abstract boolean isSpectator();

    @Shadow public abstract boolean isCreative();

    @Unique
    private int mi$tickCount;

    protected MixinPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", shift = At.Shift.AFTER, ordinal = 2))
    private void onTick(CallbackInfo ci) {
        mi$tickCount++;
        if (this.hasEffect(ModernInhibited.INHIBITED.get()) || this.isSpectator() || this.isCreative() || mi$tickCount % 40 != 0) return;
        BlockPos blockPosition = this.blockPosition();
        ChunkAccess chunkAccess = this.level().getChunkAt(blockPosition);
        chunkAccess.getAllReferences().keySet().forEach(structure -> {
            if (this.level() instanceof ServerLevel serverLevel) {
                StructureStart structureStart = serverLevel.structureManager().getStructureAt(blockPosition, structure);
                if (!structureStart.equals(StructureStart.INVALID_START)) {
                    ResourceLocation id = BuiltInRegistries.STRUCTURE_TYPE.getKey(structure.type());
                    if (id != null && ModernInhibited.validStructures.get().contains(id.toString())) {
                        this.addEffect(new MobEffectInstance(ModernInhibited.INHIBITED.get(), 200));
                    }
                }
            }
        });
    }
}
