package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import cpw.mods.fml.common.Loader;

public class RefineryRecipes extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "The following recipe takes lava and water as inputs and creates milk (Obs: Milk isn't a real liquid, unlessyou have a mod that adds it you can't use it)\n";
		XMLBuilder builder = new XMLBuilder("recipe");
		builder.makeEntry("input1", new FluidStack(FluidRegistry.WATER, 1000));
		builder.makeEntry("input2", new FluidStack(FluidRegistry.LAVA, 1000));
		XMLNode node = builder.toNode().addProperty("name", "ExampleRecipe");
		node.addNode(new XMLNode("output").setValue("milk 1000"));
		builder.makeEntry("energy", 120);
		builder.makeEntry("delay", 1);
		header += node.toString();

		header += "\n";
		header += "\n";
		header += "Obs: The parameter \"input2\" is optional. The following recipe takes water and creates lava.\n";

		builder = new XMLBuilder("recipe");
		builder.makeEntry("input1", new FluidStack(FluidRegistry.WATER, 1000));
		builder.makeEntry("output", new FluidStack(FluidRegistry.LAVA, 1000));
		builder.makeEntry("energy", 120);
		builder.makeEntry("delay", 1);
		header += builder.toNode().addProperty("name", "AnotherExample").toString();
	}

	public RefineryRecipes() {
		super("RefineryRecipes", header);
	}

	@Override
	public void preInit() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("recipe")) {
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
		return Loader.isModLoaded("BuildCraft|Energy");
	}
}