package net.shadowfacts.shadowtweaks.feature.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.shadowtweaks.feature.Feature;

/**
 * @author shadowfacts
 */
public class FeatureExtraRecipes extends Feature {

	private boolean enableLogChestRecipe;

	@Override
	public void configure(Configuration config) {
		super.configure(config);

		enableLogChestRecipe = config.getBoolean("enableLogChestRecipe", getFeatureName(), true, "Add a recipe for 8 logs (in chest shape) for 4 chests");
	}

	@Override
	public boolean requiresServerSide() {
		return true;
	}

	@Override
	public void init() {
		if (enabled) {
			if (enableLogChestRecipe) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.CHEST, 4), "OOO", "O O", "OOO", 'O', "logWood"));
			}
		}
	}

}
