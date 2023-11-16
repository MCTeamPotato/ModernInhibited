package com.teampotato.moderninhibited.mixin;

import com.teampotato.moderninhibited.api.IServerPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements IServerPlayer {
    @Unique
    private int modernInhibited$tickCount;

    @Override
    public void modernInhibited$setTickCount(int tickCount) {
        this.modernInhibited$tickCount = tickCount;
    }

    @Override
    public int modernInhibited$getTickCount() {
        return this.modernInhibited$tickCount;
    }
}
