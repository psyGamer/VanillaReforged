package dev.psygamer.vanillareforged;

import dev.psygamer.vanillareforged.setup.BlockRegistry;
import dev.psygamer.vanillareforged.setup.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod(VanillaReforged.MODID)
public class VanillaReforged {
	
	public static final String MODID = "vanillareforged";
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public VanillaReforged() {
		BlockRegistry.register();
		ItemRegistry.register();
		
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onBiomeLoad);
		
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		VanillaReforged.LOGGER.info("HELLO FROM PREINIT");
		VanillaReforged.LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		VanillaReforged.LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
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
		Supplier<?> sup = null;
		
		for (final Supplier<StructureFeature<?, ?>> structure : event.getGeneration().getStructures()) {
			if (structure.get().field_236268_b_.getRegistryName().toString().equalsIgnoreCase("minecraft:desert_pyramid")) {
				sup = structure;
			}
		}
		
		if (sup != null) {
			event.getGeneration().getStructures().remove(sup);
		}
	}
}
