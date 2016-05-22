package net.shadowfacts.shadowtweaks.feature.crops;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.shadowfacts.shadowtweaks.feature.Feature;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class FeatureCrops extends Feature {

	private static Map<Block, CropHarvestInfo> plants = new HashMap<>();

	private static Block currentPlant;

	static {
		plants.put(Blocks.WHEAT, new CropHarvestInfo(Items.WHEAT_SEEDS, 7, 0));
		plants.put(Blocks.CARROTS, new CropHarvestInfo(Items.CARROT, 7, 0));
		plants.put(Blocks.POTATOES, new CropHarvestInfo(Items.POTATO, 7, 0));
		plants.put(Blocks.NETHER_WART, new CropHarvestInfo(Items.NETHER_WART, 7, 0));
	}

	@Override
	public boolean requiresServerSide() {
		return enabled;
	}

	@Override
	public void preInit() {
		if (enabled) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		if (canHandle(event.getEntityPlayer(), event.getWorld(), event.getPos(), state)) {
			harvestCrop(event.getEntityPlayer(), event.getWorld(), event.getPos(), state);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void harvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
		if (shouldRemoveDrop(event.getState())) {
			removeDrop(event);
		}
	}

	private static boolean canHandle(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
		if (player.getHeldItem(EnumHand.MAIN_HAND) == null || !player.isSneaking()) {
			CropHarvestInfo info = plants.get(state.getBlock());
			if (info != null) {
				return state.getBlock().getMetaFromState(state) == info.beforeMeta;
			}
		}

		return false;
	}

	private static void harvestCrop(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			player.swingArm(EnumHand.MAIN_HAND);
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

		for (int i = 0; i < event.getDrops().size(); i++) {
			ItemStack stack = event.getDrops().get(i);

			if (info.seed.getItem() == stack.getItem() && (info.seed.getItemDamage() == OreDictionary.WILDCARD_VALUE || info.seed.getItemDamage() == stack.getItemDamage())) {
				event.getDrops().remove(i);
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
			this(new ItemStack(seed, 1, OreDictionary.WILDCARD_VALUE), beforeMeta, afterMeta);
		}

	}


}
