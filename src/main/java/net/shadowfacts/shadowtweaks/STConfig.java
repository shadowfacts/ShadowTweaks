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

	private static final String FEATURES = "features";
	private static final String REDIRECT_MOD_OPTIONS = FEATURES + ".modoptions";
	private static final String MISC = "miscellaneous";
	private static final String DEV = "indev";

//	FEATURES
	@ConfigProperty(comment = "Place the block in the next slot when a tool is right clicked, akin to TiCon behavior", category = FEATURES)
	public static boolean toolRightClickPlace = true;

//	Mod optiosn GUI
	@ConfigProperty(name = "enable", comment = "Enable this feature", category = REDIRECT_MOD_OPTIONS)
	public static boolean redirectModOptions = true;

	@ConfigProperty(comment = "Only show mods with config GUIs", category = REDIRECT_MOD_OPTIONS)
	public static boolean onlyConfigurableMods = true;

//	MISC
	@ConfigProperty(comment = "Override the Vanilla splash screen message with a random one of these", category = MISC)
	public static String[] splashMessages = new String[0];

	@ConfigProperty(comment = "Remove entities with these names when they attempt to spawn.\nWARNING: This can be EXTREMELY dangerous.\nDO NOT USE THIS UNLESS YOUR ARE 100% SURE YOU KNOW WHAT YOU'RE DOING.\n", category = MISC)
	public static String[] removeEntities = new String[0];

	@ConfigProperty(comment = "Print the class name of the entity on /summon\nCreated for use in conjunction with removeEntities.\n", category = MISC)
	public static boolean printEntityClass = false;

//	DEV
	@ConfigProperty(comment = "Allow the player to sleep anytime if it's raining. WARNING: VERY BROKEN, do not enable", category = DEV)
	public static boolean sleepWhenRaining = false;


	
	public static void init(FMLPreInitializationEvent event) {
		ShadowTweaks.log.info("Loading config");
		ConfigManager.instance.configDirPath = event.getModConfigurationDirectory().getAbsolutePath();
		ConfigManager.instance.register(ShadowTweaks.modId, STConfig.class);
		ConfigManager.instance.load(ShadowTweaks.modId);
	}

}
