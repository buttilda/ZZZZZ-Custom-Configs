package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.handler.ConfigurationHandler;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class OreDict extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "THE ORE DICTIONARY DOES NOT SUPPORT NBT! THIS IS NOT THIS MOD'S FAULT BUT THE WAY THE ORE DICTIONARY ITSELF WAS CODED!\n";
		header += "There is no limit to how many stack parameters you have, just make sure you number them (stack1, stack2, stack3...)\n";
		header += "The following registers the vanilla iron and gold ingots with the name \"ingots\"\n";

		XMLBuilder builder = new XMLBuilder("ore");
		builder.makeEntry("name", "ingots");
		builder.makeEntries("stack", new Object[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot) });
		header += builder.toString();

		header += "\n\nThe following registers the vanilla gold ingot as \"ingotGold\"\n";

		builder = new XMLBuilder("ore");
		builder.makeEntry("name", "ingotGold");
		builder.makeEntry("stack", new ItemStack(Items.gold_ingot));
		header += builder.toString();
	}

	public OreDict() {
		super("OreDictionary", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("ore")) {
				String name = XMLParser.parseStringNode(node.getNode("name"));
				for (XMLNode n : node.getNodes())
					if (n.getName().startsWith("stack")) {
						ItemStack stack = XMLParser.parseItemStackNode(n, NodeType.OUTPUT);
						OreDictionary.registerOre(name, stack);
					}
			}
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public static void onOreRegister(OreRegisterEvent event) {
		Map<Integer, List<Integer>> stackToId = null;
		try {
			Field f = OreDictionary.class.getDeclaredField("stackToId");
			f.setAccessible(true);
			stackToId = (Map<Integer, List<Integer>>) f.get(null);
		} catch (Exception e) {
			throw new RuntimeException("Failed to reflect into OreDictionary. Unable to continue.", e);
		}

		for (XMLNode node : ConfigurationHandler.oreDict.xmlNode.getNodes())
			if (node.getName().equals("remove")) {
				ItemStack stack = XMLParser.parseItemStackNode(node.getNode("stack"), NodeType.OUTPUT);
				if (OreDictionary.itemMatches(stack, event.Ore, false))
					for (XMLNode n : node.getNodes())
						if (n.getName().startsWith("name") && event.Name.equals(XMLParser.parseStringNode(n))) {
							Integer ore = OreDictionary.getOreID(XMLParser.parseStringNode(n));

							int id = Item.getIdFromItem(stack.getItem());
							List<Integer> ids = stackToId.get(id);
							if (ids != null)
								ids.remove(ore);
							if (stack.getItemDamage() != OreDictionary.WILDCARD_VALUE)
								id |= stack.getItemDamage() + 1 << 16;
							ids = stackToId.get(id);
							if (ids != null)
								ids.remove(ore);
						}
			}
	}
}