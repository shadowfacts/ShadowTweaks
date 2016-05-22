package net.shadowfacts.shadowtweaks.feature.bedrock;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.shadowtweaks.feature.Feature;

import java.util.Random;

/**
 * @author shadowfacts
 */
public class FeatureFlatBedrock extends Feature implements IWorldGenerator {

	@Override
	public boolean requiresServerSide() {
		return true;
	}

	@Override
	protected boolean isEnabledByDefault() {
		return false;
	}

	@Override
	public void preInit() {
		GameRegistry.registerWorldGenerator(this, 10);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		Block filler = world.provider.getDimension() == -1 ? Blocks.NETHERRACK : world.provider.getDimension() == 1 ? Blocks.END_STONE : Blocks.STONE;
		for (int x = chunkX * 16; x < chunkX * 16 + 16; x++) {
			for (int z = chunkZ * 16; z < chunkZ * 16 + 16; z++) {
				world.setBlockState(new BlockPos(x, 0, z), Blocks.BEDROCK.getDefaultState());
				for (int y = 1; y < 5; y++) {
					world.setBlockState(new BlockPos(x, y, z), filler.getDefaultState(), 2);
				}
			}
		}
	}

}
