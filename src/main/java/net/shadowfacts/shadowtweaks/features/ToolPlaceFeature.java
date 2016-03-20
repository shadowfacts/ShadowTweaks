package net.shadowfacts.shadowtweaks.features;


/**
 * @author shadowfacts
 */
public class ToolPlaceFeature {

	/**
	 * Place block on tool use
	 * Based on public domain code from Tinker's Construct
	 * {@see https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/tconstruct/library/tools/HarvestTool.java#L150}
	 */
//	TODO: REWRITE THIS
//	public static void onToolUse(ToolUseEvent event) {
//		boolean used = false;
//		if (STConfig.toolRightClickPlace) {
//			int hotbarSlot = event.getPlayer().inventory.currentItem;
//			int itemSlot = hotbarSlot >= 8 ? 0 : hotbarSlot + 1;
//
//			ItemStack toPlaceStack = event.getPlayer().inventory.getStackInSlot(itemSlot);
//			if (toPlaceStack != null) {
//				Item item = toPlaceStack.getItem();
//				if (item instanceof ItemBlock) {
//					int posX = event.getBlockHit().getX();
//					int posY = event.getBlockHit().getY();
//					int posZ = event.getBlockHit().getZ();
//
//					switch (event.getSide()) {
//						case DOWN:
//							posY--;
//							break;
//						case UP:
//							posY++;
//							break;
//						case NORTH:
//							posZ--;
//							break;
//						case SOUTH:
//							posZ++;
//							break;
//						case WEST:
//							posX--;
//							break;
//						case EAST:
//							posX++;
//							break;
//					}
//
//					AxisAlignedBB blockBounds =  AxisAlignedBB.fromBounds(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
//					AxisAlignedBB playerBounds = event.getPlayer().getEntityBoundingBox();
//
//					Block blockToPlace = ((ItemBlock)item).block;
//					if (blockToPlace.getMaterial().blocksMovement() && playerBounds.intersectsWith(blockBounds)) {
//						event.setCanceled(true);
//						return;
//					}
//
//					int damage = toPlaceStack.getItemDamage();
//					int count = toPlaceStack.stackSize;
//
//					used = item.onItemUse(toPlaceStack, event.getPlayer(), event.getWorld(), event.getBlockHit(), event.getSide(), (float)event.getHit().xCoord, (float)event.getHit().yCoord, (float)event.getHit().zCoord);
//
//					if (event.getPlayer().capabilities.isCreativeMode) {
//						toPlaceStack.setItemDamage(damage);
//						toPlaceStack.stackSize = count;
//					}
//					if (toPlaceStack.stackSize <= 0) {
//						event.getPlayer().inventory.setInventorySlotContents(itemSlot, null);
//					}
//				}
//			}
//		}
//
//
//		event.setCanceled(!used);
//	}

}
