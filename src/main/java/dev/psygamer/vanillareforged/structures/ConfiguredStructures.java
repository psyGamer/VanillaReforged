package dev.psygamer.vanillareforged.structures;

import dev.psygamer.vanillareforged.VanillaReforged;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ConfiguredStructures {
	
	public static StructureFeature<?, ?> CONFIGURED_DESERT_PYRAMID = Structures.DESERT_PYRAMID.get().configured(IFeatureConfig.NONE);
	
	public static void registerConfiguredStructures() {
		final Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
		
		Registry.register(registry, new ResourceLocation(VanillaReforged.MODID, "configured_desert_pyramid"), ConfiguredStructures.CONFIGURED_DESERT_PYRAMID);
		
		FlatGenerationSettings.STRUCTURE_FEATURES.put(Structures.DESERT_PYRAMID.get(), ConfiguredStructures.CONFIGURED_DESERT_PYRAMID);
	}
}
