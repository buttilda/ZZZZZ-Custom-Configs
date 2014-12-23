package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.files.BloodMagic;
import ganymedes01.zzzzzcustomconfigs.files.Botania;
import ganymedes01.zzzzzcustomconfigs.files.Buildcraft;
import ganymedes01.zzzzzcustomconfigs.files.ChestLoot;
import ganymedes01.zzzzzcustomconfigs.files.CraftingRecipes;
import ganymedes01.zzzzzcustomconfigs.files.EntityDrops;
import ganymedes01.zzzzzcustomconfigs.files.ExNihilo;
import ganymedes01.zzzzzcustomconfigs.files.Fishing;
import ganymedes01.zzzzzcustomconfigs.files.Forestry;
import ganymedes01.zzzzzcustomconfigs.files.IndustrialCraft2;
import ganymedes01.zzzzzcustomconfigs.files.OreDict;
import ganymedes01.zzzzzcustomconfigs.files.PneumaticCraft;
import ganymedes01.zzzzzcustomconfigs.files.Railcraft;
import ganymedes01.zzzzzcustomconfigs.files.RemoveRecipes;
import ganymedes01.zzzzzcustomconfigs.files.Smelting;
import ganymedes01.zzzzzcustomconfigs.files.Thaumcraft;
import ganymedes01.zzzzzcustomconfigs.files.ThermalExpansion;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.Loader;

public class ConfigurationHandler {

	private static final List<ConfigFile> files = new LinkedList<ConfigFile>();

	public static void preInit() {
		files.add(new CraftingRecipes());
		files.add(new OreDict());
		files.add(new RemoveRecipes());
		files.add(new Smelting());
		files.add(new Fishing());
		files.add(new ChestLoot());
		files.add(new EntityDrops());

		if (Loader.isModLoaded("IC2"))
			files.add(new IndustrialCraft2());
		if (Loader.isModLoaded("Thaumcraft"))
			files.add(new Thaumcraft());
		if (Loader.isModLoaded("BuildCraft|Energy"))
			files.add(new Buildcraft());
		if (Loader.isModLoaded("Railcraft"))
			files.add(new Railcraft());
		if (Loader.isModLoaded("PneumaticCraft"))
			files.add(new PneumaticCraft());
		if (Loader.isModLoaded("Botania"))
			files.add(new Botania());
		if (Loader.isModLoaded("Forestry"))
			files.add(new Forestry());
		if (Loader.isModLoaded("AWWayofTime"))
			files.add(new BloodMagic());
		if (Loader.isModLoaded("exnihilo"))
			files.add(new ExNihilo());
		if (Loader.isModLoaded("ThermalExpansion"))
			files.add(new ThermalExpansion());

		for (ConfigFile file : files)
			file.initFile();
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
	}
}