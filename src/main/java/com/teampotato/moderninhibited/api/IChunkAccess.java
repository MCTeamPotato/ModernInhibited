package com.teampotato.moderninhibited.api;

import net.minecraft.world.level.levelgen.structure.Structure;

public interface IChunkAccess {
    Iterable<Structure> modernInhibited$getAvailableFeatures();
}
