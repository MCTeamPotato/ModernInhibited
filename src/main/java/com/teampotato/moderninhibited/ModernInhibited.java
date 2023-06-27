package com.teampotato.moderninhibited;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ModernInhibited.ID)
public class ModernInhibited {
    public static final String ID = "moderninhibited";
    public static final DeferredRegister<Effect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, "cataclysm");

    @SuppressWarnings("unused") public static final RegistryObject<Effect> INHIBITED =  EFFECT_DEFERRED_REGISTER.register("inhibited", InhibitedEffect::new);

    public ModernInhibited() {
        EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onExpirePotion(PotionEvent.PotionExpiryEvent event) {
        EffectInstance effectInstance = event.getPotionEffect();
        if (effectInstance != null && effectInstance.getEffect().equals(INHIBITED.get()) &&
                event.getEntityLiving() instanceof PlayerEntity &&
                !((PlayerEntity) event.getEntityLiving()).isCreative() &&
                !event.getEntityLiving().isSpectator()
        ) {
            ((PlayerEntity) event.getEntityLiving()).setGameMode(GameType.SURVIVAL);
        }
    }
}
