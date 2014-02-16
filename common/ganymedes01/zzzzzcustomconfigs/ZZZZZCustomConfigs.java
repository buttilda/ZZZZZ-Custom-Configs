package ganymedes01.zzzzzcustomconfigs;

import ganymedes01.zzzzzcustomconfigs.handler.ConfigurationHandler;
import ganymedes01.zzzzzcustomconfigs.handler.HandlerEvents;
import ganymedes01.zzzzzcustomconfigs.lib.Files;
import ganymedes01.zzzzzcustomconfigs.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES)
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class ZZZZZCustomConfigs {

	@Instance(Reference.MOD_ID)
	public static ZZZZZCustomConfigs instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Files.setPath(event.getModConfigurationDirectory().getAbsolutePath());

		MinecraftForge.EVENT_BUS.register(new HandlerEvents());

		ConfigurationHandler.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ConfigurationHandler.init();

		for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++) {
			ItemStack output = ((IRecipe) CraftingManager.getInstance().getRecipeList().get(i)).getRecipeOutput();
			if (output != null && output.itemID == Item.bed.itemID)
				CraftingManager.getInstance().getRecipeList().remove(i);
		}
	}
}