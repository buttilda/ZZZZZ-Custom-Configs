package ganymedes01.zzzzzcustomconfigs.files;

import forestry.api.fuels.EngineBronzeFuel;
import forestry.api.fuels.EngineCopperFuel;
import forestry.api.fuels.FuelManager;
import forestry.api.recipes.RecipeManagers;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.Loader;

public class Forestry extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		XMLBuilder builder = new XMLBuilder("carpenter");
		builder.makeEntry("box", new ItemStack(Blocks.planks));
		builder.makeEntry("time", 100);
		builder.makeEntry("output", new ItemStack(Items.apple));
		builder.makeEntry("fluid", new FluidStack(FluidRegistry.WATER, 1));
		builder.makeEntries("row", new Object[] { " x ", "yyy", " x " });
		builder.makeEntry("x", "ingotCopper");
		builder.makeEntry("y", new ItemStack(Items.gold_nugget));
		header += "The box and the fluid parameters are optional!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("centrifuge");
		builder.makeEntry("time", 100);
		builder.makeEntry("input", new ItemStack(Items.golden_apple));
		builder.toNode().addNode(new XMLNode("output1").addProperty("chance", "50").setValue(XMLHelper.toNodeValue(new ItemStack(Items.apple))));
		builder.toNode().addNode(new XMLNode("output2").addProperty("chance", "10").setValue(XMLHelper.toNodeValue(new ItemStack(Items.gold_ingot, 8))));
		header += "Up to 9 different outputs are allowed!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("fabricator");
		builder.makeEntry("input", new ItemStack(Blocks.stained_glass));
		builder.makeEntry("outputAmount", 500);
		builder.makeEntry("meltingPoint", 2000);
		header += builder.toNode().addProperty("type", "smelting").toString() + "\n\n";

		builder = new XMLBuilder("fabricator");
		builder.makeEntry("cast", new ItemStack(Items.boat));
		builder.makeEntry("output", new ItemStack(Items.diamond_sword));
		builder.makeEntries("row", new Object[] { "x", "x", "y" });
		builder.makeEntry("x", new ItemStack(Items.diamond));
		builder.makeEntry("y", "stickWood");
		header += "The cast parameter is optional!\n";
		header += builder.toNode().addProperty("type", "recipe").toString() + "\n\n";

		builder = new XMLBuilder("fermenter");
		builder.makeEntry("fermentationValue", 250);
		builder.makeEntry("input", new ItemStack(Items.golden_apple));
		builder.makeEntry("output", new FluidStack(FluidRegistry.LAVA, 1000));
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("moistener");
		builder.makeEntry("time", 100);
		builder.makeEntry("input", new ItemStack(Items.apple));
		builder.makeEntry("output", new ItemStack(Items.golden_apple, 1, 1));
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("squeezer");
		builder.makeEntry("time", 100);
		builder.makeEntry("input", new ItemStack(Items.apple));
		builder.makeEntry("output", new FluidStack(FluidRegistry.WATER, 100));
		builder.toNode().addNode(new XMLNode("remnant").addProperty("chance", "50").setValue(XMLHelper.toNodeValue(new ItemStack(Items.potato))));
		header += "The remnant parameter is optional!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("still");
		builder.makeEntry("time", 100);
		builder.makeEntry("input", new FluidStack(FluidRegistry.LAVA, 1));
		builder.makeEntry("output", new FluidStack(FluidRegistry.WATER, 1));
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("biogasengine");
		builder.makeEntry("fluid", new FluidStack(FluidRegistry.WATER, 0));
		builder.makeEntry("powerPerCycle", 20);
		builder.makeEntry("burnDuration", 1000);
		builder.makeEntry("dissipationMultiplier", 1);
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("peatengine");
		builder.makeEntry("fuel", new ItemStack(Items.coal));
		builder.makeEntry("powerPerCycle", 20);
		builder.makeEntry("burnDuration", 400);
		header += builder.toString();
	}

	public Forestry() {
		super("Forestry", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("carpenter")) {
				ItemStack box = getItemStack(node, "box");
				int time = Integer.parseInt(node.getNode("time").getValue());
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				FluidStack fluid = getFluidStack(node, "fluid");

				if (fluid != null)
					RecipeManagers.carpenterManager.addRecipe(time, fluid, box, output, getArray(node));
				else
					RecipeManagers.carpenterManager.addRecipe(time, box, output, getArray(node));
			} else if (node.getName().equals("centrifuge")) {
				int time = Integer.parseInt(node.getNode("time").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				HashMap<ItemStack, Integer> outputs = new HashMap<ItemStack, Integer>();
				for (int i = 0; i < 9; i++) {
					XMLNode n = node.getNode("output" + (i + 1));
					if (n != null)
						outputs.put(XMLParser.parseItemStackNode(n), Integer.parseInt(n.getProperty("chance")));
				}

				RecipeManagers.centrifugeManager.addRecipe(time, input, outputs);
			} else if (node.getName().equals("fabricator")) {
				if (node.getProperty("type").equals("smelting")) {
					ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
					int outputAmount = Integer.parseInt(node.getNode("outputAmount").getValue());
					int meltingPoint = Integer.parseInt(node.getNode("meltingPoint").getValue());

					RecipeManagers.fabricatorManager.addSmelting(input, makeFluid("glass", outputAmount), meltingPoint);
				} else if (node.getProperty("type").equals("recipe")) {
					ItemStack cast = getItemStack(node, "cast");
					ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));

					RecipeManagers.fabricatorManager.addRecipe(cast, makeFluid("glass", 500), output, getArray(node));
				}
			} else if (node.getName().equals("fermenter")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				int fermentationValue = Integer.parseInt(node.getNode("fermentationValue").getValue());
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));

				addFermenterRecipe(input, fermentationValue, output);
			} else if (node.getName().equals("moistener")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				int time = Integer.parseInt(node.getNode("time").getValue());

				RecipeManagers.moistenerManager.addRecipe(input, output, time);
			} else if (node.getName().equals("squeezer")) {
				int time = Integer.parseInt(node.getNode("time").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));
				ItemStack remnant = XMLParser.parseItemStackNode(node.getNode("remnant"));
				int chance = Integer.parseInt(node.getNode("remnant").getProperty("chance"));

				if (remnant != null)
					RecipeManagers.squeezerManager.addRecipe(time, new ItemStack[] { input }, output, remnant, chance);
				else
					RecipeManagers.squeezerManager.addRecipe(time, new ItemStack[] { input }, output);
			} else if (node.getName().equals("still")) {
				FluidStack input = XMLParser.parseFluidStackNode(node.getNode("input"));
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));
				int time = Integer.parseInt(node.getNode("time").getValue());

				RecipeManagers.stillManager.addRecipe(time, input, output);
			} else if (node.getName().equals("biogasengine")) {
				Fluid fluid = XMLParser.parseFluidStackNode(node.getNode("fluid")).getFluid();
				int powerPerCycle = Integer.parseInt(node.getNode("powerPerCycle").getValue());
				int burnDuration = Integer.parseInt(node.getNode("burnDuration").getValue());
				int dissipationMultiplier = Integer.parseInt(node.getNode("dissipationMultiplier").getValue());

				FuelManager.bronzeEngineFuel.put(fluid, new EngineBronzeFuel(fluid, powerPerCycle, burnDuration, dissipationMultiplier));
			} else if (node.getName().equals("peatengine")) {
				ItemStack fuel = XMLParser.parseItemStackNode(node.getNode("fuel"));
				int powerPerCycle = Integer.parseInt(node.getNode("powerPerCycle").getValue());
				int burnDuration = Integer.parseInt(node.getNode("burnDuration").getValue());

				FuelManager.copperEngineFuel.put(fuel, new EngineCopperFuel(fuel, powerPerCycle, burnDuration));
			} else
				throw new RuntimeException("Invalid recipe name: " + node.getName());
	}

	@Override
	public void preInit() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("Forestry");
	}

	private ItemStack getItemStack(XMLNode node, String name) {
		XMLNode n = node.getNode(name);
		return n == null ? null : XMLParser.parseItemStackNode(n);
	}

	private FluidStack getFluidStack(XMLNode node, String name) {
		XMLNode n = node.getNode(name);
		return n == null ? null : XMLParser.parseFluidStackNode(n);
	}

	private Object[] getArray(XMLNode node) {
		List<Object> data = new ArrayList<Object>();
		String types = "";
		for (int i = 0; i < 3; i++) {
			XMLNode n = node.getNode("row" + (i + 1));
			if (n != null) {
				Object obj = XMLParser.parseNode(n);
				types += obj.toString().replace(" ", "");
				data.add(obj);
			}
		}
		for (char c : types.toCharArray()) {
			data.add(c);
			data.add(XMLParser.parseNode(node.getNode(Character.toString(c))));
		}

		return data.toArray();
	}

	private void addFermenterRecipe(ItemStack resource, int fermentationValue, FluidStack output) {
		RecipeManagers.fermenterManager.addRecipe(resource, fermentationValue, 1.0F, output, makeFluid("water", 1));
		if (FluidRegistry.isFluidRegistered("juice"))
			RecipeManagers.fermenterManager.addRecipe(resource, fermentationValue, 1.5F, output, makeFluid("juice", 1));
		if (FluidRegistry.isFluidRegistered("honey"))
			RecipeManagers.fermenterManager.addRecipe(resource, fermentationValue, 1.5F, output, makeFluid("honey", 1));
	}

	private FluidStack makeFluid(String name, int amount) {
		return new FluidStack(FluidRegistry.getFluid(name), amount);
	}
}