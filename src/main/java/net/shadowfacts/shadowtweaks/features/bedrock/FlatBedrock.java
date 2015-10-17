package net.shadowfacts.shadowtweaks.features.bedrock;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

/**
 * @author shadowfacts
 */
public class FlatBedrock implements IWorldGenerator {

	private static final String FLAT_BEDROCK = "ShadowTweaksFlatBedrock";

//	@SubscribeEvent
//	public void chunkLoad(ChunkDataEvent.Load event) {
//		if (event.getData() != null) {
//			if (!event.getData().getBoolean(FLAT_BEDROCK)) { // bedrock has not been replaced
//
//			}
//		}
//	}
//
//	@SubscribeEvent
//	public void chunkSave(ChunkDataEvent.Save event) {
//		event.getData().setBoolean(FLAT_BEDROCK, true);
//	}


	/* IWorldGenerator */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		Block filler = world.provider.dimensionId == -1 ? Blocks.netherrack : world.provider.dimensionId == 1 ? Blocks.end_stone : Blocks.stone;
		for (int x = chunkX * 16; x < chunkX * 16 + 16; x++) {
			for (int z = chunkZ * 16; z < chunkZ * 16 + 16; z++) {
				world.setBlock(x, 0, z, Blocks.bedrock, 0, 2);
				for (int y = 1; y < 5; y++) {
					world.setBlock(x, y, z, filler, 0, 2);
				}
			}
		}
	}
}
