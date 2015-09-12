package net.shadowfacts.shadowtweaks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.shadowfacts.shadowtweaks.proxy.CommonProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main mod class
 *
 * @author shadowfacts
 */
@Mod(modid = ShadowTweaks.modId, name = ShadowTweaks.name, version = ShadowTweaks.version, guiFactory = ShadowTweaks.guiFactory)
public class ShadowTweaks {

	public static final String modId = "ShadowTweaks";
	public static final String name = "ShadowTweaks";
	public static final String version = "0.1.0";
	public static final String proxyPrefix = "net.shadowfacts.shadowtweaks.proxy.";
	public static final String guiFactory = "net.shadowfacts.shadowtweaks.client.gui.STGuiFactory";

	public static Logger log = LogManager.getLogger(modId);

	@SidedProxy(serverSide = ShadowTweaks.proxyPrefix + "CommonProxy", clientSide = ShadowTweaks.proxyPrefix + "ClientProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		STConfig.init(event);

		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLEventHandler());

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
