package ganymedes01.zzzzzcustomconfigs;

import ganymedes01.zzzzzcustomconfigs.handler.ConfigurationHandler;
import ganymedes01.zzzzzcustomconfigs.handler.HandlerEvents;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.lib.Reference;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES)
public class ZZZZZCustomConfigs {

	@Instance(Reference.MOD_ID)
	public static ZZZZZCustomConfigs instance;

	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

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
		MinecraftForge.EVENT_BUS.register(new HandlerEvents());
		ConfigurationHandler.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ConfigurationHandler.init();
	}

	@EventHandler
	public void init(FMLPostInitializationEvent event) {
		ConfigurationHandler.postInit();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		ConfigurationHandler.serverStarting();
	}

	public static final List<String> blacklistedModIDs = new LinkedList<String>();
	public static final List<ItemStack> blacklistedInputs = new LinkedList<ItemStack>();
	public static final List<ItemStack> blacklistedOutputs = new LinkedList<ItemStack>();

	@EventHandler
	public void interModComm(IMCEvent event) {
		int outputs = 0;
		int inputs = 0;
		for (IMCMessage message : event.getMessages())
			if ("blacklist-stack-as-output".equals(message.key)) {
				ItemStack stack = message.getItemStackValue();
				blacklistedOutputs.add(stack);
				outputs++;
			} else if ("blacklist-stack-as-input".equals(message.key)) {
				ItemStack stack = message.getItemStackValue();
				blacklistedInputs.add(stack);
				inputs++;
			} else if ("blacklist-mod-items".equals(message.key)) {
				String modid = message.getStringValue();
				blacklistedModIDs.add(modid);
				logger.info("The mod " + modid + "has blacklisted itself, so none of its items will be allowed in recipes (for neither inputs or outputs).");
			} else
				logger.error("Unknown message key (" + message.key + ") sent by " + message.getSender());

		if (outputs > 0)
			logger.info(outputs + " stacks have been blacklisted and will not be allowed as outputs");
		if (inputs > 0)
			logger.info(inputs + " stacks have been blacklisted and will not be allowed as inputs");
	}
}