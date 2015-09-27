package net.shadowfacts.shadowtweaks.client.gui;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.shadowfacts.shadowtweaks.STConfig;
import net.shadowfacts.shadowtweaks.ShadowTweaks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * In-game configuration GUI
 *
 * @author shadowfacts
 */
public class GuiModOptionsList extends GuiModList {

	private GuiScreen parent;

	public GuiModOptionsList(GuiScreen parent) {
		super(null);
		this.parent = parent;
		if (STConfig.onlyConfigurableMods) {
			try {
				Field modsField = GuiModList.class.getDeclaredField("mods");
				modsField.setAccessible(true);
				@SuppressWarnings("unchecked")
				ArrayList<ModContainer> mods = (ArrayList<ModContainer>)modsField.get(this);

				List<ModContainer> toRemove = new ArrayList<>();
				mods.stream().filter(mod -> mod.getGuiClassName() == null).forEach(toRemove::add);
				toRemove.stream().forEach(mods::remove);

			} catch (ReflectiveOperationException e) {
				ShadowTweaks.log.error("There was a problem opening the in-game config gui");
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			switch (button.id) {
				case 6:
					mc.displayGuiScreen(parent);
				default:
					super.actionPerformed(button);
			}
		}
	}


}
