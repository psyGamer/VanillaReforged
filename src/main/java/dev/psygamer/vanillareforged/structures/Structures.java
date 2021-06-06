package dev.psygamer.vanillareforged.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.psygamer.vanillareforged.VanillaReforged;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class Structures {
	
	public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, VanillaReforged.MODID);
	
	public static final RegistryObject<Structure<NoFeatureConfig>> DESERT_PYRAMID = Structures.DEFERRED_REGISTRY_STRUCTURE.register(
			"desert_pyramid", () -> new DesertPyramidStructure(NoFeatureConfig.CODEC)
	);
	
	public static void setupStructures() {
		Structures.setupMapSpacingAndLand(
				Structures.DESERT_PYRAMID.get(),
				new StructureSeparationSettings(10, 5, 923871401),
				true
		);
	}
	
	public static <F extends Structure<?>> void setupMapSpacingAndLand(final F structure, final StructureSeparationSettings structureSeparationSettings, final boolean transformSurroundingLand) {
		Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
		
		if (transformSurroundingLand) {
			Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
					.addAll(Structure.NOISE_AFFECTING_FEATURES)
					.add(structure)
					.build();
		}
		
		DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
				.putAll(DimensionStructuresSettings.DEFAULTS)
				.put(structure, structureSeparationSettings)
				.build();
		
		WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			final Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();
			
			if (structureMap instanceof ImmutableMap) {
				final Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
				
				tempMap.put(structure, structureSeparationSettings);
				
				settings.getValue().structureSettings().structureConfig = tempMap;
			} else {
				structureMap.put(structure, structureSeparationSettings);
			}
		});
	}
}
