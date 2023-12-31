package com.teampotato.moderninhibited;

import com.teampotato.moderninhibited.api.IChunkAccess;
import com.teampotato.moderninhibited.api.IServerPlayer;
import com.teampotato.moderninhibited.api.IStructure;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
    private static boolean initialized;

    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!initialized && event.getEntity().level instanceof ServerLevel serverLevel) {
            initialized = true;
            Registry<Structure> registry = serverLevel.getServer().registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
            registry.forEach(configuredStructureFeature -> {
                ResourceLocation id = registry.getKey(configuredStructureFeature);
                if (id != null) {
                    ((IStructure) configuredStructureFeature).modernInhibited$setShouldBeEffectedByInhibited(validStructures.get().contains(id.toString()));
                }
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player instanceof ServerPlayer serverPlayer && initialized) {
            ServerLevel serverLevel = serverPlayer.getLevel();
            boolean hasInhibited = serverPlayer.hasEffect(INHIBITED.get());
            if (hasInhibited && serverPlayer.gameMode.getGameModeForPlayer().equals(GameType.SURVIVAL)) serverPlayer.setGameMode(GameType.ADVENTURE);
            ((IServerPlayer)serverPlayer).modernInhibited$setTickCount(((IServerPlayer) serverPlayer).modernInhibited$getTickCount() + 1);
            if (hasInhibited || serverPlayer.isSpectator() || serverPlayer.isCreative() || ((IServerPlayer) serverPlayer).modernInhibited$getTickCount() % 30 != 0) return;
            ((IServerPlayer) serverPlayer).modernInhibited$setTickCount(0);
            BlockPos blockPosition = serverPlayer.blockPosition();
            LevelChunk chunkAccess = serverLevel.getChunkAt(blockPosition);
            for (Structure structure : ((IChunkAccess)chunkAccess).modernInhibited$getAvailableFeatures()) {
                if (!serverLevel.structureManager().getStructureAt(blockPosition, structure).equals(StructureStart.INVALID_START) && ((IStructure)structure).modernInhibited$shouldBeEffectedByInhibited()) {
                    serverPlayer.addEffect(new MobEffectInstance(INHIBITED.get(), 200, 0, false, showParticle.get(), showIcon.get()));
                    serverPlayer.setGameMode(GameType.ADVENTURE);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPotionExpired(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && event.getEffectInstance().getEffect().equals(INHIBITED.get()) && event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.setGameMode(GameType.SURVIVAL);
            ((IServerPlayer) serverPlayer).modernInhibited$setTickCount(29);
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
        EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
