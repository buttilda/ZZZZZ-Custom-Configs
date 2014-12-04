package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import mods.railcraft.api.crafting.IRockCrusherRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;

public class Railcraft extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "Blast Furnace:\n";
		XMLBuilder builder = new XMLBuilder("blastfurnace");
		builder.makeEntry("input", new ItemStack(Blocks.diamond_ore));
		builder.makeEntry("output", new ItemStack(Items.diamond, 3));
		builder.makeEntry("cookTime", 1200);
		header += builder.toString() + "\n\n";

		header += "Coke Oven:\n";
		builder = new XMLBuilder("cokeoven");
		builder.makeEntry("input", new ItemStack(Blocks.gold_ore));
		builder.makeEntry("output", new ItemStack(Items.gold_ingot));
		builder.makeEntry("cookTime", 100);
		builder.makeEntry("fluidOutput", new FluidStack(FluidRegistry.LAVA, 50));
		header += builder.toString() + "\n\n";

		header += "Rock Crusher (accepts from 1 to 9 outputs, outputs must have a chance (from 0 to 1) parameter):\n";
		builder = new XMLBuilder("rockcrusher");
		builder.makeEntry("input", new ItemStack(Items.brewing_stand));
		XMLNode node = builder.toNode();
		node.addNode(new XMLNode("output1").setValue(XMLHelper.toNodeValue(new ItemStack(Items.blaze_rod))).addProperty("chance", "1.0"));
		node.addNode(new XMLNode("output2").setValue(XMLHelper.toNodeValue(new ItemStack(Blocks.cobblestone))).addProperty("chance", "0.6"));
		node.addNode(new XMLNode("output3").setValue(XMLHelper.toNodeValue(new ItemStack(Blocks.stone))).addProperty("chance", "0.4"));
		header += builder.toString() + "\n\n";

		header += "Rolling Machine (has to be shaped or shapeless. for more information look in the CraftingRecipes file):\n";
		header += "It's also worth noting that the rolling machine DOES NOT support ore dictionary recipes.\n";
		builder = new XMLBuilder("rollingmachine");
		builder.makeEntry("output", new ItemStack(Items.golden_sword));
		builder.makeEntries("row", new Object[] { "x", "x", "y" });
		builder.makeEntry("x", new ItemStack(Items.gold_ingot));
		builder.makeEntry("y", new ItemStack(Items.stick));
		header += builder.toNode().addProperty("type", "shaped").toString() + "\n";

		builder = new XMLBuilder("rollingmachine");
		builder.makeEntry("output", new ItemStack(Blocks.coal_ore));
		builder.makeEntries("input", new Object[] { new ItemStack(Items.coal), new ItemStack(Blocks.stone) });
		header += builder.toNode().addProperty("type", "shapeless").toString();
	}

	public Railcraft() {
		super("Railcraft", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("blastfurnace")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				int burnTime = Integer.parseInt(node.getNode("cookTime").getValue());

				RailcraftCraftingManager.blastFurnace.addRecipe(input, matchMeta(input), matchNBT(input), burnTime, output);
			} else if (node.getName().equals("cokeoven")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				int cookTime = Integer.parseInt(node.getNode("cookTime").getValue());
				FluidStack fluidOutput = XMLParser.parseFluidStackNode(node.getNode("fluidOutput"));

				RailcraftCraftingManager.cokeOven.addRecipe(input, matchMeta(input), matchNBT(input), output, fluidOutput, cookTime);
			} else if (node.getName().equals("rockcrusher")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				IRockCrusherRecipe recipe = RailcraftCraftingManager.rockCrusher.createNewRecipe(input, matchMeta(input), matchNBT(input));
				for (int i = 0; i < 9; i++) {
					XMLNode n = node.getNode("output" + (i + 1));
					if (n != null) {
						ItemStack output = XMLParser.parseItemStackNode(n);
						float chance = Float.parseFloat(n.getProperty("chance"));
						recipe.addOutput(output, chance);
					}
				}
			} else if (node.getName().equals("rollingmachine")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				String type = node.getProperty("type");
				if (type.equals("shaped")) {
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

					RailcraftCraftingManager.rollingMachine.addRecipe(output, data.toArray());
				} else if (type.equals("shapeless")) {
					List<Object> data = new ArrayList<Object>();
					for (int i = 0; i < 9; i++) {
						XMLNode n = node.getNode("input" + (i + 1));
						if (n != null)
							data.add(XMLParser.parseNode(n));
						else
							break;
					}
					RailcraftCraftingManager.rollingMachine.addShapelessRecipe(output, data.toArray());
				}
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
		return Loader.isModLoaded("Railcraft");
	}

	private boolean matchMeta(ItemStack stack) {
		return OreDictionary.WILDCARD_VALUE != stack.getItemDamage();
	}

	private boolean matchNBT(ItemStack stack) {
		return stack.hasTagCompound();
	}
}