package net.shadowfacts.shadowtweaks.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.shadowtweaks.STConfig;

/**
 * @author shadowfacts
 */
public class STRecipes {

	public static void addRecipes() {
		if (STConfig.addLogChestRecipe) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.chest, 4), "OOO", "O O", "OOO", 'O', "logWood"));
	}

}
