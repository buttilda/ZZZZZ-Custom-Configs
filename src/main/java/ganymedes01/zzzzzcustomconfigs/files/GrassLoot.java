package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class GrassLoot extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "The following shows how to add potatoes and carrots as loot from breaking tall grass.\n";
		header += "The bigger the weight value the more common the loot will be. The default weight (used by the wheat seeds) is 10.\n\n";

		XMLBuilder builder = new XMLBuilder("GrassLoot");
		XMLNode node = new XMLNode("loot0");
		node.setValue(XMLHelper.toNodeValue(new ItemStack(Items.potato)));
		node.addProperty("weight", 10);
		builder.toNode().addNode(node);

		node = new XMLNode("loot1");
		node.setValue(XMLHelper.toNodeValue(new ItemStack(Items.carrot)));
		node.addProperty("weight", 3);
		builder.toNode().addNode(node);

		header += builder.toString() + "\n\n";
	}

	public GrassLoot() {
		super("GrassLoot", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes()) {
			ItemStack stack = XMLParser.parseItemStackNode(node, NodeType.OUTPUT);
			int weight = Integer.parseInt(node.getProperty("weight"));
			MinecraftForge.addGrassSeed(stack, weight);
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