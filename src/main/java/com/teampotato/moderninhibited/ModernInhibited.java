package com.teampotato.moderninhibited;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
@Mod(ModernInhibited.ID)
public class ModernInhibited {
    public static final String ID = "moderninhibited";
    public static final DeferredRegister<Effect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, ID);

    @SuppressWarnings("unused") public static final RegistryObject<Effect> INHIBITED =  EFFECT_DEFERRED_REGISTER.register("inhibited", InhibitedEffect::new);

    public ModernInhibited() {
        EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
