package net.shadowfacts.shadowtweaks.feature.screenshot;

import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowtweaks.feature.Feature;
import net.shadowfacts.shadowtweaks.feature.screenshot.service.Service;
import net.shadowfacts.shadowtweaks.feature.screenshot.service.ServiceManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * @author shadowfacts
 */
public class FeatureScreenshot extends Feature {

	private String screenshotDir;
	private String activeService;

	@Override
	public void configure(Configuration config) {
		super.configure(config);

		screenshotDir = config.getString("screenshotDir", getFeatureName(), "", "Change the Minecraft screenshot directory.\nLeave empty for MC default");
		activeService = config.getString("activeService", getFeatureName(), "", "The service to use for uploading screenshots.\nLeave empty for none");

		ServiceManager.loadConfiguration(config, getFeatureName());
	}

	@Override
	public boolean requiresServerSide() {
		return false;
	}

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onScreenshot(ScreenshotEvent event) throws IOException {
		if (!screenshotDir.isEmpty()) {
			File screenshotsDir = new File(screenshotDir);
			if (!screenshotsDir.exists()) screenshotsDir.mkdirs();
			event.setScreenshotFile(new File(screenshotsDir, event.getScreenshotFile().getName()));

			File temp = File.createTempFile("ShadowTweaksScreenshot", null);
			temp.deleteOnExit();

			ImageIO.write(event.getImage(), "png", temp);

			if (activeService != null && !activeService.isEmpty()) {
				Service service = ServiceManager.getServices().get(activeService);
				if (service != null) {
					service.accept(temp);
				}
			}
		}
	}

}
