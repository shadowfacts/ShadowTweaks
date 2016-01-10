package net.shadowfacts.shadowtweaks.features.bedrock;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * @author shadowfacts
 */
public class FlatBedrock implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		Block filler = world.provider.getDimensionId() == -1 ? Blocks.netherrack : world.provider.getDimensionId() == 1 ? Blocks.end_stone : Blocks.stone;
		for (int x = chunkX * 16; x < chunkX * 16 + 16; x++) {
			for (int z = chunkZ * 16; z < chunkZ * 16 + 16; z++) {
				world.setBlockState(new BlockPos(x, 0, z), Blocks.bedrock.getDefaultState());
				for (int y = 1; y < 5; y++) {
					world.setBlockState(new BlockPos(x, y, z), filler.getDefaultState(), 2);
				}
			}
		}
	}

}
