package com.teampotato.moderninhibited;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.world.GameType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod(ModernInhibited.ID)
public class ModernInhibited {
    public static final String ID = "moderninhibited";
    public static final DeferredRegister<Effect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, ID);

    public static final RegistryObject<Effect> INHIBITED =  EFFECT_DEFERRED_REGISTER.register("inhibited", InhibitedEffect::new);

    public ModernInhibited() {
        EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);
    }

    public static final ForgeConfigSpec config;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> validStructures;
    public static final ForgeConfigSpec.BooleanValue showIcon, showParticle;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("ModernInhibited");
        validStructures = builder.defineList("validStructures", new ObjectArrayList<>(), o -> true);
        showIcon = builder.define("showEffectIcon", false);
        showParticle = builder.define("showEffectParticle", false);
        builder.pop();
        config = builder.build();
    }

    @SubscribeEvent
    public void onPotionExpired(PotionEvent.@NotNull PotionExpiryEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getEffect().equals(INHIBITED.get()) && event.getEntity() instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity)event.getEntity()).setGameMode(GameType.SURVIVAL);
        }
    }
}
