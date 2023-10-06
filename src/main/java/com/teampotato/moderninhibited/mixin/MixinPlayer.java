package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.ModernInhibited;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayer extends LivingEntity {
    @Shadow public abstract boolean isCreative();

    protected MixinPlayer(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }

    @Unique
    private int mi$tickCount;

    @Inject(method = "tick", at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraftforge/fml/hooks/BasicEventHooks;onPlayerPreTick(Lnet/minecraft/entity/player/PlayerEntity;)V", shift = At.Shift.AFTER))
    private void onTick(CallbackInfo ci) {
        mi$tickCount++;
        if (this.hasEffect(ModernInhibited.INHIBITED.get()) || this.isSpectator() || this.isCreative() || mi$tickCount % 40 != 0) return;
        BlockPos blockPosition = this.blockPosition();
        Chunk chunkAccess = this.level.getChunkAt(blockPosition);
        for (Structure<?> structure : chunkAccess.getAllReferences().keySet()) {
            if (this.level instanceof ServerWorld) {
                StructureStart<?> structureStart = ((ServerWorld)this.level).structureFeatureManager().getStructureAt(blockPosition, true, structure);
                if (!structureStart.equals(StructureStart.INVALID_START)) {
                    ResourceLocation id = structure.getRegistryName();
                    if (id != null && ModernInhibited.validStructures.get().contains(id.toString())) {
                        this.addEffect(new EffectInstance(ModernInhibited.INHIBITED.get(), 200));
                        break;
                    }
                }
            }
        }
    }
}
