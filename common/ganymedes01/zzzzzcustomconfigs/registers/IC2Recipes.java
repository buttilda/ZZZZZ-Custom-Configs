package ganymedes01.zzzzzcustomconfigs.registers;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class IC2Recipes {

	public static void registerRecipes(Logger logger, String line) {
		String[] data = line.split(",");

		String recipeID = data[0];

		if (recipeID.equalsIgnoreCase("macerator")) {
			addMaceratorRecipe(getInputFromArray(data), getOutputs(canParse(data[1]) ? 4 : 3, data)[0]);
			logger.log(Level.INFO, "\tRegistered Macerator recipe input: " + data[1]);
		} else if (recipeID.equalsIgnoreCase("extractor")) {
			addExtractorRecipe(getInputFromArray(data), getOutputs(canParse(data[1]) ? 4 : 3, data)[0]);
			logger.log(Level.INFO, "\tRegistered Extractor recipe input: " + data[1]);
		} else if (recipeID.equalsIgnoreCase("compressor")) {
			addCompressorRecipe(getInputFromArray(data), getOutputs(canParse(data[1]) ? 4 : 3, data)[0]);
			logger.log(Level.INFO, "\tRegistered Compressor recipe input: " + data[1]);
		} else if (recipeID.equalsIgnoreCase("centrifuge")) {
			addThermalCentrifugeRecipe(getInputFromArray(data), getInt(data[canParse(data[1]) ? 4 : 3]), getOutputs(canParse(data[1]) ? 5 : 4, data));
			logger.log(Level.INFO, "\tRegistered Thermal Centrifuge recipe input: " + data[1]);
		} else if (recipeID.equalsIgnoreCase("metalformerExtruding")) {
			addRecipeExtruding(getInputFromArray(data), getOutputs(canParse(data[1]) ? 4 : 3, data)[0]);
			logger.log(Level.INFO, "\tRegistered Metal Former - Extruding recipe input: " + data[1]);
		} else if (recipeID.equalsIgnoreCase("metalformerCutting")) {
			addRecipeCutting(getInputFromArray(data), getOutputs(canParse(data[1]) ? 4 : 3, data)[0]);
			logger.log(Level.INFO, "\tRegistered Metal Former - Cutting recipe input: " + data[1]);
		} else if (recipeID.equalsIgnoreCase("metalformerRolling")) {
			addRecipeRolling(getInputFromArray(data), getOutputs(canParse(data[1]) ? 4 : 3, data)[0]);
			logger.log(Level.INFO, "\tRegistered Metal Former - Rolling recipe input: " + data[1]);
		} else if (recipeID.equalsIgnoreCase("oreWashing")) {
			addOreWashingRecipe(getInputFromArray(data), getOutputs(canParse(data[1]) ? 4 : 3, data));
			logger.log(Level.INFO, "\tRegistered Ore Washing Plant recipe input: " + data[1]);
		}
	}

	private static void addMaceratorRecipe(IRecipeInput input, ItemStack output) {
		Recipes.macerator.addRecipe(input, null, output);
	}

	private static void addExtractorRecipe(IRecipeInput input, ItemStack output) {
		Recipes.extractor.addRecipe(input, null, output);
	}

	private static void addCompressorRecipe(IRecipeInput input, ItemStack output) {
		Recipes.compressor.addRecipe(input, null, output);
	}

	private static void addRecipeExtruding(IRecipeInput input, ItemStack output) {
		Recipes.metalformerExtruding.addRecipe(input, null, output);
	}

	private static void addRecipeCutting(IRecipeInput input, ItemStack output) {
		Recipes.metalformerCutting.addRecipe(input, null, output);
	}

	private static void addRecipeRolling(IRecipeInput input, ItemStack output) {
		Recipes.metalformerRolling.addRecipe(input, null, output);
	}

	private static void addThermalCentrifugeRecipe(IRecipeInput input, int minHeat, ItemStack... output) {
		NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("minHeat", minHeat);

		Recipes.centrifuge.addRecipe(input, metadata, output);
	}

	private static void addOreWashingRecipe(IRecipeInput input, ItemStack... output) {
		NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("amount", 1000);

		Recipes.oreWashing.addRecipe(input, metadata, output);
	}

	private static ItemStack[] getOutputs(int beginIndex, String[] data) {
		ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();

		for (int i = beginIndex; i < data.length; i += 3)
			outputs.add(getItemStack(data[i], data[i + 1], data[i + 2]));

		return outputs.toArray(new ItemStack[0]);
	}

	private static IRecipeInput getInputFromArray(String... data) {
		if (canParse(data[1]))
			return new RecipeInputItemStack(getItemStack(data[1], data[2], data[3]));
		else
			return new RecipeInputOreDict(data[1].trim(), getInt(data[2]));
	}

	private static ItemStack getItemStack(String... data) {
		return new ItemStack(getInt(data[0]), getInt(data[1]), getInt(data[2]));
	}

	private static int getInt(String text) {
		return Integer.parseInt(text.trim());
	}

	private static boolean canParse(String data) {
		boolean ret;
		try {
			Integer.parseInt(data.trim());
			ret = true;
		} catch (NumberFormatException e) {
			ret = false;
		}
		return ret;
	}
}