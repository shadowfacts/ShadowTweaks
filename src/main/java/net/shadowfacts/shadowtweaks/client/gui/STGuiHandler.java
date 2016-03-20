package net.shadowfacts.shadowtweaks.client.gui;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author shadowfacts
 */
public class STGuiHandler implements IGuiHandler {

	public static final int signID = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == signID) return new GuiEditSign((TileEntitySign)world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

}
