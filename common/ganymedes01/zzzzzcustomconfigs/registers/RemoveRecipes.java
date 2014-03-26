package ganymedes01.zzzzzcustomconfigs.registers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RemoveRecipes {

	public static void removeRecipeFromLine(Logger logger, String line) {
		int itemID = Integer.parseInt(line.split(",")[0]);
		int meta = Integer.parseInt(line.split(",")[1]);

		ArrayList<IRecipe> list = new ArrayList<IRecipe>();
		for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++) {
			IRecipe recipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);
			ItemStack output = recipe.getRecipeOutput();
			if (output != null)
				if (output.itemID == itemID && output.getItemDamage() == meta)
					list.add(recipe);
		}

		int recipeCount = list.size();
		for (IRecipe recipe : list)
			CraftingManager.getInstance().getRecipeList().remove(recipe);

		logger.log(Level.INFO, "\tRemoved " + recipeCount + " recipe" + (recipeCount > 1 || recipeCount == 0 ? "s" : "") + " for " + Item.itemsList[itemID].getUnlocalizedName(new ItemStack(itemID, 1, meta)));
	}
}