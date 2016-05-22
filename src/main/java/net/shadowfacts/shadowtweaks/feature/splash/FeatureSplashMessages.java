package net.shadowfacts.shadowtweaks.feature.splash;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowtweaks.ShadowTweaks;
import net.shadowfacts.shadowtweaks.feature.Feature;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author shadowfacts
 */
public class FeatureSplashMessages extends Feature {

	private String[] splashMessages;

	@Override
	public void configure(Configuration config) {
		super.configure(config);

		splashMessages = config.getStringList("splashMessages", getFeatureName(), new String[0], "Override the Vanilla splash screen message with a random one of these");
	}

	@Override
	public boolean requiresServerSide() {
		return false;
	}

	@Override
	protected boolean isEnabledByDefault() {
		return false;
	}

	@Override
	public void preInit() {
		if (!Arrays.equals(splashMessages, new String[0])) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiMainMenu && !Arrays.equals(splashMessages, new String[0])) {
			try {
				Boolean deobfuscated = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
				Field splashText = event.getGui().getClass().getDeclaredField(deobfuscated ? "splashText" : "field_73975_c");
				splashText.setAccessible(true);

				int num = ThreadLocalRandom.current().nextInt(0, splashMessages.length);
				splashText.set(event.getGui(), splashMessages[num]);
			} catch (ReflectiveOperationException e) {
				ShadowTweaks.log.error("There was a problem trying to set the splash screen message");
				e.printStackTrace();
			}
		}
	}

}
