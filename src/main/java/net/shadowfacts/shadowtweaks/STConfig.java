package net.shadowfacts.shadowtweaks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.shadowmc.config.Config;
import net.shadowfacts.shadowmc.config.ConfigManager;
import net.shadowfacts.shadowmc.config.ConfigProperty;
import net.shadowfacts.shadowtweaks.features.screenshot.services.ServiceManager;

/**
 * Configuration class
 *
 * @author shadowfacts
 */
@Config(name = "ShadowTweaks")
public class STConfig {

	public static final String FEATURES = "features";
	public static final String SCREENSHOT = FEATURES + ".screenshot";
	public static final String BEDROCK = FEATURES + ".flatbedrock";
	public static final String SIGNS = FEATURES + ".signs";
	public static final String MISC = "miscellaneous";

//	FEATURES
	@ConfigProperty(comment = "Place the block in the next slot when a tool is right clicked, akin to TiCon behavior", category = FEATURES)
	public static boolean toolRightClickPlace = true;

	@ConfigProperty(comment = "Add a recipe for 8 logs (in chest shape) for 4 chests", category = FEATURES)
	public static boolean addLogChestRecipe = true;

	@ConfigProperty(comment = "Allow right-clicking crops to harvest them", category = FEATURES)
	public static boolean rightClickCrops = true;

//	FEATURE: Screenshot
	@ConfigProperty(comment = "Change the Minecraft screenshot directory.\nLeave empty for MC default", category = SCREENSHOT)
	public static String screenshotDir = "";

	@ConfigProperty(comment = "The service to use for screenshots", category = SCREENSHOT)
	public static String screenshotService = "";

//	FEATURE: Flat bedrock
	@ConfigProperty(comment = "Generate flat bedrock in the world", category = BEDROCK)
	public static boolean flatBedrock = false;

//	FEATURE: Signs
	@ConfigProperty(comment = "Right-click to edit signs", category = SIGNS)
	public static boolean editSigns = true;

	@ConfigProperty(comment = "Shift+right-click to clear signs", category = SIGNS)
	public static boolean clearSigns = true;

//	MISC
	@ConfigProperty(comment = "Override the Vanilla splash screen message with a random one of these", category = MISC)
	public static String[] splashMessages = new String[0];

	@ConfigProperty(comment = "Remove entities with these names when they attempt to spawn.\nWARNING: This can be EXTREMELY dangerous.\nDO NOT USE THIS UNLESS YOUR ARE 100% SURE YOU KNOW WHAT YOU'RE DOING.\n", category = MISC)
	public static String[] removeEntities = new String[0];

	@ConfigProperty(comment = "Print the class name of the entity on /summon\nCreated for use in conjunction with removeEntities.\n", category = MISC)
	public static boolean printEntityClass = false;


	public static void init(FMLPreInitializationEvent event) {
		ShadowTweaks.log.info("Loading config");
		ConfigManager.instance.configDir = event.getModConfigurationDirectory();
		ConfigManager.instance.register(ShadowTweaks.modId, STConfig.class, ShadowTweaks.modId);
		ConfigManager.instance.load(ShadowTweaks.modId);

		Configuration config = ConfigManager.instance.getConfigurationObject(ShadowTweaks.modId);
		ServiceManager.loadConfiguration(config);
	}

}
