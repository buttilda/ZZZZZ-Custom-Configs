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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import cpw.mods.fml.common.Loader;

public class Buildcraft extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "The following recipe takes lava and water as inputs and creates milk (Obs: Milk isn't a real liquid, unlessyou have a mod that adds it you can't use it)\n";
		XMLBuilder builder = new XMLBuilder("refinery");
		builder.makeEntry("input1", new FluidStack(FluidRegistry.WATER, 1000));
		builder.makeEntry("input2", new FluidStack(FluidRegistry.LAVA, 1000));
		XMLNode node = builder.toNode().addProperty("name", "ExampleRecipe");
		node.addNode(new XMLNode("output").setValue("milk 1000"));
		builder.makeEntry("energy", 120);
		builder.makeEntry("delay", 1);
		header += node.toString() + "\n\n";

		header += "Obs: The parameter \"input2\" is optional. The following recipe takes water and creates lava.\n";
		builder = new XMLBuilder("refinery");
		builder.makeEntry("input1", new FluidStack(FluidRegistry.WATER, 1000));
		builder.makeEntry("output", new FluidStack(FluidRegistry.LAVA, 1000));
		builder.makeEntry("energy", 120);
		builder.makeEntry("delay", 1);
		header += builder.toNode().addProperty("name", "AnotherExample").toString() + "\n\n";

		builder = new XMLBuilder("assemblytable");
		builder.makeEntry("energyCost", 10000);
		builder.makeEntry("output", new ItemStack(Blocks.bedrock));
		builder.makeEntries("input", new Object[] { new ItemStack(Blocks.stone), "gemEmerald", new ItemStack(Items.golden_apple) });
		header += builder.toNode().addProperty("name", "AssemblyExample").toString();
	}

	public Buildcraft() {
		super("Buildcraft", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("refinery")) {
				String id = node.getProperty("name");
				FluidStack input1 = XMLParser.parseFluidStackNode(node.getNode("input1"));
				FluidStack input2 = null;
				XMLNode n = node.getNode("input2");
				if (n != null)
					input2 = XMLParser.parseFluidStackNode(node.getNode("input2"));
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				int delay = Integer.parseInt(node.getNode("delay").getValue());

				if (input2 != null)
					BuildcraftRecipeRegistry.refinery.addRecipe(id, input1, input2, output, energy, delay);
				else
					BuildcraftRecipeRegistry.refinery.addRecipe(id, input1, output, energy, delay);
			} else if (node.getName().equals("assemblytable")) {
				String id = node.getProperty("name");
				int energyCost = Integer.parseInt(node.getNode("energyCost").getValue());
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);
				List<Object> inputs = new ArrayList<Object>();
				for (XMLNode n : node.getNodes())
					if (n.getName().startsWith("input"))
						inputs.add(XMLParser.parseNode(n, NodeType.INPUT));
				BuildcraftRecipeRegistry.assemblyTable.addRecipe(id, energyCost, output, inputs.toArray());
			} else
				throw new IllegalArgumentException("Invalid recipe name: " + node.getName());
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("BuildCraft|Energy");
	}
}