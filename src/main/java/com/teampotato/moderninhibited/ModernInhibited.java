package com.teampotato.moderninhibited;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class ModernInhibited implements ModInitializer {
	public static final StatusEffect inhibited = new InhibitedEffect();

	@Override
	public void onInitialize() {
		ForgeConfigRegistry.INSTANCE.register("moderninhibited", ModConfig.Type.COMMON, config);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("moderninhibited", "inhibited"), inhibited);
	}

	public static final ForgeConfigSpec config;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> validStructures;
	public static final ForgeConfigSpec.BooleanValue showIcon, shouldParticle;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("ModernInhibited");
		validStructures = builder.defineList("validStructures", new ObjectArrayList<>(), o -> true);
		showIcon = builder.define("showEffectIcon", false);
		shouldParticle = builder.define("shouldEffectParticle", false);
		builder.pop();
		config = builder.build();
	}
}