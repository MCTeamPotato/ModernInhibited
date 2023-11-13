package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.ModernInhibited;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
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

    @Inject(method = "tick", at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraftforge/event/ForgeEventFactory;onPlayerPreTick(Lnet/minecraft/world/entity/player/Player;)V", shift = At.Shift.AFTER))
    private void onTick(CallbackInfo ci) {
        mi$tickCount++;
        if (this.hasEffect(ModernInhibited.INHIBITED.get()) || this.isSpectator() || this.isCreative() || mi$tickCount % 40 != 0) return;
        BlockPos blockPosition = this.blockPosition();
        LevelChunk chunkAccess = this.level.getChunkAt(blockPosition);
        for (ConfiguredStructureFeature<?, ?> structure : chunkAccess.getAllReferences().keySet()) {
            if (this.level instanceof ServerLevel serverLevel) {
                StructureStart structureStart = serverLevel.structureFeatureManager().getStructureAt(blockPosition, structure);
                if (!structureStart.equals(StructureStart.INVALID_START)) {
                    ResourceLocation id = this.level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY).getKey(structure);
                    if (id != null && ModernInhibited.validStructures.get().contains(id.toString())) {
                        this.addEffect(new MobEffectInstance(ModernInhibited.INHIBITED.get(), 200, 0, false, ModernInhibited.showParticle.get(), ModernInhibited.showIcon.get()));
                        break;
                    }
                }
            }
        }
    }
}
