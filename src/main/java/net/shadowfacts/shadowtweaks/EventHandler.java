package net.shadowfacts.shadowtweaks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowmc.config.ConfigManager;
import net.shadowfacts.shadowtweaks.features.CropHarvestFeature;
import net.shadowfacts.shadowtweaks.features.screenshot.ServiceManager;

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
//		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
//			IBlockState state = event.world.getBlockState(event.pos);
//			if (state.getBlock() == Blocks.standing_sign || state.getBlock() == Blocks.wall_sign) {
//				if (STConfig.editSigns || STConfig.clearSigns) {
//					SignFeature.handleSignRightClicked(event.entityPlayer, event.world, event.pos, state);
//					event.setCanceled(true);
//				}
//			}else if (CropHarvestFeature.canHandle(event.entityPlayer, event.world, event.pos, state)) {
//				CropHarvestFeature.harvestCrop(event.entityPlayer, event.world, event.pos, state);
//				event.setCanceled(true);
//			}
//		}
//		TODO: enable this (pending https://github.com/MinecraftForge/MinecraftForge/issues/2591)
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

//	@SubscribeEvent
//	public void onToolUse(ToolUseEvent event) {
//		ToolPlaceFeature.onToolUse(event);
//	}
//	TODO: rewrite this ^

//	@SubscribeEvent
//	public void onPreScreenshot(ScreenShotEvent.Pre event) {
//		if (!STConfig.screenshotDir.isEmpty()) {
//			File screenshotsDir = new File(STConfig.screenshotDir);
//			if (!screenshotsDir.exists()) screenshotsDir.mkdirs();
//			event.setScreenshotFile(new File(screenshotsDir, event.getScreenshotFile().getName()));
//		}
//	}
//
//	@SubscribeEvent
//	public void onPostScreenshot(ScreenShotEvent.Post event) {
//		if (ServiceManager.getActiveService() != null) {
//			ServiceManager.getActiveService().accept(event.getScreenshotFile());
//		}
//	}
//	TODO: re-enable pending https://github.com/MinecraftForge/MinecraftForge/pull/2602

}
