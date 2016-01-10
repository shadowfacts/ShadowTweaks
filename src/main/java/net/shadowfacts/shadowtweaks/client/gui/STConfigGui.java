package net.shadowfacts.shadowtweaks.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
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

		list.add(getCategory("features", StringHelper.localize("shadowtweaks.config.ctgy.features"), ""));
		list.add(getCategory("miscellaneous", StringHelper.localize("shadowtweaks.config.ctgy.misc"), ""));

		return list;
	}

	@SuppressWarnings("unchecked")
	private static IConfigElement getCategory(String category, String name, String tooltipKey) {
		IConfigElement configElement = new ConfigElement(ConfigManager.instance.getConfigurationObject(ShadowTweaks.modId).getCategory(category));
		return new DummyConfigElement.DummyCategoryElement(name, tooltipKey, configElement.getChildElements());
	}

}
