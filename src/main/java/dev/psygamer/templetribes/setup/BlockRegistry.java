package dev.psygamer.templetribes.setup;

import dev.psygamer.templetribes.TempleTribes;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TempleTribes.MODID);
	
//	public static final RegistryObject<Block>
	
	public static void register() {
		BLOCKS.register( FMLJavaModLoadingContext.get().getModEventBus());
	}
}
