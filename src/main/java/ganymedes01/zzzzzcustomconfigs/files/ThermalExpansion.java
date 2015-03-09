package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ThermalExpansion extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "NO ORE DICTIONARY INPUTS ALLOWED!\n\n";

		header += "The following shows how to add a to the Pulverizer\n";
		XMLBuilder builder = new XMLBuilder("pulverizer");
		builder.makeEntry("input", new ItemStack(Blocks.cobblestone));
		builder.makeEntry("output", new ItemStack(Blocks.gravel));
		builder.makeEntry("energy", 10000);
		header += builder.toString() + "\n";
		header += "You can also add recipes that have a bonus output that is yielded with a certain chance (between 1 and 100)\n";
		builder = new XMLBuilder("pulverizer");
		builder.makeEntry("input", new ItemStack(Blocks.cobblestone));
		builder.makeEntry("output", new ItemStack(Blocks.gravel));
		builder.makeEntry("bonus", new ItemStack(Items.diamond));
		builder.makeEntry("chance", 5);
		builder.makeEntry("energy", 10000);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add a to the Redstone Furnace\n";
		builder = new XMLBuilder("redstonefurnace");
		builder.makeEntry("input", new ItemStack(Blocks.cobblestone));
		builder.makeEntry("output", new ItemStack(Blocks.stone));
		builder.makeEntry("energy", 1000);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add a to the Sawmill\n";
		builder = new XMLBuilder("sawmill");
		builder.makeEntry("input", new ItemStack(Blocks.planks));
		builder.makeEntry("output1", new ItemStack(Items.stick));
		builder.makeEntry("energy", 1000);
		header += builder.toString() + "\n";
		header += "You can also add recipes that have a secondary output that is yielded with a certain chance (between 1 and 100)\n";
		builder = new XMLBuilder("sawmill");
		builder.makeEntry("input", new ItemStack(Blocks.planks));
		builder.makeEntry("output1", new ItemStack(Items.stick));
		builder.makeEntry("output2", new ItemStack(Items.stick));
		builder.makeEntry("chance", 100);
		builder.makeEntry("energy", 10000);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add recipes to the Induction Smelter\n";
		builder = new XMLBuilder("inductionsmelter");
		builder.makeEntry("input1", new ItemStack(Items.wheat));
		builder.makeEntry("input2", new ItemStack(Items.sugar));
		builder.makeEntry("output1", new ItemStack(Items.cookie));
		builder.makeEntry("energy", 5000);
		header += builder.toString() + "\n";
		header += "You can also add recipes that have a bonus output that is yielded with a certain chance (between 1 and 100)\n";
		builder = new XMLBuilder("inductionsmelter");
		builder.makeEntry("input1", new ItemStack(Items.wheat));
		builder.makeEntry("input2", new ItemStack(Items.sugar));
		builder.makeEntry("output1", new ItemStack(Items.cookie));
		builder.makeEntry("output2", new ItemStack(Items.baked_potato));
		builder.makeEntry("chance", 25);
		builder.makeEntry("energy", 5000);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add recipes to the Magma Crucible\n";
		builder = new XMLBuilder("magmacrucible");
		builder.makeEntry("input", new ItemStack(Blocks.packed_ice));
		builder.makeEntry("output", new FluidStack(FluidRegistry.WATER, 2000));
		builder.makeEntry("energy", 25000);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add recipes to the Fluid Transposer\n";
		header += "This shows a recipe where the transposer fills an empty bucket with 1000mB of water and turns it into a water bucket\n";
		builder = new XMLBuilder("transposer");
		builder.makeEntry("input", new ItemStack(Items.bucket));
		builder.makeEntry("output", new ItemStack(Items.water_bucket));
		builder.makeEntry("fluid", new FluidStack(FluidRegistry.WATER, 1000));
		builder.makeEntry("energy", 200);
		header += builder.toNode().addProperty("type", "fill").toString() + "\n\n";
		header += "This shows a recipe where the transposer extracts 1000mB of water from a water bucket and yields an empty bucket with 100% chance\n";
		builder = new XMLBuilder("transposer");
		builder.makeEntry("input", new ItemStack(Items.water_bucket));
		builder.makeEntry("output", new ItemStack(Items.bucket));
		builder.makeEntry("fluid", new FluidStack(FluidRegistry.WATER, 1000));
		builder.makeEntry("energy", 200);
		builder.makeEntry("chance", 100);
		header += builder.toNode().addProperty("type", "extract").toString() + "\n\n";

		header += "The following shows how to add a fuel to the Magmatic Dynamo\n";
		builder = new XMLBuilder("magmaticfuel");
		builder.makeEntry("fuelName", "lava");
		builder.makeEntry("energy", 10);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add a fuel to the Compression Dynamo\n";
		builder = new XMLBuilder("compressionfuel");
		builder.makeEntry("fuelName", "lava");
		builder.makeEntry("energy", 10);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add a fuel to the Reactant Dynamo\n";
		builder = new XMLBuilder("reactantfuel");
		builder.makeEntry("fuelName", "lava");
		builder.makeEntry("energy", 10);
		header += builder.toString() + "\n\n";

		header += "The following shows how to add a coolant\n";
		builder = new XMLBuilder("coolant");
		builder.makeEntry("fuelName", "water");
		builder.makeEntry("energy", 10);
		header += builder.toString();
	}

	public ThermalExpansion() {
		super("ThermalExpansion", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("pulverizer")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.INPUT);
				XMLNode n = node.getNode("bonus");
				if (n != null) {
					ItemStack bonus = XMLParser.parseItemStackNode(n, NodeType.OUTPUT);
					int chance = Integer.parseInt(node.getNode("chance").getValue());
					addPulveriserRecipe(energy, input, output, bonus, chance);
				} else
					addPulveriserRecipe(energy, input, output, null, 0);
			} else if (node.getName().equals("inductionsmelter")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input1 = XMLParser.parseItemStackNode(node.getNode("input1"), NodeType.INPUT);
				ItemStack input2 = XMLParser.parseItemStackNode(node.getNode("input2"), NodeType.INPUT);
				ItemStack output1 = XMLParser.parseItemStackNode(node.getNode("output1"), NodeType.OUTPUT);
				XMLNode n = node.getNode("output2");
				if (n != null) {
					ItemStack output2 = XMLParser.parseItemStackNode(n, NodeType.OUTPUT);
					int chance = Integer.parseInt(node.getNode("chance").getValue());
					addInductionSmelterRecipe(energy, input1, input2, output1, output2, chance);
				} else
					addInductionSmelterRecipe(energy, input1, input2, output1, null, 0);
			} else if (node.getName().equals("magmacrucible")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));
				addMagmaCrucibleRecipe(energy, input, output);
			} else if (node.getName().equals("redstonefurnace")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.INPUT);
				addRedstoneFurnaceRecipe(energy, input, output);
			} else if (node.getName().equals("sawmill")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output1"), NodeType.OUTPUT);
				XMLNode n = node.getNode("output2");
				if (n != null) {
					ItemStack bonus = XMLParser.parseItemStackNode(n, NodeType.OUTPUT);
					int chance = Integer.parseInt(node.getNode("chance").getValue());
					addSawmillRecipe(energy, input, output, bonus, chance);
				} else
					addSawmillRecipe(energy, input, output, null, 0);
			} else if (node.getName().equals("transposer")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.INPUT);
				FluidStack fluid = XMLParser.parseFluidStackNode(node.getNode("fluid"));
				String type = node.getProperty("type");
				if (type.equals("fill"))
					addTransposerFill(energy, input, output, fluid);
				else if (type.equals("extract")) {
					int chance = Integer.parseInt(node.getNode("chance").getValue());
					addTransposerExtract(energy, input, output, chance, fluid);
				} else
					throw new IllegalArgumentException("Invalid transposer recipe type: " + type);
			} else if (node.getName().equals("magmaticfuel")) {
				String fuelName = XMLParser.parseStringNode(node.getNode("fuelName"));
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				addFuel("MagmaticFuel", fuelName, energy);
			} else if (node.getName().equals("compressionfuel")) {
				String fuelName = XMLParser.parseStringNode(node.getNode("fuelName"));
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				addFuel("CompressionFuel", fuelName, energy);
			} else if (node.getName().equals("reactantfuel")) {
				String fuelName = XMLParser.parseStringNode(node.getNode("fuelName"));
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				addFuel("ReactantFuel", fuelName, energy);
			} else if (node.getName().equals("coolant")) {
				String fuelName = XMLParser.parseStringNode(node.getNode("fuelName"));
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				addFuel("Coolant", fuelName, energy);
			} else
				throw new IllegalArgumentException("Invalid recipe name: " + node.getName());
	}

	@Override
	public void postInit() {
	}

	@Override
	public void serverStarting() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("ThermalExpansion");
	}

	private void addPulveriserRecipe(int energy, ItemStack input, ItemStack output, ItemStack bonus, int chance) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);

		NBTTagCompound inputCompound = new NBTTagCompound();
		input.writeToNBT(inputCompound);
		data.setTag("input", inputCompound);

		NBTTagCompound outputCompound = new NBTTagCompound();
		output.writeToNBT(outputCompound);
		data.setTag("primaryOutput", outputCompound);

		if (bonus != null) {
			NBTTagCompound outputCompound2 = new NBTTagCompound();
			bonus.writeToNBT(outputCompound2);
			data.setTag("secondaryOutput", outputCompound2);

			data.setInteger("secondaryChance", chance);
		}

		FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", data);
	}

	private void addInductionSmelterRecipe(int energy, ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, int chance) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);

		NBTTagCompound input1Compound = new NBTTagCompound();
		input1.writeToNBT(input1Compound);
		data.setTag("primaryInput", input1Compound);

		NBTTagCompound input2Compound = new NBTTagCompound();
		input2.writeToNBT(input2Compound);
		data.setTag("secondaryInput", input2Compound);

		NBTTagCompound output1Compound = new NBTTagCompound();
		output1.writeToNBT(output1Compound);
		data.setTag("primaryOutput", output1Compound);

		if (output2 != null) {
			NBTTagCompound output2Compound = new NBTTagCompound();
			output2.writeToNBT(output2Compound);
			data.setTag("secondaryOutput", output2Compound);

			data.setInteger("secondaryChance", chance);
		}

		FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", data);
	}

	private void addMagmaCrucibleRecipe(int energy, ItemStack input, FluidStack output) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);

		NBTTagCompound inputCompound = new NBTTagCompound();
		input.writeToNBT(inputCompound);
		data.setTag("input", inputCompound);

		NBTTagCompound outputCompound = new NBTTagCompound();
		output.writeToNBT(outputCompound);
		data.setTag("output", outputCompound);

		FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", data);
	}

	private void addRedstoneFurnaceRecipe(int energy, ItemStack input, ItemStack output) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);

		NBTTagCompound inputCompound = new NBTTagCompound();
		input.writeToNBT(inputCompound);
		data.setTag("input", inputCompound);

		NBTTagCompound outputCompound = new NBTTagCompound();
		output.writeToNBT(outputCompound);
		data.setTag("output", outputCompound);

		FMLInterModComms.sendMessage("ThermalExpansion", "FurnaceRecipe", data);
	}

	private void addFuel(String key, String fluidName, int energy) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);

		NBTTagCompound fluidCompound = new NBTTagCompound();
		fluidCompound.setString("fluidName", fluidName);
		data.setTag("input", fluidCompound);

		FMLInterModComms.sendMessage("ThermalExpansion", key, data);
	}

	private void addTransposerFill(int energy, ItemStack input, ItemStack output, FluidStack fluid) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);

		NBTTagCompound inputCompound = new NBTTagCompound();
		input.writeToNBT(inputCompound);
		data.setTag("input", inputCompound);

		NBTTagCompound outputCompound = new NBTTagCompound();
		output.writeToNBT(outputCompound);
		data.setTag("output", outputCompound);

		NBTTagCompound fluidCompound = new NBTTagCompound();
		fluid.writeToNBT(fluidCompound);
		data.setTag("fluid", fluidCompound);

		FMLInterModComms.sendMessage("ThermalExpansion", "TransposerFillRecipe", data);
	}

	private void addTransposerExtract(int energy, ItemStack input, ItemStack output, int chance, FluidStack fluid) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);
		data.setInteger("chance", chance);

		NBTTagCompound inputCompound = new NBTTagCompound();
		input.writeToNBT(inputCompound);
		data.setTag("input", inputCompound);

		NBTTagCompound outputCompound = new NBTTagCompound();
		output.writeToNBT(outputCompound);
		data.setTag("output", outputCompound);

		NBTTagCompound fluidCompound = new NBTTagCompound();
		fluid.writeToNBT(fluidCompound);
		data.setTag("fluid", fluidCompound);

		FMLInterModComms.sendMessage("ThermalExpansion", "TransposerExtractRecipe", data);
	}

	private void addSawmillRecipe(int energy, ItemStack input, ItemStack output1, ItemStack output2, int chance) {
		NBTTagCompound data = new NBTTagCompound();

		data.setInteger("energy", energy);

		NBTTagCompound inputCompound = new NBTTagCompound();
		input.writeToNBT(inputCompound);
		data.setTag("input", inputCompound);

		NBTTagCompound outputCompound = new NBTTagCompound();
		output1.writeToNBT(outputCompound);
		data.setTag("primaryOutput", outputCompound);

		if (output2 != null) {
			NBTTagCompound outputCompound2 = new NBTTagCompound();
			output2.writeToNBT(outputCompound2);
			data.setTag("secondaryOutput", outputCompound2);

			data.setInteger("secondaryChance", chance);
		}

		FMLInterModComms.sendMessage("ThermalExpansion", "SawmillRecipe", data);
	}
}