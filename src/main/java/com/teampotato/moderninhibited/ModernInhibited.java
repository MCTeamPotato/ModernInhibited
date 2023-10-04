package com.teampotato.moderninhibited;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod(ModernInhibited.ID)
public class ModernInhibited {
    public static final String ID = "moderninhibited";
    public static final DeferredRegister<MobEffect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ID);

    public static final RegistryObject<MobEffect> INHIBITED = EFFECT_DEFERRED_REGISTER.register("inhibited", InhibitedEffect::new);

    public ModernInhibited() {
        EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);
        MinecraftForge.EVENT_BUS.register(this);
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

    @SubscribeEvent
    public void onServerPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.isCreative() || serverPlayer.isSpectator()) return;
            if (serverPlayer.hasEffect(ModernInhibited.INHIBITED.get()) && serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL) {
                serverPlayer.setGameMode(GameType.ADVENTURE);
            }
        }
    }

    @SubscribeEvent
    public void onPotionExpired(MobEffectEvent.@NotNull Expired event) {
        if (event.getEffectInstance() != null && event.getEffectInstance().getEffect().equals(INHIBITED.get()) && event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.setGameMode(GameType.SURVIVAL);
        }
    }
}
