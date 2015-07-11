package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.items.ShapelessBloodOrbRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class BloodMagic extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		XMLBuilder builder = new XMLBuilder("altar");
		builder.makeEntry("output", new ItemStack(Items.potato));
		builder.makeEntry("input", new ItemStack(Items.poisonous_potato));
		builder.makeEntry("tier", 1);
		builder.makeEntry("bloodAmount", 1000);
		builder.makeEntry("consumptionRate", 10);
		builder.makeEntry("drainRate", 15);
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("bloodorb");
		builder.makeEntry("output", new ItemStack(Blocks.stone, 4));
		builder.makeEntries("row", new Object[] { " x ", "xyx", " x " });
		builder.makeEntry("x", new ItemStack(Blocks.cobblestone));
		builder.makeEntry("y", new ItemStack(ModItems.weakBloodOrb));
		header += "Blood Orbs:\n";
		header += "AWWayofTime:weakBloodOrb\n";
		header += "AWWayofTime:apprenticeBloodOrb\n";
		header += "AWWayofTime:magicianBloodOrb\n";
		header += "AWWayofTime:masterBloodOrb\n";
		header += "AWWayofTime:archmageBloodOrb\n\n";
		header += builder.toNode().addProperty("type", "shaped").toString() + "\n\n";

		builder = new XMLBuilder("bloodorb");
		builder.makeEntry("output", new ItemStack(Blocks.stone));
		builder.makeEntries("input", new Object[] { new ItemStack(Blocks.cobblestone), new ItemStack(ModItems.weakBloodOrb) });
		header += builder.toNode().addProperty("type", "shapeless").toString() + "\n\n";

		builder = new XMLBuilder("binding");
		builder.makeEntry("output", new ItemStack(Items.golden_apple));
		builder.makeEntry("input", new ItemStack(Items.apple));
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("alchemy");
		builder.makeEntry("output", new ItemStack(Items.golden_apple));
		builder.makeEntry("bloodOrbLevel", 2);
		builder.makeEntry("lp", 200);
		builder.makeEntries("input", new Object[] { new ItemStack(Items.apple), new ItemStack(Blocks.gold_block) });
		header += "The number of inputs should not be larger than 5!\n";
		header += "The \"lp\" value will be multiplied by 100! If you think that's weird ask WayOfTime why.\n";
		header += builder.toString();
	}

	public BloodMagic() {
		super("BloodMagic", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("altar")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);
				int tier = Integer.parseInt(node.getNode("tier").getValue());
				int bloodAmount = Integer.parseInt(node.getNode("bloodAmount").getValue());
				int consumptionRate = Integer.parseInt(node.getNode("consumptionRate").getValue());
				int drainRate = Integer.parseInt(node.getNode("drainRate").getValue());

				AltarRecipeRegistry.registerAltarRecipe(output, input, tier, bloodAmount, consumptionRate, drainRate, false);
			} else if (node.getName().equals("bloodorb")) {
				String prop = node.getProperty("type");
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);
				if (prop.equals("shaped")) {
					List<Object> data = new ArrayList<Object>();
					String types = "";
					for (int i = 0; i < 3; i++) {
						XMLNode n = node.getNode("row" + (i + 1));
						if (n != null) {
							Object obj = XMLParser.parseNode(n, NodeType.N_A);
							types += obj.toString().replace(" ", "");
							data.add(obj);
						}
					}

					for (char c : types.toCharArray()) {
						data.add(c);
						data.add(XMLParser.parseNode(node.getNode(Character.toString(c)), NodeType.INPUT));
					}

					addRecipe(new ShapedBloodOrbRecipe(output, data.toArray()));
				} else if (prop.equals("shapeless")) {
					List<Object> data = new ArrayList<Object>();
					for (int i = 0; i < 9; i++) {
						XMLNode n = node.getNode("input" + (i + 1));
						if (n != null)
							data.add(XMLParser.parseNode(n, NodeType.INPUT));
						else
							break;
					}

					addRecipe(new ShapelessBloodOrbRecipe(output, data.toArray()));
				}
			} else if (node.getName().equals("binding")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"), NodeType.INPUT);
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);

				BindingRegistry.registerRecipe(output, input);
			} else if (node.getName().equals("alchemy")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);
				int time = Integer.parseInt(node.getNode("lp").getValue());
				List<ItemStack> recipe = new LinkedList<ItemStack>();
				int bloodOrbLevel = Integer.parseInt(node.getNode("bloodOrbLevel").getValue());
				for (int i = 0; i < 5; i++) {
					XMLNode n = node.getNode("input" + (i + 1));
					if (n != null)
						recipe.add(XMLParser.parseItemStackNode(n, NodeType.INPUT));
				}

				AlchemyRecipeRegistry.registerRecipe(output, time, recipe.toArray(new ItemStack[0]), bloodOrbLevel);
			} else
				throw new IllegalArgumentException("Invalid recipe name: " + node.getName());
	}

	private void addRecipe(IRecipe recipe) {
		CraftingRecipes.addedRecipes.add(recipe);
		GameRegistry.addRecipe(recipe);
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("AWWayofTime");
	}
}