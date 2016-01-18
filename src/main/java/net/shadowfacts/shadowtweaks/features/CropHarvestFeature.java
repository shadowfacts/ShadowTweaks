package net.shadowfacts.shadowtweaks.features;

import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.shadowfacts.shadowtweaks.STConfig;

import java.util.*;

/**
 * @author shadowfacts
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CropHarvestFeature {

	private static Map<Block, CropHarvestInfo> plants = new HashMap<>();

	private static Block currentPlant;

	static {
		plants.put(Blocks.wheat, new CropHarvestInfo(Items.wheat_seeds, 7, 0));
		plants.put(Blocks.carrots, new CropHarvestInfo(Items.carrot, 7, 0));
		plants.put(Blocks.potatoes, new CropHarvestInfo(Items.potato, 7, 0));
		plants.put(Blocks.nether_wart, new CropHarvestInfo(Items.nether_wart, 3, 0));
	}

	public static boolean canHandle(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
		if (STConfig.rightClickCrops && (player.getHeldItem() == null || !player.isSneaking())) {
			CropHarvestInfo info = plants.get(state.getBlock());
			if (info != null) {
				return state.getBlock().getMetaFromState(state) == info.beforeMeta;
			}
		}
		return false;
	}

	public static void harvestCrop(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			player.swingItem();
		} else {
			currentPlant = state.getBlock();
			state.getBlock().dropBlockAsItem(world, pos, state, 0);
			world.setBlockState(pos, state.getBlock().getStateFromMeta(plants.get(state.getBlock()).afterMeta), 3);
		}

	}

	public static boolean shouldRemoveDrop(IBlockState state) {
		return state.getBlock() == currentPlant;
	}

	public static void removeDrop(BlockEvent.HarvestDropsEvent event) {
		CropHarvestInfo info = plants.get(currentPlant);

		for (int i = 0; i < event.drops.size(); i++) {
			ItemStack stack = event.drops.get(i);
			if (info.seed.getItem() == stack.getItem() && (info.seed.getItemDamage() == OreDictionary.WILDCARD_VALUE || info.seed.getItemDamage() == stack.getItemDamage())) {
				event.drops.remove(i);
				break;
			}
		}
		currentPlant = null;
	}

	@EqualsAndHashCode
	@AllArgsConstructor
	public static class CropHarvestInfo {
		private final ItemStack seed;
		private final int beforeMeta;
		private final int afterMeta;

		public CropHarvestInfo(Item seed, int beforeMeta, int afterMeta) {
			this(new ItemStack(seed, 1,OreDictionary.WILDCARD_VALUE), beforeMeta, afterMeta);
		}
	}

}
