package ganymedes01.zzzzzcustomconfigs.registers;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

public class RemoveRecipes {

	@SuppressWarnings("unchecked")
	public static void removeRecipeFromLine(Logger logger, String line) {
		String itemID = line.split(",")[0].trim();
		int meta = Integer.parseInt(line.split(",")[1].trim());

		ArrayList<IRecipe> list = new ArrayList<IRecipe>();
		for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++) {
			IRecipe recipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);
			if (recipe == null)
				continue;
			ItemStack output = recipe.getRecipeOutput();
			if (output != null)
				if (Item.itemRegistry.getNameForObject(output.getItem()).matches(ensureNamespaced(itemID)) && output.getItemDamage() == meta)
					list.add(recipe);
		}

		ArrayList<ItemStack> smeltingList = new ArrayList<ItemStack>();
		for (Object obj : FurnaceRecipes.smelting().getSmeltingList().entrySet()) {
			Entry<ItemStack, ItemStack> entry = (Entry<ItemStack, ItemStack>) obj;
			ItemStack output = entry.getValue();
			if (output != null)
				if (Item.itemRegistry.getNameForObject(output.getItem()).matches(ensureNamespaced(itemID)) && output.getItemDamage() == meta)
					smeltingList.add(entry.getKey());
		}

		int recipeCount = list.size() + smeltingList.size();

		for (ItemStack result : smeltingList)
			FurnaceRecipes.smelting().getSmeltingList().remove(result);

		for (IRecipe recipe : list)
			CraftingManager.getInstance().getRecipeList().remove(recipe);

		logger.log(Level.INFO, "\tRemoved " + recipeCount + " recipe" + (recipeCount > 1 ? "s" : "") + " for " + ((Item) Item.itemRegistry.getObject(itemID)).getUnlocalizedName(new ItemStack((Item) Item.itemRegistry.getObject(itemID), 1, meta)));
	}

	protected static String ensureNamespaced(String paramString) {
		return paramString.indexOf(':') == -1 ? "minecraft:" + paramString : paramString;
	}
}