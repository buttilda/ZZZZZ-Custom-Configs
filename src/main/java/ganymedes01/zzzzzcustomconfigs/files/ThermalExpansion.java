package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ThermalExpansion extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
	}

	public ThermalExpansion() {
		super("ThermalExpansion", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("pulverizer")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				XMLNode n = node.getNode("bonus");
				if (n != null) {
					ItemStack bonus = XMLParser.parseItemStackNode(n);
					int chance = Integer.parseInt(node.getNode("chance").getValue());
					addPulveriserRecipe(energy, input, output, bonus, chance);
				} else
					addPulveriserRecipe(energy, input, output, null, 0);
			} else if (node.getName().equals("inductionsmelter")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input1 = XMLParser.parseItemStackNode(node.getNode("input1"));
				ItemStack input2 = XMLParser.parseItemStackNode(node.getNode("input2"));
				ItemStack output1 = XMLParser.parseItemStackNode(node.getNode("output1"));
				XMLNode n = node.getNode("output2");
				if (n != null) {
					ItemStack output2 = XMLParser.parseItemStackNode(n);
					int chance = Integer.parseInt(node.getNode("chance").getValue());
					addInductionSmelterRecipe(energy, input1, input2, output1, output2, chance);
				} else
					addInductionSmelterRecipe(energy, input1, input2, output1, null, 0);
			} else if (node.getName().equals("magmacruicible")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));
				addMagmaCruicibleRecipe(energy, input, output);
			} else if (node.getName().equals("redstonefurnace")) {
				int energy = Integer.parseInt(node.getNode("energy").getValue());
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				addRedstoneFurnaceRecipe(energy, input, output);
			} else if (node.getName().equals("sawmill")) {

			} else if (node.getName().equals("transposer")) {

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

	private void addMagmaCruicibleRecipe(int energy, ItemStack input, FluidStack output) {
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
}