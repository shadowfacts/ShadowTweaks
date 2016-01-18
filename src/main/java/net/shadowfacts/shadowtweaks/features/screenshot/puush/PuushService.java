package net.shadowfacts.shadowtweaks.features.screenshot.puush;

import net.minecraftforge.common.config.Configuration;
import net.shadowfacts.shadowtweaks.features.screenshot.Service;

import java.io.File;

/**
 * @author shadowfacts
 */
public class PuushService implements Service {

	private String apiKey;
	private boolean copyLink;

	@Override
	public String getName() {
		return "puush";
	}

	@Override
	public void configure(Configuration config, String category) {
		apiKey = config.getString("apiKey", category, "", "The Puush API key.\nGo to http://puush.me/account/settings to find yours");
		copyLink = config.getBoolean("copyLink", category, true, "Automatically copy the resulting link to the clipboard");
	}

	@Override
	public void accept(File file) {
		((Runnable) () -> {
			System.out.println("test");
		}).run();
	}

}
