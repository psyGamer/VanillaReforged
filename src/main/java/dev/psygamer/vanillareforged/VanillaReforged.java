package dev.psygamer.vanillareforged;

import com.mojang.serialization.Codec;
import dev.psygamer.vanillareforged.structures.ConfiguredStructures;
import dev.psygamer.vanillareforged.structures.Structures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mod(VanillaReforged.MODID)
public class VanillaReforged {
	
	public static final String MODID = "vanillareforged";
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public VanillaReforged() {
//		BlockRegistry.register();
//		ItemRegistry.register();
		
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
		
		Structures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
		
		// Register the setup method for modloading
		modEventBus.addListener(this::setup);
		
		// Register the enqueueIMC method for modloading
//		modEventBus.addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
//		modEventBus.addListener(this::processIMC);
		// Register the doClientStuff method for modloading
//		modEventBus.addListener(this::doClientStuff);
		
		forgeEventBus.addListener(EventPriority.HIGH, this::onBiomeLoad);
		forgeEventBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
		
		// Register ourselves for server and other game events we are interested in
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
//		VanillaReforged.LOGGER.info("HELLO FROM PREINIT");
//		VanillaReforged.LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
		
		event.enqueueWork(() -> {
			Structures.setupStructures();
			ConfiguredStructures.registerConfiguredStructures();
		});
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		VanillaReforged.LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
	}
	
	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("templetribes", "helloworld", () -> {
			VanillaReforged.LOGGER.info("Hello world from the MDK");
			return "Hello world";
		});
	}
	
	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		VanillaReforged.LOGGER.info("Got IMC {}", event.getIMCStream().
				map(m -> m.getMessageSupplier().get()).
				collect(Collectors.toList()));
	}
	
	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(final FMLServerStartingEvent event) {
		// do something when the server starts
		VanillaReforged.LOGGER.info("HELLO from server starting");
	}
	
	public void onBiomeLoad(final BiomeLoadingEvent event) {
//		event.getGeneration().getStructures().remove(Structure.DESERT_PYRAMID.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		/*Supplier<?> sup = null;
		
		for (final Supplier<StructureFeature<?, ?>> structure : event.getGeneration().getStructures()) {
			if (structure.get().feature.getRegistryName().toString().equalsIgnoreCase("minecraft:desert_pyramid")) {
				sup = structure;
			}
		}
		
		if (sup != null) {
			event.getGeneration().getStructures().remove(sup);
		}
		
		if (event.getName() != null && (
				event.getName().toString().equalsIgnoreCase("minecraft:desert") ||
						event.getName().toString().equalsIgnoreCase("minecraft:desert_hills")
		)) {
		}*/
		event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_DESERT_PYRAMID);
	}
	
	/**
	 * Will go into the world's chunkgenerator and manually add our structure spacing.
	 * If the spacing is not added, the structure doesn't spawn.
	 * <p>
	 * Use this for dimension blacklists for your structure.
	 * (Don't forget to attempt to remove your structure too from the map if you are blacklisting that dimension!)
	 * (It might have your structure in it already.)
	 * <p>
	 * Basically use this to make absolutely sure the chunkgenerator can or cannot spawn your structure.
	 */
	private static Method GETCODEC_METHOD;
	
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
			final ServerWorld serverWorld = (ServerWorld) event.getWorld();
			
			/*
			 * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
			 * They will handle your structure spacing for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
			 * This here is done with reflection as this tutorial is not about setting up and using Mixins.
			 * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
			 */
			try {
				if (VanillaReforged.GETCODEC_METHOD == null) {
					VanillaReforged.GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
				}
				final ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) VanillaReforged.GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
				if (cgRL != null && cgRL.getNamespace().equals("terraforged")) {
					return;
				}
			} catch (final Exception e) {
				VanillaReforged.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
			}
			
			/*
			 * Prevent spawning our structure in Vanilla's superflat world as
			 * people seem to want their superflat worlds free of modded structures.
			 * Also that vanilla superflat is really tricky and buggy to work with in my experience.
			 */
			if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
					serverWorld.dimension().equals(World.OVERWORLD)) {
				return;
			}
			
			/*
			 * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
			 * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
			 *
			 * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
			 * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
			 * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
			 */
			final Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.putIfAbsent(Structures.DESERT_PYRAMID.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.DESERT_PYRAMID.get()));
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}
}
