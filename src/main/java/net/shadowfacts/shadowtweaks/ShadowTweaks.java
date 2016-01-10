package net.shadowfacts.shadowtweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.shadowfacts.shadowlib.version.Version;
import net.shadowfacts.shadowlib.version.VersionMatcher;
import net.shadowfacts.shadowtweaks.client.gui.STGuiHandler;
import net.shadowfacts.shadowtweaks.features.bedrock.FlatBedrock;
import net.shadowfacts.shadowtweaks.proxy.CommonProxy;
import net.shadowfacts.shadowtweaks.recipe.STRecipes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;

/**
 * Main mod class
 *
 * @author shadowfacts
 */
@Mod(modid = ShadowTweaks.modId, name = ShadowTweaks.name, version = ShadowTweaks.versionString, guiFactory = ShadowTweaks.guiFactory, acceptedMinecraftVersions = "[1.8.8,1.8.9]")
public class ShadowTweaks {

	public static final String modId = "ShadowTweaks";
	public static final String name = "ShadowTweaks";
	public static final String versionString = "1.6.3";
	public static final Version version = new Version(versionString);
	public static final String proxyPrefix = "net.shadowfacts.shadowtweaks.proxy.";
	public static final String guiFactory = "net.shadowfacts.shadowtweaks.client.gui.STGuiFactory";

	public static Logger log = LogManager.getLogger(modId);

	@SidedProxy(serverSide = ShadowTweaks.proxyPrefix + "CommonProxy", clientSide = ShadowTweaks.proxyPrefix + "ClientProxy")
	public static CommonProxy proxy;

	@Mod.Instance(modId)
	public static ShadowTweaks instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		STConfig.init(event);

		registerForgeHandlers();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new STGuiHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		STRecipes.addRecipes();

		if (STConfig.flatBedrock) {
			GameRegistry.registerWorldGenerator(new FlatBedrock(), 10);
		}
	}

	@NetworkCheckHandler
	public boolean networkCheckHandler(Object instance, Map<String, String> versions, Side side) {
		if (side == Side.SERVER) {
			if (requiresServerSide()) {
				return VersionMatcher.matches("1.2.*", new Version(versions.get(modId)));
			}
		}

		return true;
	}

	private boolean requiresServerSide() {
		return STConfig.toolRightClickPlace || STConfig.addLogChestRecipe || !Arrays.equals(STConfig.removeEntities, new String[0]);
	}

	private void registerForgeHandlers() {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

}
