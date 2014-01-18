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
		int recipeCount = 0;

		ArrayList<IRecipe> list = (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < list.size(); i++) {
			ItemStack output = list.get(i).getRecipeOutput();
			if (output != null)
				if (output.itemID == itemID && output.getItemDamage() == meta) {
					CraftingManager.getInstance().getRecipeList().remove(i);
					recipeCount++;
				}
		}
		logger.log(Level.INFO, "\tRemoved " + recipeCount + " recipe" + (recipeCount > 1 ? "s" : "") + " for " + Item.itemsList[itemID].getUnlocalizedName(new ItemStack(itemID, 1, meta)));
	}
}