package com.teampotato.moderninhibited.api;

import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public interface IChunkAccess {
    Iterable<ConfiguredStructureFeature<?, ?>> modernInhibited$getAvailableFeatures();
}
