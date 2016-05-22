package net.shadowfacts.shadowtweaks.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.shadowfacts.shadowtweaks.ShadowTweaks;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public class STConfigGui extends GuiConfig {

	public STConfigGui(GuiScreen parent) {
		super(parent, getConfigElements(), ShadowTweaks.modId, false, false, "ShadowTweaks configuration");
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<>();

		ShadowTweaks.config.getCategoryNames().forEach(s -> {
			list.add(getCategory(ShadowTweaks.config.getCategory(s), I18n.format("shadowtweaks.cfg." + s), ""));
		});

		return list;
	}

	private static IConfigElement getCategory(ConfigCategory category, String name, String tooltipKey) {
		IConfigElement element = new ConfigElement(category);
		return new DummyConfigElement.DummyCategoryElement(name, tooltipKey, element.getChildElements());
	}


}
