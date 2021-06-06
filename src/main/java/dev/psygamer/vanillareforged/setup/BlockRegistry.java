package dev.psygamer.vanillareforged.setup;

import dev.psygamer.vanillareforged.VanillaReforged;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockRegistry {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VanillaReforged.MODID);
	
	public static final RegistryObject<Block> SILVER_ORE = BlockRegistry.register("silver_ore", () ->
			new Block(AbstractBlock.Properties.of(Material.STONE)
					.strength(3, 10)
					.sound(SoundType.STONE)
			));
	public static final RegistryObject<Block> SILVER_BLOCK = BlockRegistry.register("silver_block", () ->
			new Block(AbstractBlock.Properties.of(Material.STONE)
					.strength(3, 10)
					.sound(SoundType.METAL)
			));
	
	
	public static void register() {
		BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	private static <T extends Block> RegistryObject<T> registerNoItem(final String name, final Supplier<T> supplier) {
		return BlockRegistry.BLOCKS.register(name, supplier);
	}
	
	private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<T> supplier) {
		final RegistryObject<T> block = BlockRegistry.registerNoItem(name, supplier);
		
		ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
		
		return block;
	}
}
