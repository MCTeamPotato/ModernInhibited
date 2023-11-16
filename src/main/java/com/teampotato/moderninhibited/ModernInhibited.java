package com.teampotato.moderninhibited;

import com.teampotato.moderninhibited.api.IStructure;
import dev.architectury.event.events.common.PlayerEvent;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class ModernInhibited implements ModInitializer {
	public static final StatusEffect INHIBITED = new InhibitedEffect();

	public static boolean initialized;
	@Override
	public void onInitialize() {
		ForgeConfigRegistry.INSTANCE.register("moderninhibited", ModConfig.Type.COMMON, CONFIG);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("moderninhibited", "inhibited"), INHIBITED);
		PlayerEvent.PLAYER_JOIN.register(player -> {
			if (!initialized) {
				initialized = true;
				ServerWorld serverWorld = player.getServerWorld();
				Registry<Structure> registry = serverWorld.getServer().getRegistryManager().get(RegistryKeys.STRUCTURE);
				registry.forEach(structure -> {
					Identifier id = registry.getId(structure);
					if (id != null) {
						((IStructure)structure).modernInhibited$setShouldBeEffectedByInhibited(VALID_STRUCTURES.get().contains(id.toString()));
					}
				});
			}
		});
	}

	public static final ForgeConfigSpec CONFIG;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> VALID_STRUCTURES;
	public static final ForgeConfigSpec.BooleanValue SHOW_ICON, SHOULD_PARTICLE;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("ModernInhibited");
		VALID_STRUCTURES = builder.defineList("validStructures", new ObjectArrayList<>(), o -> true);
		SHOW_ICON = builder.define("showEffectIcon", false);
		SHOULD_PARTICLE = builder.define("shouldEffectParticle", false);
		builder.pop();
		CONFIG = builder.build();
	}
}