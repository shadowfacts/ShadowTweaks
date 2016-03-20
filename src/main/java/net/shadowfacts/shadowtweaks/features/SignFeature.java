package net.shadowfacts.shadowtweaks.features;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.shadowfacts.shadowtweaks.STConfig;
import net.shadowfacts.shadowtweaks.ShadowTweaks;
import net.shadowfacts.shadowtweaks.client.gui.STGuiHandler;

/**
 * @author shadowfacts
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignFeature {

	public static void handleSignRightClicked(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
		boolean success = false;

		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntitySign) {
			TileEntitySign sign = (TileEntitySign)te;
			if (player.isSneaking() && STConfig.clearSigns) {
				ITextComponent[] text = new ITextComponent[]{new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("")};
				ObfuscationReflectionHelper.setPrivateValue(TileEntitySign.class, sign, text, "signText", "field_145915_a");
				success = true;
			} else if (STConfig.editSigns) {
				player.openGui(ShadowTweaks.instance, STGuiHandler.signID, world, pos.getX(), pos.getY(), pos.getZ());
				success = true;
			}
		}

		if (success) {
			player.swingArm(EnumHand.MAIN_HAND);
		}
	}

}
