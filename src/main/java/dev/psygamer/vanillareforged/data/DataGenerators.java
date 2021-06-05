package dev.psygamer.vanillareforged.data;

import dev.psygamer.vanillareforged.VanillaReforged;
import dev.psygamer.vanillareforged.data.client.ModBlockStateProvider;
import dev.psygamer.vanillareforged.data.client.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = VanillaReforged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	
	@SubscribeEvent
	public static void gatherData(final GatherDataEvent event) {
		final DataGenerator generator = event.getGenerator();
		final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		
		generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
		generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
	}
}
