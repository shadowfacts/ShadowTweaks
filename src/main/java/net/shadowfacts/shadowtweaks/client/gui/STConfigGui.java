package net.shadowfacts.shadowtweaks.client.gui;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.shadowfacts.shadowmc.config.ConfigManager;
import net.shadowfacts.shadowmc.util.StringHelper;
import net.shadowfacts.shadowtweaks.ShadowTweaks;

import java.util.ArrayList;
import java.util.List;

/**
 * In-game ShadowTweaks configuration GUI
 *
 * @author shadowfacts
 */
public class STConfigGui extends GuiConfig {

	public STConfigGui(GuiScreen parent) {
		super(parent, getConfigElements(), ShadowTweaks.modId, false, false, "ShadowTweaks Configuration");
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<>();

		list.add(getCategory("features", StringHelper.localize("shadowtweaks.config.ctgy.features"), "shadowtweaks.config.ctgy.features"));

		return list;
	}

	@SuppressWarnings("unchecked")
	private static IConfigElement getCategory(String category, String name, String tooltipKey) {
		IConfigElement configElement = new ConfigElement(ConfigManager.instance.getConfigurationObject(ShadowTweaks.modId).getCategory(category));
		return new DummyConfigElement.DummyCategoryElement(name, tooltipKey, configElement.getChildElements());
	}

}
