package dev.psygamer.vanillareforged.data.client;

import dev.psygamer.vanillareforged.VanillaReforged;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
	
	public ModItemModelProvider(final DataGenerator generator, final ExistingFileHelper existingFileHelper) {
		super(generator, VanillaReforged.MODID, existingFileHelper);
	}
	
	@Override
	protected void registerModels() {
		withExistingParent("silver_block", modLoc("block/silver_block"));
		withExistingParent("silver_ore", modLoc("block/silver_block"));
		
		final ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
		
		builder(itemGenerated, "silver_ingot");
	}
	
	private void builder(final ModelFile itemGenerated, final String name) {
		getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
	}
}
