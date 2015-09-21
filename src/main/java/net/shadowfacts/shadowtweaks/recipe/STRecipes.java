package net.shadowfacts.shadowtweaks.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author shadowfacts
 */
public class STRecipes {

	public static void addRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.chest, 4), "OOO", "O O", "OOO", 'O', "logWood"));
	}

}
