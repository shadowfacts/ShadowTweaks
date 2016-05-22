package net.shadowfacts.shadowtweaks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.shadowfacts.shadowlib.version.Version;
import net.shadowfacts.shadowlib.version.VersionMatcher;
import net.shadowfacts.shadowtweaks.feature.Feature;
import net.shadowfacts.shadowtweaks.feature.bedrock.FeatureFlatBedrock;
import net.shadowfacts.shadowtweaks.feature.crops.FeatureCrops;
import net.shadowfacts.shadowtweaks.feature.dev.FeatureDevTools;
import net.shadowfacts.shadowtweaks.feature.recipe.FeatureExtraRecipes;
import net.shadowfacts.shadowtweaks.feature.screenshot.FeatureScreenshot;
import net.shadowfacts.shadowtweaks.feature.sign.FeatureSign;
import net.shadowfacts.shadowtweaks.feature.splash.FeatureSplashMessages;
import net.shadowfacts.shadowtweaks.feature.tools.FeatureTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shadowfacts
 */
@Mod(modid = ShadowTweaks.modId, name = ShadowTweaks.name, version = ShadowTweaks.version, acceptedMinecraftVersions = "[1.9]", dependencies = "required-after:shadowmc@[3.1.0,);required-after:Forge@[12.16.1.1897,);")
public final class ShadowTweaks {

	public static final String modId = "ShadowTweaks";
	public static final String name = "Shadow Tweaks";
	public static final String version = "1.8.0";

	public static Logger log = LogManager.getLogger(modId);

	private Configuration config;
	private List<Feature> features = new ArrayList<>();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(new File(event.getModConfigurationDirectory(), "shadowfacts/ShadowTweaks.cfg"));

		features.add(new FeatureFlatBedrock());
		features.add(new FeatureCrops());
		features.add(new FeatureDevTools());
		features.add(new FeatureExtraRecipes());
		features.add(new FeatureScreenshot());
		features.add(new FeatureSign());
		features.add(new FeatureTools());
		features.add(new FeatureSplashMessages());

		features.forEach(feature -> feature.configure(config));

		config.save();

		features.stream()
				.filter(Feature::isEnabled)
				.forEach(Feature::preInit);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		features.stream()
				.filter(Feature::isEnabled)
				.forEach(Feature::init);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		features.stream()
				.filter(Feature::isEnabled)
				.forEach(Feature::postInit);
	}

	@NetworkCheckHandler
	public boolean networkCheckHandler(Map<String, String> versions, Side side) {
		if (side == Side.CLIENT) {
			return true;
		} else {
			if (requiresServerSide()) {
				return VersionMatcher.matches("1.8.*", new Version(versions.get(modId)));
			}
		}

		return true;
	}

	private boolean requiresServerSide() {
		return features.stream()
				.filter(Feature::requiresServerSide)
				.findFirst()
				.isPresent();
	}

}
