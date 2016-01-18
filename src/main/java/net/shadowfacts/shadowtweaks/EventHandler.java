package net.shadowfacts.shadowtweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowmc.config.ConfigManager;
import net.shadowfacts.shadowmc.event.ScreenShotEvent;
import net.shadowfacts.shadowmc.event.ToolUseEvent;
import net.shadowfacts.shadowtweaks.client.gui.STGuiHandler;
import net.shadowfacts.shadowtweaks.features.CropHarvestFeature;
import net.shadowfacts.shadowtweaks.features.SignFeature;
import net.shadowfacts.shadowtweaks.features.screenshot.services.ServiceManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Event handler for MinecraftForge events
 *
 * @author shadowfacts
 */
public class EventHandler {

	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event) {
		if (event.gui instanceof GuiMainMenu && !Arrays.equals(STConfig.splashMessages, new String[0])) {
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

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void checkSpawn(EntityEvent.EntityConstructing event) {
		String className = event.entity.getClass().getCanonicalName();
		if (className != null) {
			for (String s : STConfig.removeEntities) {
				if (s.equals(className)) {
					event.entity.setDead();
				}
			}
		}
	}

	@SubscribeEvent
	public void onCommand(CommandEvent event) {
		if (STConfig.printEntityClass && event.command instanceof CommandSummon) {
			if (event.parameters[0] != null) {
				Class clazz = (Class)EntityList.stringToClassMapping.get(event.parameters[0]);
				if (clazz != null) {
					System.out.println(clazz.getCanonicalName());
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onBlockActivated(PlayerInteractEvent event) {
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			IBlockState state = event.world.getBlockState(event.pos);
			if (state.getBlock() == Blocks.standing_sign || state.getBlock() == Blocks.wall_sign) {
				if (STConfig.editSigns || STConfig.clearSigns) {
					SignFeature.handleSignRightClicked(event.entityPlayer, event.world, event.pos, state);
					event.setCanceled(true);
				}
			} else if (CropHarvestFeature.canHandle(event.entityPlayer, event.world, event.pos, state)) {
				CropHarvestFeature.harvestCrop(event.entityPlayer, event.world, event.pos, state);
//				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void harvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
		if (STConfig.rightClickCrops && CropHarvestFeature.shouldRemoveDrop(event.state)) {
			CropHarvestFeature.removeDrop(event);
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(ShadowTweaks.modId)) {
			ConfigManager.instance.load(ShadowTweaks.modId);
			ServiceManager.loadConfiguration(ConfigManager.instance.getConfigurationObject(ShadowTweaks.modId));
		}
	}

	/**
	 * Place block on tool use
	 * Based on public domain code from Tinker's Construct
	 * {@see https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/tconstruct/library/tools/HarvestTool.java#L150}
	 */
	@SubscribeEvent
	public void onToolUse(ToolUseEvent event) {
		boolean used = false;
		if (STConfig.toolRightClickPlace) {
			int hotbarSlot = event.getPlayer().inventory.currentItem;
			int itemSlot = hotbarSlot >= 8 ? 0 : hotbarSlot + 1;

			ItemStack toPlaceStack = event.getPlayer().inventory.getStackInSlot(itemSlot);
			if (toPlaceStack != null) {
				Item item = toPlaceStack.getItem();
				if (item instanceof ItemBlock) {
					int posX = event.getBlockHit().getX();
					int posY = event.getBlockHit().getY();
					int posZ = event.getBlockHit().getZ();

					switch (event.getSide()) {
						case DOWN:
							posY--;
							break;
						case UP:
							posY++;
							break;
						case NORTH:
							posZ--;
							break;
						case SOUTH:
							posZ++;
							break;
						case WEST:
							posX--;
							break;
						case EAST:
							posX++;
							break;
					}

					AxisAlignedBB blockBounds =  AxisAlignedBB.fromBounds(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
					AxisAlignedBB playerBounds = event.getPlayer().getEntityBoundingBox();

					Block blockToPlace = ((ItemBlock)item).block;
					if (blockToPlace.getMaterial().blocksMovement() && playerBounds.intersectsWith(blockBounds)) {
						event.setCanceled(true);
						return;
					}

					int damage = toPlaceStack.getItemDamage();
					int count = toPlaceStack.stackSize;

					used = item.onItemUse(toPlaceStack, event.getPlayer(), event.getWorld(), event.getBlockHit(), event.getSide(), (float)event.getHit().xCoord, (float)event.getHit().yCoord, (float)event.getHit().zCoord);

					if (event.getPlayer().capabilities.isCreativeMode) {
						toPlaceStack.setItemDamage(damage);
						toPlaceStack.stackSize = count;
					}
					if (toPlaceStack.stackSize <= 0) {
						event.getPlayer().inventory.setInventorySlotContents(itemSlot, null);
					}
				}
			}
		}


		event.setCanceled(!used);
	}

	@SubscribeEvent
	public void onPreScreenshot(ScreenShotEvent.Pre event) {
		if (!STConfig.screenshotDir.isEmpty()) {
			File screenshotsDir = new File(STConfig.screenshotDir);
			if (!screenshotsDir.exists()) screenshotsDir.mkdirs();
			event.setScreenshotFile(new File(screenshotsDir, event.getScreenshotFile().getName()));
		}
	}

	@SubscribeEvent
	public void onPostScreenshot(ScreenShotEvent.Post event) {
		if (ServiceManager.getActiveService() != null) {
			ServiceManager.getActiveService().accept(event.getScreenshotFile());
		}
	}

}
