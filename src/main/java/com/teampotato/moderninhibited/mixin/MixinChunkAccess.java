package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.api.IChunkAccess;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ChunkAccess.class)
public abstract class MixinChunkAccess implements IChunkAccess {
    @Shadow @Final private Map<ConfiguredStructureFeature<?, ?>, LongSet> structuresRefences;

    @Override
    public Iterable<ConfiguredStructureFeature<?, ?>> modernInhibited$getAvailableFeatures() {
        return this.structuresRefences.keySet();
    }
}
