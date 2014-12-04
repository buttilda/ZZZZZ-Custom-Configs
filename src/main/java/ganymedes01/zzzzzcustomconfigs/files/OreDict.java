package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
						ItemStack stack = XMLParser.parseItemStackNode(n);
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
}