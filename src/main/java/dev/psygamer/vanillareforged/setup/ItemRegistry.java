package dev.psygamer.vanillareforged.setup;

import dev.psygamer.vanillareforged.VanillaReforged;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VanillaReforged.MODID);
	
	public static final RegistryObject<Item> SILVER_INGOT = ItemRegistry.ITEMS.register("silver_ingot", () -> new Item(new Item.Properties()
			.tab(ItemGroup.TAB_MATERIALS)
	));
	
	public static void register() {
		ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
