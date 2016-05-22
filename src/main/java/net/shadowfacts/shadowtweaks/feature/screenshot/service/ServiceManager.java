package net.shadowfacts.shadowtweaks.feature.screenshot.service;

import lombok.Getter;
import net.minecraftforge.common.config.Configuration;
import net.shadowfacts.shadowtweaks.feature.screenshot.service.imgur.ImgurService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class ServiceManager {

	@Getter
	private static Map<String, Service> services = new HashMap<>();

	static {
		register(new ImgurService());
	}

	private static void register(Service service) {
		services.put(service.getName(), service);
	}

	public static void loadConfiguration(Configuration config, String category) {
		services.entrySet().stream()
				.map(Map.Entry::getValue)
				.forEach(service -> service.configure(config, category + "." + service.getName()));
	}

}
