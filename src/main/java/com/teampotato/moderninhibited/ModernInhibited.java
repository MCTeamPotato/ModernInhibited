package com.teampotato.moderninhibited;

import com.teampotato.moderninhibited.api.IChunk;
import com.teampotato.moderninhibited.api.IStructure;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod(ModernInhibited.ID)
public class ModernInhibited {
    public static final String ID = "moderninhibited";
    public static final DeferredRegister<Effect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, ID);
    public static final RegistryObject<Effect> INHIBITED;
    static {
        INHIBITED = EFFECT_DEFERRED_REGISTER.register("inhibited", () -> InhibitedEffect.INSTANCE);
    }
    private static int tickCount;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) event.player;
            ServerWorld serverLevel = serverPlayer.getLevel();
            boolean hasInhibited = serverPlayer.hasEffect(InhibitedEffect.INSTANCE);
            if (hasInhibited && serverPlayer.gameMode.getGameModeForPlayer().equals(GameType.SURVIVAL)) serverPlayer.setGameMode(GameType.ADVENTURE);
            tickCount++;
            if (hasInhibited || serverPlayer.isSpectator() || serverPlayer.isCreative() || tickCount % 30 != 0) return;
            tickCount = 0;
            BlockPos blockPosition = serverPlayer.blockPosition();
            Chunk chunkAccess = serverLevel.getChunkAt(blockPosition);
            for (Structure<?> structure : ((IChunk)chunkAccess).modernInhibited$getAvailableFeatures()) {
                if (!serverLevel.structureFeatureManager().getStructureAt(blockPosition, true, structure).equals(StructureStart.INVALID_START) && ((IStructure)structure).modernInhibited$shouldBeEffectedByInhibited()) {
                    serverPlayer.addEffect(new EffectInstance(InhibitedEffect.INSTANCE, 200, 0, false, showParticle.get(), showIcon.get()));
                    serverPlayer.setGameMode(GameType.ADVENTURE);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPotionExpired(PotionEvent.PotionExpiryEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getEffect().equals(InhibitedEffect.INSTANCE) && event.getEntity() instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity)event.getEntity()).setGameMode(GameType.SURVIVAL);
            tickCount = 29;
        }
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

    public ModernInhibited() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EFFECT_DEFERRED_REGISTER.register(bus);
        bus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() ->
                ForgeRegistries.STRUCTURE_FEATURES.forEach(structureFeature -> {
                    ResourceLocation id = structureFeature.getRegistryName();
                    if (id != null) {
                        ((IStructure) structureFeature).modernInhibited$setShouldBeEffectedByInhibited(validStructures.get().contains(id.toString()));
                    }
                })));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
