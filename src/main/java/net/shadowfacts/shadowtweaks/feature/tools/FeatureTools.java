package net.shadowfacts.shadowtweaks.feature.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowtweaks.feature.Feature;

/**
 * @author shadowfacts
 */
public class FeatureTools extends Feature {

//	Config
	private boolean rightClickPlace;

	@Override
	public void configure(Configuration config) {
		super.configure(config);

		rightClickPlace = config.getBoolean("rightClickPlace", getFeatureName(), true, "Place the block in the next slot when a tool is right clicked, akin to TiCon behavior");
	}

	@Override
	public boolean requiresServerSide() {
		return enabled && rightClickPlace;
	}

	@Override
	public void preInit() {
		if (enabled && rightClickPlace) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@SubscribeEvent
	public void onItemUseEvent(PlayerInteractEvent.RightClickItem event) {
		if (rightClickPlace) {
			boolean used = false;

			ItemStack heldStack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);

			if (heldStack != null && heldStack.getItem() instanceof ItemTool) {

				int hotbarSlot = event.getEntityPlayer().inventory.currentItem;
				int itemSlot = hotbarSlot >= 8 ? 0 : hotbarSlot + 1;

				ItemStack toPlaceStack = event.getEntityPlayer().inventory.getStackInSlot(itemSlot);

				if (toPlaceStack != null && toPlaceStack.getItem() instanceof ItemBlock) {
					RayTraceResult rayTrace = rayTracePlayerLook(event.getEntityPlayer());

					if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {

						EnumFacing hitSide = rayTrace.sideHit;
						BlockPos pos = new BlockPos(rayTrace.hitVec);

						AxisAlignedBB blockBounds = new AxisAlignedBB(pos, pos.add(1, 1, 1));
						AxisAlignedBB playerBounds = event.getEntityPlayer().getEntityBoundingBox();

						Block blockToPlace = ((ItemBlock) toPlaceStack.getItem()).block;
						if (blockToPlace.getDefaultState().getMaterial().blocksMovement() && playerBounds.intersectsWith(blockBounds)) {
							event.setCanceled(true);
							return;
						}

						int damage = toPlaceStack.getItemDamage();
						int count = toPlaceStack.stackSize;

						EnumActionResult result = toPlaceStack.getItem().onItemUse(toPlaceStack, event.getEntityPlayer(), event.getWorld(), pos, EnumHand.MAIN_HAND, hitSide, (float) rayTrace.hitVec.xCoord, (float) rayTrace.hitVec.yCoord, (float) rayTrace.hitVec.zCoord);

						if (result == EnumActionResult.SUCCESS) {
							used = true;

							if (event.getEntityPlayer().capabilities.isCreativeMode) {
								toPlaceStack.setItemDamage(damage);
								toPlaceStack.stackSize = count;
							}
							if (toPlaceStack.stackSize <= 0) {
								event.getEntityPlayer().inventory.setInventorySlotContents(itemSlot, null);
							}
						}
					}
				}
			}

			if (used) {
				event.setCanceled(true);
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
			}
		}
	}

	private static RayTraceResult rayTracePlayerLook(EntityPlayer player) {
		double distance = player.capabilities.isCreativeMode ? 5f : 4.5f;
		Vec3d posVec = new Vec3d(player.posX, player.posY + player.eyeHeight, player.posZ);
		Vec3d lookVec = player.getLook(1);
		lookVec = posVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
		return player.worldObj.rayTraceBlocks(posVec, lookVec);
	}

}
