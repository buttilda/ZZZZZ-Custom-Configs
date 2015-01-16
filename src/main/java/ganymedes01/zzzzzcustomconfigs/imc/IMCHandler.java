package ganymedes01.zzzzzcustomconfigs.imc;

import ganymedes01.zzzzzcustomconfigs.ZZZZZCustomConfigs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCHandler {

	public static final List<String> blacklistedModIDs = new LinkedList<String>();
	public static final HashMapListed<TYPE, ItemStack> blacklist = new HashMapListed<TYPE, ItemStack>();

	public static void handleEvent(IMCEvent event) {
		int outputs = 0;
		int inputs = 0;
		for (IMCMessage message : event.getMessages())
			if ("blacklist-stack-as-output".equals(message.key)) {
				ItemStack stack = message.getItemStackValue();
				blacklist.add(TYPE.OUTPUT, stack);
				outputs++;
			} else if ("blacklist-stack-as-input".equals(message.key)) {
				ItemStack stack = message.getItemStackValue();
				blacklist.add(TYPE.INPUT, stack);
				inputs++;
			} else if ("blacklist-mod-items".equals(message.key)) {
				String modid = message.getStringValue();
				blacklistedModIDs.add(modid);
				ZZZZZCustomConfigs.logger.info("The mod " + modid + "has blacklisted itself, so none of its items will be allowed in recipes (for neither inputs or outputs).");
			} else
				ZZZZZCustomConfigs.logger.error("Unknown message key (" + message.key + ") sent by " + message.getSender());

		if (outputs > 0)
			ZZZZZCustomConfigs.logger.info(outputs + " stacks have been blacklisted and will not be allowed as outputs");
		if (inputs > 0)
			ZZZZZCustomConfigs.logger.info(inputs + " stacks have been blacklisted and will not be allowed as inputs");
	}

	private static enum TYPE {
		OUTPUT,
		INPUT;
	}

	private static class HashMapListed<K, V> extends HashMap<K, List<V>> {
		private static final long serialVersionUID = 1L;

		public boolean add(K key, V value) {
			return getInternal(key).add(value);
		}

		private List<V> getInternal(K key) {
			List<V> value = get(key);
			if (value == null) {
				value = new LinkedList<V>();
				put(key, value);
			}

			return value;
		}
	}
}