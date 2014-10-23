package ganymedes01.zzzzzcustomconfigs;

import ganymedes01.zzzzzcustomconfigs.handler.ConfigurationHandler;
import ganymedes01.zzzzzcustomconfigs.handler.HandlerEvents;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.lib.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES)
public class ZZZZZCustomConfigs {

	@Instance(Reference.MOD_ID)
	public static ZZZZZCustomConfigs instance;

	public static boolean showTooltips = true;

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
		MinecraftForge.EVENT_BUS.register(new HandlerEvents());
		ConfigurationHandler.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ConfigurationHandler.init();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		ConfigurationHandler.serverStarting();
	}
}