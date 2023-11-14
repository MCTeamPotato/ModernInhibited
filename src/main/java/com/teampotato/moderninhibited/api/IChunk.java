package com.teampotato.moderninhibited.api;


import net.minecraft.world.gen.feature.structure.Structure;

public interface IChunk {
    Iterable<Structure<?>> modernInhibited$getAvailableFeatures();
}
