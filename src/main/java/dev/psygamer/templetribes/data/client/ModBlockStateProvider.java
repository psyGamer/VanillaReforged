package dev.psygamer.templetribes.data.client;

import dev.psygamer.templetribes.TempleTribes;
import dev.psygamer.templetribes.setup.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
	
	public ModBlockStateProvider(final DataGenerator gen, final ExistingFileHelper exFileHelper) {
		super(gen, TempleTribes.MODID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		simpleBlock(BlockRegistry.SILVER_BLOCK.get());
		simpleBlock(BlockRegistry.SILVER_ORE.get());
	}
}
