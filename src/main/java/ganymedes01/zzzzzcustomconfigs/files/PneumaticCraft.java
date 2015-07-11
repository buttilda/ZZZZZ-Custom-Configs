package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.recipe.AssemblyRecipe;
import pneumaticCraft.api.recipe.PressureChamberRecipe;
import cpw.mods.fml.common.Loader;

public class PneumaticCraft extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		XMLBuilder builder = new XMLBuilder("assemblydrill");
		builder.makeEntry("input", new ItemStack(Items.porkchop));
		builder.makeEntry("output", new ItemStack(Items.cooked_porkchop));
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("assemblylaser");
		builder.makeEntry("input", new ItemStack(Items.coal));
		builder.makeEntry("output", new ItemStack(Items.diamond));
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("pressurechamber");
		builder.makeEntries("input", new Object[] { new ItemStack(Blocks.ice), new ItemStack(Blocks.snow), new ItemStack(Items.snowball) });
		builder.makeEntry("pressure", 1.0F);
		builder.makeEntries("output", new Object[] { new ItemStack(Blocks.packed_ice), new ItemStack(Blocks.mycelium) });
		header += builder.toString();
	}

	public PneumaticCraft() {
		super("PneumaticCraft", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("assemblydrill")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);

				AssemblyRecipe.addDrillRecipe(input, output);
			} else if (node.getName().equals("assemblylaser")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);

				AssemblyRecipe.addLaserRecipe(input, output);
			} else if (node.getName().equals("pressurechamber")) {
				ItemStack[] input = getStacks(node, "input", NodeType.INPUT);
				ItemStack[] output = getStacks(node, "output", NodeType.OUTPUT);
				float pressure = Float.parseFloat(node.getNode("pressure").getValue());

				PressureChamberRecipe.chamberRecipes.add(new PressureChamberRecipe(input, pressure, output, false));
			} else
				throw new IllegalArgumentException("Invalid recipe name: " + node.getName());
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("PneumaticCraft");
	}

	private ItemStack[] getStacks(XMLNode node, String name, NodeType type) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		for (XMLNode n : node.getNodes())
			if (n.getName().startsWith(name))
				list.add(XMLParser.parseItemStackNode(n, type));

		return list.toArray(new ItemStack[0]);
	}
}