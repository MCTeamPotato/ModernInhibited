package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.api.IChunk;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Chunk.class)
public abstract class MixinChunk implements IChunk {

    @Shadow @Final private Map<Structure<?>, LongSet> structuresRefences;

    @Override
    public Iterable<Structure<?>> modernInhibited$getAvailableFeatures() {
        return this.structuresRefences.keySet();
    }
}
