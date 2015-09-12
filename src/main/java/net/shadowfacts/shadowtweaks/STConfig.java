package net.shadowfacts.shadowtweaks;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.shadowmc.config.Config;
import net.shadowfacts.shadowmc.config.ConfigManager;
import net.shadowfacts.shadowmc.config.ConfigProperty;

/**
 * Configuration class
 *
 * @author shadowfacts
 */
@Config(name = "ShadowTweaks")
public class STConfig {

	public static final String FEATURES = "features";
	public static final String REDIRECT_MOD_OPTIONS = FEATURES + ".modoptions";

	@ConfigProperty(comment = "Place the block in the next slot when a tool is right clicked, akin to TiCon behavior", category = FEATURES)
	public static boolean toolRightClickPlace = true;

	@ConfigProperty(name = "enable", comment = "Enable this feature", category = REDIRECT_MOD_OPTIONS)
	public static boolean redirectModOptions = true;

	@ConfigProperty(comment = "Only show mods with config GUIs", category = REDIRECT_MOD_OPTIONS)
	public static boolean onlyConfigurableMods = true;

	public static void init(FMLPreInitializationEvent event) {
		ConfigManager.instance.configDirPath = event.getModConfigurationDirectory().getAbsolutePath();
		ConfigManager.instance.register(ShadowTweaks.modId, STConfig.class);
		ConfigManager.instance.load(ShadowTweaks.modId);
	}

}
