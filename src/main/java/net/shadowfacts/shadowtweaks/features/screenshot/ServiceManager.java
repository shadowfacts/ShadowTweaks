package net.shadowfacts.shadowtweaks.features.screenshot;

import net.minecraftforge.common.config.Configuration;
import net.shadowfacts.shadowtweaks.STConfig;
import net.shadowfacts.shadowtweaks.features.screenshot.imgur.ImgurService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class ServiceManager {

	private static Map<String, Service> services = new HashMap<>();

	static {
		register(new ImgurService());
	}

	private static void register(Service service) {
		services.put(service.getName(), service);
	}

	public static void loadConfiguration(Configuration config) {
		services.entrySet().stream()
				.map(Map.Entry::getValue)
				.forEach(service -> service.configure(config, STConfig.SCREENSHOT + ".services." + service.getName()));

		config.save();
	}

	public static Service getActiveService() {
		return services.get(STConfig.screenshotService);
	}
}
