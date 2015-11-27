package ganymedes01.zzzzzcustomconfigs.imc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import ganymedes01.zzzzzcustomconfigs.ZZZZZCustomConfigs;
import ganymedes01.zzzzzcustomconfigs.lib.StackUtils;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;
import net.minecraft.item.ItemStack;

public class IMCHandler {

	public static final HashMapListed<NodeType, ItemStack> blacklistStacks = new HashMapListed<NodeType, ItemStack>();
	public static final HashMapListed<NodeType, String> blacklistMods = new HashMapListed<NodeType, String>();

	public static void handleEvent(List<IMCMessage> messages) {
		int outputs = 0;
		int inputs = 0;
		for (IMCMessage message : messages)
			if ("blacklist-stack-as-output".equals(message.key)) {
				ItemStack stack = message.getItemStackValue();
				blacklistStacks.add(NodeType.OUTPUT, stack);
				outputs++;
			} else if ("blacklist-stack-as-input".equals(message.key)) {
				ItemStack stack = message.getItemStackValue();
				blacklistStacks.add(NodeType.INPUT, stack);
				inputs++;
			} else if ("blacklist-mod-as-input".equals(message.key)) {
				String modid = message.getStringValue();
				blacklistMods.add(NodeType.INPUT, modid);
				ZZZZZCustomConfigs.logger.info("The mod " + message.getSender() + " has blacklisted " + modid + ", so none of its items will be allowed as input.");
			} else if ("blacklist-mod-as-output".equals(message.key)) {
				String modid = message.getStringValue();
				blacklistMods.add(NodeType.OUTPUT, modid);
				ZZZZZCustomConfigs.logger.info("The mod " + message.getSender() + " has blacklisted " + modid + ", so none of its items will be allowed as output.");
			} else
				ZZZZZCustomConfigs.logger.error("Unknown message key (" + message.key + ") sent by " + message.getSender());

		if (outputs > 0)
			ZZZZZCustomConfigs.logger.info(outputs + " stacks have been blacklisted and will not be allowed as outputs");
		if (inputs > 0)
			ZZZZZCustomConfigs.logger.info(inputs + " stacks have been blacklisted and will not be allowed as inputs");
	}

	public static void checkIfAllowed(ItemStack stack, String modid, NodeType type) {
		if (type != NodeType.INPUT && type != NodeType.OUTPUT)
			return;

		boolean allowed = true;

		List<String> modList = blacklistMods.get(type);
		if (modList != null && !modList.isEmpty())
			allowed = !modList.contains(modid);

		if (allowed) {
			List<ItemStack> stackList = blacklistStacks.get(type);
			if (stackList != null && !stackList.isEmpty())
				for (ItemStack s : stackList)
					if (StackUtils.areStacksTheSame(s, stack, false)) {
						allowed = false;
						break;
					}
		}

		if (!allowed)
			throw new IllegalArgumentException("The item " + XMLHelper.toNodeValue(stack) + " has been blacklisted by another mod. It is not allowed as " + type.name().toLowerCase() + " of any recipes.");
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