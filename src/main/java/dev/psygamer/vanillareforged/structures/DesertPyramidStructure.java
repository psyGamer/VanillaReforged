package dev.psygamer.vanillareforged.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import dev.psygamer.vanillareforged.VanillaReforged;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

import java.util.List;

public class DesertPyramidStructure extends Structure<NoFeatureConfig> {
	
	public DesertPyramidStructure(final Codec<NoFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return DesertPyramidStructure.Start::new;
	}
	
	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
		return ImmutableList.of(
				new MobSpawnInfo.Spawners(EntityType.HUSK, 100, 9, 4)
		);
	}
	
	@Override
	protected boolean isFeatureChunk(final ChunkGenerator chunkGenerator, final BiomeProvider biomeSource, final long seed, final SharedSeedRandom chunkRandom, final int chunkX, final int chunkZ, final Biome biome, final ChunkPos chunkPos, final NoFeatureConfig featureConfig) {
		final BlockPos chunkCenter = new BlockPos(chunkX * 16 + 7, 0, chunkZ * 16 + 7);
		
		final int height = chunkGenerator.getFirstOccupiedHeight(chunkCenter.getX(), chunkCenter.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
		
		final IBlockReader blockColumn = chunkGenerator.getBaseColumn(chunkCenter.getX(), chunkCenter.getZ());
		final BlockState topBlock = blockColumn.getBlockState(chunkCenter.above(height));
		
		return topBlock.getFluidState().isEmpty();
	}
	
	public static final class Start extends StructureStart<NoFeatureConfig> {
		public Start(final Structure<NoFeatureConfig> structureIn, final int chunkX, final int chunkZ, final MutableBoundingBox mutableBoundingBox, final int referenceIn, final long seedIn) {
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
		}
		
		@Override
		public void generatePieces(final DynamicRegistries dynamicRegistryManager, final ChunkGenerator chunkGenerator, final TemplateManager templateManagerIn, final int chunkX, final int chunkZ, final Biome biomeIn, final NoFeatureConfig config) {
			final int posX = chunkX * 16 + 7;
			final int posZ = chunkZ * 16 + 7;
			
			final BlockPos pos = new BlockPos(posX, 0, posZ);
			
			JigsawManager.addPieces(
					dynamicRegistryManager,
					new VillageConfig(() ->
							dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
									.get(new ResourceLocation(VanillaReforged.MODID, "run_down_house/start_pool")), 10),
					AbstractVillagePiece::new,
					chunkGenerator,
					templateManagerIn,
					pos,
					this.pieces,
					this.random,
					false,
					true
			);
			
			VanillaReforged.LOGGER.log(Level.DEBUG, "Desert pyramid at " +
					this.pieces.get(0).getBoundingBox().x0 + " " +
					this.pieces.get(0).getBoundingBox().y0 + " " +
					this.pieces.get(0).getBoundingBox().z0);
		}
	}
}
