package net.shadowfacts.shadowtweaks;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowmc.config.ConfigManager;

/**
 * Event handler for ForgeModLoader events
 *
 * @author shadowfacts
 */
public class FMLEventHandler {

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(ShadowTweaks.modId)) {
			ConfigManager.instance.load(ShadowTweaks.modId);
		}
	}

}
