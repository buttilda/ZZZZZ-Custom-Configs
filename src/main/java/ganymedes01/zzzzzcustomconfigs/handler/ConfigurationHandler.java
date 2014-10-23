package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.files.CraftingRecipes;
import ganymedes01.zzzzzcustomconfigs.files.EntityBlacklist;
import ganymedes01.zzzzzcustomconfigs.files.GregTech;
import ganymedes01.zzzzzcustomconfigs.files.IndustrialCraft2;
import ganymedes01.zzzzzcustomconfigs.files.OreDict;
import ganymedes01.zzzzzcustomconfigs.files.RemoveRecipes;
import ganymedes01.zzzzzcustomconfigs.files.Smelting;
import ganymedes01.zzzzzcustomconfigs.files.Thaumcraft4;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;

import java.util.LinkedList;
import java.util.List;

public class ConfigurationHandler {

	private static final List<ConfigFile> files = new LinkedList<ConfigFile>();

	public static void preInit() {
		files.add(new CraftingRecipes());
		files.add(new EntityBlacklist());
		files.add(new GregTech());
		files.add(new IndustrialCraft2());
		files.add(new OreDict());
		files.add(new RemoveRecipes());
		files.add(new Smelting());
		files.add(new Thaumcraft4());

		for (ConfigFile file : files) {
			file.initFile();
			if (file.isEnabled())
				file.preInit();
		}

		/*
		registerFile(Files.getBlacklistEntityFile(), Types.BLACKLIST_ENTITY);
		 */
	}

	public static void init() {
		for (ConfigFile file : files)
			if (file.isEnabled())
				file.init();
	}

	public static void serverStarting() {
		for (ConfigFile file : files)
			if (file.isEnabled())
				file.postInit();

		/*
		registerFile(Files.getRemoveRecipeFile(), Types.REMOVE_RECIPE);
		registerFile(Files.getGTRecipeFile(), Types.GT_RECIPE);
		 */
	}
}