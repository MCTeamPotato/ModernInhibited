package com.teampotato.moderninhibited;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod(ModernInhibited.ID)
public class ModernInhibited {
    public static final String ID = "moderninhibited";
    public static final DeferredRegister<MobEffect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ID);

    @SuppressWarnings("unused") public static final RegistryObject<MobEffect> INHIBITED =  EFFECT_DEFERRED_REGISTER.register("inhibited", InhibitedEffect::new);

    public ModernInhibited() {
        EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);
    }

    public static final ForgeConfigSpec config;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> validStructures;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("ModernInhibited");
        validStructures = builder.defineList("validStructures", new ObjectArrayList<>(), o -> true);
        builder.pop();
        config = builder.build();
    }
}
