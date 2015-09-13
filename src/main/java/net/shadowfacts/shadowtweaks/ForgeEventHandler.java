package net.shadowfacts.shadowtweaks;

import cpw.mods.fml.client.GuiIngameModOptions;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.shadowfacts.shadowtweaks.client.gui.GuiModOptionsList;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Event handler for MinecraftForge events
 *
 * @author shadowfacts
 */
public class ForgeEventHandler {

	/**
	 * Place block on tool use
	 * Based on public domain code from Tinker's Construct
	 * {@see https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/tconstruct/library/tools/HarvestTool.java#L150}
	 * @param event
	 */
	@SubscribeEvent
	public void useItem(PlayerInteractEvent event) {
		if (STConfig.toolRightClickPlace &&
				event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			ItemStack heldStack = event.entityPlayer.getHeldItem();
			if (heldStack != null && heldStack.getItem() instanceof ItemTool) {
				boolean result = false;

				int hotbarSlot = event.entityPlayer.inventory.currentItem;
				int toPlaceSlot = hotbarSlot == 0 ? 8 : hotbarSlot + 1;
				ItemStack toPlaceStack = null;

				if (hotbarSlot < 8) {
					toPlaceStack = event.entityPlayer.inventory.getStackInSlot(toPlaceSlot);
					if (toPlaceStack != null) {
						Item item = toPlaceStack.getItem();
						if (item instanceof ItemBlock) {
							int x = event.x;
							int y = event.y;
							int z = event.z;
							switch (event.face) {
								case 0:
									--y;
									break;
								case 1:
									++y;
									break;
								case 2:
									--z;
									break;
								case 3:
									++z;
									break;
								case 4:
									--x;
									break;
								case 5:
									++x;
									break;
							}

							AxisAlignedBB blockBounds = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
							AxisAlignedBB playerBounds = event.entityPlayer.boundingBox;

							Block blockToPlace = ((ItemBlock) item).field_150939_a;

							if (blockToPlace.getMaterial().blocksMovement() && playerBounds.intersectsWith(blockBounds))
								return;

							int damage = toPlaceStack.getItemDamage();
							int count = toPlaceStack.stackSize;

							MovingObjectPosition mop = event.entityPlayer.rayTrace(16, 0);
							result = item.onItemUse(toPlaceStack, event.entityPlayer, event.world, event.x, event.y, event.z, event.face, (float)mop.hitVec.xCoord, (float)mop.hitVec.yCoord, (float)mop.hitVec.zCoord);

							if (event.entityPlayer.capabilities.isCreativeMode) {
								toPlaceStack.setItemDamage(damage);
								toPlaceStack.stackSize = count;
							}
							if (toPlaceStack.stackSize <= 0) {
								event.entityPlayer.inventory.setInventorySlotContents(toPlaceSlot, null);
							}
						}
					}
				}

				if (result) event.entityPlayer.swingItem();
			}
		}
	}

	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event) {
		if (event.gui instanceof GuiIngameModOptions && STConfig.redirectModOptions) {
			try {
				Field parentScreen = event.gui.getClass().getDeclaredField("parentScreen");
				parentScreen.setAccessible(true);
				GuiScreen parent = (GuiScreen) parentScreen.get(event.gui);

				event.gui = new GuiModOptionsList(parent);
			} catch (ReflectiveOperationException e) {
				ShadowTweaks.log.error("There was a problem replacing the in-game mod options gui");
				e.printStackTrace();
			}
		} else if (event.gui instanceof GuiMainMenu && !Arrays.equals(STConfig.splashMessages, new String[0])) {
			try {
				Boolean deobfuscated = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
				Field splashText = event.gui.getClass().getDeclaredField(deobfuscated ? "splashText" : "field_73975_c");
				splashText.setAccessible(true);

				int num = ThreadLocalRandom.current().nextInt(0, STConfig.splashMessages.length);
				splashText.set(event.gui, STConfig.splashMessages[num]);
			} catch (ReflectiveOperationException e) {
				ShadowTweaks.log.error("There was a problem trying to set the splash screen message");
				e.printStackTrace();
			}
		}
	}

}
