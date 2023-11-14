package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.api.IConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ConfiguredStructureFeature.class)
public abstract class MixinConfiguredStructureFeature implements IConfiguredStructureFeature {
    @Unique
    private boolean modernInhibited$shouldBeEffectedByInhibited;

    @Override
    public boolean modernInhibited$shouldBeEffectedByInhibited() {
        return this.modernInhibited$shouldBeEffectedByInhibited;
    }

    @Override
    public void modernInhibited$setShouldBeEffectedByInhibited(boolean shouldBeEffectedByInhibited) {
        this.modernInhibited$shouldBeEffectedByInhibited = shouldBeEffectedByInhibited;
    }
}
