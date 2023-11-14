package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.api.IStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Structure.class)
public abstract class MixinStructure implements IStructure {
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
