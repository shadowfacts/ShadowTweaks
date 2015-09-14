package net.shadowfacts.shadowtweaks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class MiscHandlers {

	/**
	 * Place block on tool use
	 * Based on public domain code from Tinker's Construct
	 * {@see https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/tconstruct/library/tools/HarvestTool.java#L150}
	 */
	public static boolean onToolUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		boolean used = false;

		if  (STConfig.toolRightClickPlace) {
			int hotbarSlot = player.inventory.currentItem;
			int itemSlot = hotbarSlot >= 8 ? 0 : hotbarSlot + 1;

			ItemStack toPlaceStack = player.inventory.getStackInSlot(itemSlot);
			if (toPlaceStack != null) {
				Item item = toPlaceStack.getItem();
				if (item instanceof ItemBlock) {
					int posX = x;
					int posY = y;
					int posZ = z;

					switch (side) {
						case 0:
							--posY;
							break;
						case 1:
							++posY;
							break;
						case 2:
							--posZ;
							break;
						case 3:
							++posZ;
							break;
						case 4:
							--posX;
							break;
						case 5:
							++posX;
							break;
					}

					AxisAlignedBB blockBounds = AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
					AxisAlignedBB playerBounds = player.boundingBox;

					Block blockToPlace = ((ItemBlock) item).field_150939_a;
					if (blockToPlace.getMaterial().blocksMovement() && playerBounds.intersectsWith(blockBounds))
						return false;

					int damage = toPlaceStack.getItemDamage();
					int count = toPlaceStack.stackSize;

					used = item.onItemUse(toPlaceStack, player, world, x, y, z, side, hitX, hitY, hitZ);

					if (player.capabilities.isCreativeMode) {
						toPlaceStack.setItemDamage(damage);
						toPlaceStack.stackSize = count;
					}
					if (toPlaceStack.stackSize <= 0) {
						player.inventory.setInventorySlotContents(itemSlot, null);
					}
				}
			}
		}

		return used;
	}

}
