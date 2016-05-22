package net.shadowfacts.shadowtweaks.feature.sign;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowtweaks.feature.Feature;

/**
 * @author shadowfacts
 */
public class FeatureSign extends Feature {

	private boolean editSigns;
	private boolean clearSigns;

	@Override
	public void configure(Configuration config) {
		super.configure(config);

		editSigns = config.getBoolean("editSigns", getFeatureName(), true, "Right-click to edit signs");
		clearSigns = config.getBoolean("clearSigns", getFeatureName(), true, "Shift+right-click to clear signs");
	}

	@Override
	public boolean requiresServerSide() {
		return editSigns || clearSigns;
	}

	@Override
	public void preInit() {
		if (editSigns || clearSigns) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		boolean success = false;

		TileEntity te = event.getWorld().getTileEntity(event.getPos());
		if (te instanceof TileEntitySign) {
			TileEntitySign sign = (TileEntitySign)te;
			if (event.getEntityPlayer().isSneaking() && clearSigns) {
				ITextComponent[] text = new ITextComponent[] {new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("")};
				ObfuscationReflectionHelper.setPrivateValue(TileEntitySign.class, sign, text, "signText", "field_145915_a");
				success = true;
			} else if (editSigns) {
				event.getEntityPlayer().openEditSign(sign);
				success = true;
			}
		}

		if (success) {
			event.setCanceled(true);
			event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
		}
	}

}
