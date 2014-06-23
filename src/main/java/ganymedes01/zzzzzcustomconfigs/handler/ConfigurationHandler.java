package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.lib.Files;
import ganymedes01.zzzzzcustomconfigs.lib.Reference;
import ganymedes01.zzzzzcustomconfigs.registers.BlacklistedEntities;
import ganymedes01.zzzzzcustomconfigs.registers.CraftingRecipes;
import ganymedes01.zzzzzcustomconfigs.registers.GTRecipes;
import ganymedes01.zzzzzcustomconfigs.registers.IC2Recipes;
import ganymedes01.zzzzzcustomconfigs.registers.OreDictionaryRegister;
import ganymedes01.zzzzzcustomconfigs.registers.RemoveRecipes;
import ganymedes01.zzzzzcustomconfigs.registers.SmeltingRegister;
import ganymedes01.zzzzzcustomconfigs.registers.TC4AspectsRegister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigurationHandler {

	private static final String LOG = "Registering %s: %d entries found.";

	enum Types {
		ORE_DICT, SMELTING, SHAPED, SHAPELESS, ORE_DICT_SMELTING, BLACKLIST_ENTITY, REMOVE_RECIPE, IC2_RECIPE, TC4_ASPECTS, GT_RECIPE;
	}

	private final static Logger logger = Logger.getLogger(Reference.MOD_ID.toUpperCase());

	public static void preInit(FMLPreInitializationEvent event) {
		try {

			registerFile(Files.getOreDictionaryFile(), Types.ORE_DICT);
			registerFile(Files.getSmeltingFile(), Types.SMELTING);
			registerFile(Files.getBlacklistEntityFile(), Types.BLACKLIST_ENTITY);

		} catch (IOException e) {
			logger.log(Level.SEVERE, Reference.MOD_NAME + " has had a problem pre-initialising its configuration");
			throw new RuntimeException(e);
		}
	}

	public static void init() {
		try {

			registerFile(Files.getOreDictSmeltingFile(), Types.ORE_DICT_SMELTING);
			if (Loader.isModLoaded("IC2"))
				registerFile(Files.getIC2RecipeFile(), Types.IC2_RECIPE);
			if (Loader.isModLoaded("Thaumcraft"))
				registerFile(Files.getTC4AspectsFile(), Types.TC4_ASPECTS);

		} catch (IOException e) {
			logger.log(Level.SEVERE, Reference.MOD_NAME + " has had a problem initialising its configuration");
			throw new RuntimeException(e);
		}
	}

	public static void serverStarting() {
		try {

			registerFile(Files.getRemoveRecipeFile(), Types.REMOVE_RECIPE);
			registerFile(Files.getShapedRecipesFile(), Types.SHAPED);
			registerFile(Files.getShapedOreDictRecipesFile(), Types.SHAPELESS);
			if (Loader.isModLoaded("gregtech"))
				registerFile(Files.getGTRecipeFile(), Types.GT_RECIPE);

		} catch (IOException e) {
			logger.log(Level.SEVERE, Reference.MOD_NAME + " has had a problem removing recipes");
			throw new RuntimeException(e);
		}
	}

	private static void registerFile(File configFile, Types id) throws IOException {
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
				case BLACKLIST_ENTITY:
					BlacklistedEntities.blacklistEntityFromLine(logger, line);
					break;
				case REMOVE_RECIPE:
					RemoveRecipes.removeRecipeFromLine(logger, line);
					break;
				case IC2_RECIPE:
					IC2Recipes.registerRecipes(logger, line);
					break;
				case TC4_ASPECTS:
					TC4AspectsRegister.registerAspects(logger, line);
					break;
				case GT_RECIPE:
					GTRecipes.registerRecipes(logger, line);
					break;
				default:
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