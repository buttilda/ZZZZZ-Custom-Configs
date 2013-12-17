package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.lib.Files;
import ganymedes01.zzzzzcustomconfigs.lib.Reference;
import ganymedes01.zzzzzcustomconfigs.registers.CraftingRecipes;
import ganymedes01.zzzzzcustomconfigs.registers.OreDictionaryRegister;
import ganymedes01.zzzzzcustomconfigs.registers.SmeltingRegister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigurationHandler {

	private static final String LOG = "Registering %s: %d entries found.";

	private static final int ORE_DICT = 0;
	private static final int SMELTING = 1;
	private static final int SHAPED = 2;
	private static final int SHAPELESS = 3;
	private static final int ORE_DICT_SMELTING = 4;

	private final static Logger logger = Logger.getLogger(Reference.MOD_ID.toUpperCase());

	static {
		logger.setParent(FMLLog.getLogger());
	}

	public static void preInit(FMLPreInitializationEvent event) {
		try {

			registerFile(Files.getOreDictionaryFile(), ORE_DICT);
			registerFile(Files.getSmeltingFile(), SMELTING);
			registerFile(Files.getShapedRecipesFile(), SHAPED);
			registerFile(Files.getShapedOreDictRecipesFile(), SHAPELESS);

		} catch (IOException e) {
			FMLLog.log(Level.SEVERE, e, Reference.MOD_NAME + " has had a problem pre-initialising its configuration");
			throw new RuntimeException(e);
		}
	}

	public static void init() {
		try {

			registerFile(Files.getOreDictSmeltingFile(), ORE_DICT_SMELTING);

		} catch (IOException e) {
			FMLLog.log(Level.SEVERE, e, Reference.MOD_NAME + " has had a problem initialising its configuration");
			throw new RuntimeException(e);
		}
	}

	private static void registerFile(File configFile, int id) throws IOException {
		String[] data = getArrayFromFile(configFile);
		if (data == null)
			return;

		logger.log(Level.INFO, String.format(LOG, configFile.getName(), data.length));
		for (String line : data)
			switch (id) {
				case ORE_DICT:
					OreDictionaryRegister.registerOreFromString(logger, line);
					break;
				case SMELTING:
					SmeltingRegister.registerSmeltingFromString(logger, line);
					break;
				case SHAPED:
					CraftingRecipes.registerShapedRecipeFromLine(logger, line);
					break;
				case SHAPELESS:
					CraftingRecipes.registerShapelessRecipeFromLine(logger, line);
					break;
				case ORE_DICT_SMELTING:
					SmeltingRegister.registerOreDictSmeltingFromString(logger, line);
					break;
			}
	}

	private static String[] getArrayFromFile(File configFile) throws IOException {
		if (!configFile.exists()) {
			configFile.createNewFile();
			return null;
		}

		BufferedReader reader = new BufferedReader(new FileReader(configFile));

		ArrayList<String> array = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null)
			if (!line.trim().startsWith("#"))
				array.add(line);

		reader.close();
		return array.toArray(new String[0]);
	}
}