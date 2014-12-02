package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.Loader;

public class IndustrialCraft2 extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "All of the inputs that are not fluids can be either an item or a ore dictionary name!\n\n";
		header += "The following examples are valid for: macerator, extractor, compressor, metalformerExtruding, metalformerCutting, metalformerRolling\n";

		XMLBuilder builder = new XMLBuilder("macerator");
		builder.makeEntry("input", "oreDiamond");
		builder.makeEntry("output", new ItemStack(Items.diamond));
		header += builder.toString() + "\n";

		builder = new XMLBuilder("compressor");
		builder.makeEntry("input", new ItemStack(Items.diamond, 9));
		builder.makeEntry("output", new ItemStack(Blocks.diamond_block));
		header += builder.toString() + "\n\n";

		header += "The following is valid for only the Thermal Centrifuge. There can be from 1 to 3 outputs.\n";
		builder = new XMLBuilder("centrifuge");
		builder.makeEntry("input", "oreDiamond");
		builder.makeEntry("minHeat", 1000);
		builder.makeEntries("output", new Object[] { new ItemStack(Items.diamond), new ItemStack(Blocks.cobblestone) });
		header += builder.toString() + "\n\n";

		header += "The following is valid for only the Block Cutter.\n";
		builder = new XMLBuilder("blockcutter");
		builder.makeEntry("input", "blockDiamond");
		builder.makeEntry("cutterLevel", 3);
		builder.makeEntry("output", new ItemStack(Items.diamond, 9));
		header += builder.toString() + "\n\n";

		header += "The following is valid for only the Blast Furnace. There can be 1 or 2 outputs.\n";
		builder = new XMLBuilder("blastfurance");
		builder.makeEntry("input", "oreDiamond");
		builder.makeEntries("output", new Object[] { new ItemStack(Items.diamond) });
		header += builder.toString() + "\n\n";

		header += "The following is valid for only the Ore Washing Plant. There can be from 1 to 3 outputs.\n";
		builder = new XMLBuilder("oreWashing");
		builder.makeEntry("input", "crushedIron");
		builder.makeEntries("output", new Object[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_nugget) });
		header += builder.toString() + "\n\n";

		header += "The following is valid for only the Canner in bottling mode.\n";
		builder = new XMLBuilder("cannerBottle");
		builder.makeEntry("input1", new ItemStack(Items.bucket));
		builder.makeEntry("input2", new ItemStack(Items.magma_cream));
		builder.makeEntry("output", new ItemStack(Items.lava_bucket));
		header += builder.toString() + "\n\n";

		header += "The following is valid for only the Canner in enrich mode. Input and output must be a fluid, and the additive is an item\n";
		builder = new XMLBuilder("cannerEnrich");
		builder.makeEntry("input", new FluidStack(FluidRegistry.WATER, 1000));
		builder.makeEntry("output", new FluidStack(FluidRegistry.LAVA, 500));
		builder.makeEntry("additive", new ItemStack(Items.magma_cream));
		header += builder.toString();
	}

	public IndustrialCraft2() {
		super("IC2", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes()) {
			String name = node.getName();
			if (name.equals("macerator"))
				addBasic(Recipes.macerator, node);
			else if (name.equals("extractor"))
				addBasic(Recipes.extractor, node);
			else if (name.equals("compressor"))
				addBasic(Recipes.compressor, node);
			else if (name.equals("centrifuge")) {
				IRecipeInput input = getInput(node.getNode("input"));
				int minHeat = Integer.parseInt(node.getNode("minHeat").getValue());
				List<ItemStack> outputs = new LinkedList<ItemStack>();
				for (int i = 0; i < 3; i++) {
					XMLNode n = node.getNode("output" + (i + 1));
					if (n != null)
						outputs.add(XMLParser.parseItemStackNode(n));
				}
				addThermalCentrifugeRecipe(input, minHeat, outputs.toArray(new ItemStack[0]));
			} else if (name.equals("blockcutter"))
				addCutterRecipe(getInput(node.getNode("input")), Integer.parseInt(node.getNode("cutterLevel").getValue()), XMLParser.parseItemStackNode(node.getNode("output")));
			else if (name.equals("blastfurance"))
				Recipes.blastfurance.addRecipe(getInput(node.getNode("input")), null, getArray(node, "output", 2));
			else if (name.equals("metalformerExtruding"))
				addBasic(Recipes.metalformerExtruding, node);
			else if (name.equals("metalformerCutting"))
				addBasic(Recipes.metalformerCutting, node);
			else if (name.equals("metalformerRolling"))
				addBasic(Recipes.metalformerRolling, node);
			else if (name.equals("oreWashing"))
				addOreWashingRecipe(getInput(node.getNode("input")), getArray(node, "output", 3));
			else if (name.equals("cannerBottle")) {
				IRecipeInput input1 = getInput(node.getNode("input1"));
				IRecipeInput input2 = getInput(node.getNode("input2"));
				Recipes.cannerBottle.addRecipe(input1, input2, XMLParser.parseItemStackNode(node.getNode("output")));
			} else if (name.equals("cannerEnrich")) {
				FluidStack input = XMLParser.parseFluidStackNode(node.getNode("input"));
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));
				IRecipeInput additive = getInput(node.getNode("additive"));
				Recipes.cannerEnrich.addRecipe(input, additive, output);
			}
		}
	}

	@Override
	public void preInit() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("IC2");
	}

	private void addBasic(IMachineRecipeManager machine, XMLNode node) {
		machine.addRecipe(getInput(node.getNode("input")), null, XMLParser.parseItemStackNode(node.getNode("output")));
	}

	private void addThermalCentrifugeRecipe(IRecipeInput input, int minHeat, ItemStack... output) {
		NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("minHeat", minHeat);

		Recipes.centrifuge.addRecipe(input, metadata, output);
	}

	private void addOreWashingRecipe(IRecipeInput input, ItemStack... output) {
		NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("amount", 1000);

		Recipes.oreWashing.addRecipe(input, metadata, output);
	}

	public void addCutterRecipe(IRecipeInput input, int cutterlevel, ItemStack output) {
		NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("hardness", cutterlevel);

		Recipes.blockcutter.addRecipe(input, metadata, output);
	}

	private IRecipeInput getInput(XMLNode node) {
		if (XMLParser.isItemStackValue(node.getValue()))
			return new RecipeInputItemStack(XMLParser.parseItemStackNode(node));
		else {
			String value = node.getValue().replace("\"", "");
			String[] array = value.split(" ");
			if (array.length > 1)
				return new RecipeInputOreDict(array[0], Integer.parseInt(array[1]));
			else
				return new RecipeInputOreDict(value);
		}
	}

	private ItemStack[] getArray(XMLNode node, String name, int max) {
		List<ItemStack> list = new LinkedList<ItemStack>();
		for (int i = 0; i < max; i++) {
			XMLNode n = node.getNode(name + (i + 1));
			if (n != null)
				list.add(XMLParser.parseItemStackNode(n));
		}

		return list.toArray(new ItemStack[0]);
	}
}