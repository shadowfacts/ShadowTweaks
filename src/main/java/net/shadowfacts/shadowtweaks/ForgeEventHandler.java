package net.shadowfacts.shadowtweaks;

import cpw.mods.fml.client.GuiIngameModOptions;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.event.GuiOpenEvent;
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
