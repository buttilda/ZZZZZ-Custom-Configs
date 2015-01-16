package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;

import java.util.Map.Entry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import cpw.mods.fml.common.Loader;

public class Thaumcraft extends ConfigFile {

	private static String header = "Examples:\n";
	static {
		header += "Aspects: ";
		for (Entry<String, Aspect> entry : Aspect.aspects.entrySet())
			header += entry.getKey() + ", ";
		header = header.substring(0, header.length() - 2);

		header += "\n\nThere is not limit to the number of aspects you add to an item (unless Thaumcraft has one...)\n\n";

		XMLBuilder builder = new XMLBuilder("aspects");
		builder.makeEntry("stack", new ItemStack(Items.diamond));
		XMLNode node = builder.toNode();
		node.addNode(new XMLNode("aspect1").setValue("terra 1"));
		node.addNode(new XMLNode("aspect2").setValue("aer 5"));
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("aspects");
		builder.makeEntry("stack", "gemDiamond");
		node = builder.toNode();
		node.addNode(new XMLNode("aspect1").setValue("terra 1"));
		node.addNode(new XMLNode("aspect2").setValue("aer 5"));
		header += builder.toString() + "\n\n";
	}

	public Thaumcraft() {
		super("Thaumcraft", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("aspects")) {
				Object input = XMLParser.parseNode(node.getNode("stack"), NodeType.N_A);
				AspectList aspects = new AspectList();
				for (XMLNode n : node.getNodes())
					if (n.getName().startsWith("aspect")) {
						String[] data = n.getValue().split(" ");
						aspects.add(Aspect.getAspect(data[0]), Integer.parseInt(data[1]));
					}

				if (input instanceof ItemStack)
					ThaumcraftApi.registerComplexObjectTag((ItemStack) input, aspects);
				else if (input instanceof String)
					ThaumcraftApi.registerObjectTag((String) input, aspects);
				else
					throw new IllegalArgumentException("Invalid object type. Must be ItemStack or String");
			}
	}

	@Override
	public void postInit() {
	}

	@Override
	public void serverStarting() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("Thaumcraft");
	}
}