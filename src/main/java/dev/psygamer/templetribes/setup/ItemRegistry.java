package dev.psygamer.templetribes.setup;

import dev.psygamer.templetribes.TempleTribes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TempleTribes.MODID);
	
	public static final RegistryObject<Item> SILVER_INGOT = ItemRegistry.ITEMS.register("silver_ingot", () -> new Item(new Item.Properties()
			.group(ItemGroup.MATERIALS)
	));
	
	public static void register() {
		ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
