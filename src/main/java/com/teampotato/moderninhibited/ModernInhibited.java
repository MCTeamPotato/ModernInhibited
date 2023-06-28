package com.teampotato.moderninhibited;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(ModernInhibited.ID)
public class ModernInhibited {
    public static final String ID = "moderninhibited";
    public static final DeferredRegister<MobEffect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ID);

    @SuppressWarnings("unused") public static final RegistryObject<MobEffect> INHIBITED =  EFFECT_DEFERRED_REGISTER.register("inhibited", InhibitedEffect::new);

    public ModernInhibited() {
        EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
