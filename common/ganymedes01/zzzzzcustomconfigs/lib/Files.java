package ganymedes01.zzzzzcustomconfigs.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Files {

	private static String path;

	private static final String ORE_DICTIONARY = "OreDictionary";
	private static final String SMELTING = "Smelting";
	private static final String ORE_DICTIONARY_SMELTING = "OreDictSmelting";
	private static final String SHAPED_RECIPE = "ShapedRecipe";
	private static final String SHAPELESS_RECIPE = "ShapelessRecipe";

	private static final String ORE_DICTIONARY_DEFAULT = "# oreName, itemID, itemMetadata #";
	private static final String SMELTING_DEFAULT = "# inputID, inputMetadata, outputID, outputAmount, outputMetadata, xp #";
	private static final String ORE_DICTIONARY_SMELTING_DEFAULT = "# oreName, outputID, outputAmount, outputMetadata, xp #\n" + "# Will override recipes from vanilla and mods loaded previously #";
	private static final String SHAPED_RECIPE_DEFAULT = "# resultID, resultAmount, resultMetadata, topRow, middleRow, bottomRow, (ingredIdentifier, ingredID, ingredMetada)... #\n" + "# Diamond sword recipe example: 276, 0, 0, x, x, y, x, 264, 0, y, 280, 0 #\n"
	+ "# If is empty leave it blank. Example: xx, , xx\n" + "# L shaped recipe example (leave an actual blank space where it says (space)): (space)(space)x, (space)(space)x, xxx #";
	private static final String SHAPELESS_RECIPE_DEFAULT = "# resultID, resultAmount, resultMetadata, (inputId, inputMetadata)... #\n" + "# Example of recipe for bedrock using stone, dirt and grass: 7, 1, 0, 1, 0, 2, 0, 3, 0 #";

	public static void setPath(String path) {
		Files.path = path + File.separator + Reference.MOD_ID + File.separator;

		File parentPath = new File(Files.path);
		parentPath.mkdirs();
	}

	public static File getOreDictionaryFile() throws IOException {
		return getFile(ORE_DICTIONARY, ORE_DICTIONARY_DEFAULT);
	}

	public static File getSmeltingFile() throws IOException {
		return getFile(SMELTING, SMELTING_DEFAULT);
	}

	public static File getOreDictSmeltingFile() throws IOException {
		return getFile(ORE_DICTIONARY_SMELTING, ORE_DICTIONARY_SMELTING_DEFAULT);
	}

	public static File getShapedRecipesFile() throws IOException {
		return getFile(SHAPED_RECIPE, SHAPED_RECIPE_DEFAULT);
	}

	public static File getShapedOreDictRecipesFile() throws IOException {
		return getFile(SHAPELESS_RECIPE, SHAPELESS_RECIPE_DEFAULT);
	}

	public static File getFile(String name, String heading) throws IOException {
		File file = new File(path + name + ".csv");
		if (!file.exists()) {
			file.createNewFile();

			BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			writer.write(heading);
			writer.close();
		}
		return file;
	}
}