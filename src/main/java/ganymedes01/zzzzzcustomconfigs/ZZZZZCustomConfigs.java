package ganymedes01.zzzzzcustomconfigs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.zzzzzcustomconfigs.handler.ConfigurationHandler;
import ganymedes01.zzzzzcustomconfigs.handler.HandlerEvents;
import ganymedes01.zzzzzcustomconfigs.imc.IMCHandler;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.lib.Reference;
import ganymedes01.zzzzzcustomconfigs.recipes.ItemFluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES)
public class ZZZZZCustomConfigs {

	@Instance(Reference.MOD_ID)
	public static ZZZZZCustomConfigs instance;

	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID.toUpperCase());

	public static boolean showTooltips = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		try {
			config.load();
			showTooltips = config.get("Options", "showTooltip", showTooltips).getBoolean(showTooltips);

		} catch (Exception e) {
			FMLLog.severe(Reference.MOD_NAME + " has had a problem loading its configuration");
			throw new RuntimeException(e);
		} finally {
			config.save();
		}

		ConfigFile.setPath(event.getModConfigurationDirectory().getAbsolutePath());
		MinecraftForge.EVENT_BUS.register(HandlerEvents.INSTANCE);
		MinecraftForge.ORE_GEN_BUS.register(HandlerEvents.INSTANCE);
		ConfigurationHandler.preInit();

		GameRegistry.registerItem(ItemFluid.instance, "item_fluid");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		IMCHandler.handleEvent(FMLInterModComms.fetchRuntimeMessages(this));

		ConfigurationHandler.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ConfigurationHandler.postInit();
	}
}