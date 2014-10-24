package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDict extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "<ore>\n";
		header += "\t<name>\"ingots\"</name>\n";
		header += "\t<stack>minecraft:iron_ingot 1 0</stack>\n";
		header += "\t<stack1>minecraft:gold_ingot 1 0</stack1>\n";
		header += "</ore>\n";
		header += "\n";
		header += "<ore>\n";
		header += "\t<name>\"ingotGold\"</name>\n";
		header += "\t<stack>minecraft:gold_ingot 1 0</stack>\n";
		header += "</ore>";
	}

	public OreDict() {
		super("OreDictionary", header);
	}

	@Override
	public void preInit() {
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
	public void init() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}