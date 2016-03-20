package net.shadowfacts.shadowtweaks.features;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * @author shadowfacts
 */
public class FlatBedrockFeature implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		Block filler = world.provider.getDimension() == -1 ? Blocks.netherrack : world.provider.getDimension() == 1 ? Blocks.end_stone : Blocks.stone;
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
