package ganymedes01.zzzzzcustomconfigs.registers;

import ganymedes01.zzzzzcustomconfigs.lib.Logs;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class SmeltingRegister {

	public static void registerSmeltingFromString(Logger logger, String line) {
		String[] data = line.split(",");
		if (data.length != 6) {
			logger.log(Level.SEVERE, String.format(Logs.INVALID_ARGS_NUMBER + line, "smelting"));
			return;
		}

		Integer inputID;
		Integer inputMeta;

		Integer outputID;
		Integer outputSize;
		Integer outputMeta;

		Float xp;

		try {
			inputID = Integer.parseInt(data[0].trim());
			inputMeta = Integer.parseInt(data[1].trim());

			outputID = Integer.parseInt(data[2].trim());
			outputSize = Integer.parseInt(data[3].trim());
			outputMeta = Integer.parseInt(data[4].trim());

			xp = Float.parseFloat(data[5].trim());
		} catch (NumberFormatException e) {
			logger.log(Level.SEVERE, String.format(Logs.FAILED_TO_CAST + line, "smelting"));
			return;
		}

		if (inputID >= Item.itemsList.length || Item.itemsList[inputID] == null || outputID >= Item.itemsList.length || Item.itemsList[outputID] == null) {
			logger.log(Level.SEVERE, String.format(Logs.INVALID_ID + line, "smelting"));
			return;
		}

		FurnaceRecipes.smelting().addSmelting(inputID, inputMeta, new ItemStack(outputID, outputSize, outputMeta), xp);
		logger.log(Level.INFO, "\tRegistered new Smelting: " + Item.itemsList[inputID].getUnlocalizedName(new ItemStack(inputID, 1, inputMeta)) + " to " + Item.itemsList[outputID].getUnlocalizedName(new ItemStack(outputID, outputSize, outputMeta)));
	}

	public static void registerOreDictSmeltingFromString(Logger logger, String line) {
		String[] data = line.split(",");
		if (data.length != 5) {
			logger.log(Level.SEVERE, String.format(Logs.INVALID_ARGS_NUMBER + line, "ore dict smelting"));
			return;
		}

		String input;

		Integer outputID;
		Integer outputSize;
		Integer outputMeta;

		Float xp;

		try {
			input = data[0].trim();

			outputID = Integer.parseInt(data[1].trim());
			outputSize = Integer.parseInt(data[2].trim());
			outputMeta = Integer.parseInt(data[3].trim());

			xp = Float.parseFloat(data[4].trim());
		} catch (NumberFormatException e) {
			logger.log(Level.SEVERE, String.format(Logs.FAILED_TO_CAST + line, "ore dict smelting"));
			return;
		}

		if (outputID >= Item.itemsList.length || Item.itemsList[outputID] == null) {
			logger.log(Level.SEVERE, String.format(Logs.INVALID_ID + line, "ore dict smelting"));
			return;
		}

		for (ItemStack ore : OreDictionary.getOres(input))
			if (ore != null) {
				FurnaceRecipes.smelting().addSmelting(ore.itemID, ore.getItemDamage(), new ItemStack(outputID, outputSize, outputMeta), xp);
				logger.log(Level.INFO, "\tRegistered new ore dictionary Smelting: " + Item.itemsList[ore.itemID].getUnlocalizedName(ore) + " to " + Item.itemsList[outputID].getUnlocalizedName(new ItemStack(outputID, outputSize, outputMeta)));
			}
	}
}