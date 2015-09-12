package net.shadowfacts.shadowtweaks;

import cpw.mods.fml.client.GuiIngameModOptions;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.shadowfacts.shadowtweaks.client.gui.GuiModOptionsList;

import java.lang.reflect.Field;

/**
 * Event handler for MinecraftForge events
 *
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void useItem(PlayerInteractEvent event) {
		if (STConfig.toolRightClickPlace && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			ItemStack held = event.entityPlayer.getHeldItem();
			if (held != null && held.getItem() instanceof ItemTool) {
				Block block = event.world.getBlock(event.x, event.y, event.z);
				MovingObjectPosition mop = event.entityPlayer.rayTrace(16, 0);
				if (event.entityPlayer.isSneaking() || !block.onBlockActivated(event.world, event.x, event.y, event.z, event.entityPlayer, event.face, (float)mop.hitVec.xCoord, (float)mop.hitVec.yCoord, (float)mop.hitVec.zCoord)) {
					InventoryPlayer inv = event.entityPlayer.inventory;
					int slotToPlace = inv.currentItem + 1;
					slotToPlace = slotToPlace > 8 ? 0 : slotToPlace;

					ItemStack toPlace = inv.getStackInSlot(slotToPlace);

					if (toPlace != null && toPlace.getItem() instanceof ItemBlock) {
						Block blockToPlace = ((ItemBlock) toPlace.getItem()).field_150939_a;
						ForgeDirection dir = ForgeDirection.getOrientation(event.face);
						int x = event.x + dir.offsetX;
						int y = event.y + dir.offsetY;
						int z = event.z + dir.offsetZ;

						if (blockToPlace.canPlaceBlockOnSide(event.world, x, y, z, event.face)) {

							AxisAlignedBB blockBounds = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
							if (!event.entityPlayer.boundingBox.intersectsWith(blockBounds)) {

								((ItemBlock) toPlace.getItem()).placeBlockAt(toPlace, event.entityPlayer, event.world, x, y, z, event.face, 0, 0, 0, toPlace.getItemDamage());
								blockToPlace.onBlockAdded(event.world, x, y, z);
								Block.SoundType sound = blockToPlace.stepSound;
								event.world.playSoundEffect(x + .5f, y + .5f, z + .5f, sound.getBreakSound(), (sound.getVolume() + 1f) / 2f, sound.getPitch() * .8f);

								event.entityPlayer.swingItem();

								if (!event.entityPlayer.capabilities.isCreativeMode) {
									toPlace.stackSize--;
									if (toPlace.stackSize <= 0) inv.setInventorySlotContents(slotToPlace, null);
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event) {
		if (STConfig.redirectModOptions && event.gui instanceof GuiIngameModOptions) {
			try {
				Field parentScreen = event.gui.getClass().getDeclaredField("parentScreen");
				parentScreen.setAccessible(true);
				GuiScreen parent = (GuiScreen) parentScreen.get(event.gui);

				event.gui = new GuiModOptionsList(parent);
			} catch (ReflectiveOperationException e) {
				ShadowTweaks.log.error("There was a problem replacing the in-game mod options gui");
				e.printStackTrace();
			}
		} else if (!STConfig.splashMessageOverride.equals("") && event.gui instanceof GuiMainMenu) {
			try {
				Field splashText = event.gui.getClass().getDeclaredField("splashText");
				splashText.setAccessible(true);
				splashText.set(event.gui, STConfig.splashMessageOverride);
			} catch (ReflectiveOperationException e) {
				ShadowTweaks.log.error("There was a problem trying to set the splash screen message");
				e.printStackTrace();
			}
		}
	}

}
