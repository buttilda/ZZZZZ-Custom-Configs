package ganymedes01.zzzzzcustomconfigs.files;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.recipes.ShapedFluidRecipe;
import ganymedes01.zzzzzcustomconfigs.recipes.ShapelessFluidRecipe;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingRecipes extends ConfigFile {

	public static final List<IRecipe> addedRecipes = new LinkedList<IRecipe>();

	private static String header = "Examples:\n\n";

	static {
		header += "The following is an example of a shapeless recipe. The number of inputs must not exceed 9 and you must number them (input1, input2, input3...)\n";
		XMLBuilder builder = new XMLBuilder("shapeless");
		builder.makeEntry("output", new ItemStack(Items.iron_ingot, 3));
		builder.makeEntries("input", new Object[] { new ItemStack(Blocks.iron_bars), "gemDiamond", new ItemStack(Blocks.bedrock) });
		header += builder.toString();

		header += "\n\n";
		header += "The following is an example of a shaped recipe. The row paremters determine where the inputs will have to be placed in the grid.\n";
		header += "ALL OF THE ROWs MUST BE OF THE SAME LENGH AND THAT LENGHT MUST BE SMALLER OR EQUAL TO 3! Use spaces where you don't want empty spaces to be!\n";
		header += "You don't necessarily need 3 rows. If your recipe fits in just 1 or 2, use just 1 or 2.\n";

		builder = new XMLBuilder("shaped");
		builder.makeEntry("output", new ItemStack(Items.diamond_sword));
		builder.makeEntries("row", new Object[] { "x z", " x ", " y " });
		builder.makeEntry("x", "stickWood");
		builder.makeEntry("y", new ItemStack(Items.paper));
		builder.makeEntry("z", new ItemStack(Items.skull, 1, OreDictionary.WILDCARD_VALUE));

		header += builder.toString();
	}

	public CraftingRecipes() {
		this("CraftingRecipes", header);
	}

	protected CraftingRecipes(String name, String header) {
		super(name, header);
	}

	protected void addRecipe(IRecipe recipe) {
		addedRecipes.add(recipe);
		GameRegistry.addRecipe(recipe);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes()) {
			ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"), NodeType.OUTPUT);
			if (node.getName().equals("shaped")) {
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
				addRecipe(new ShapedFluidRecipe(output, data.toArray()));
			} else if (node.getName().equals("shapeless")) {
				List<Object> data = new ArrayList<Object>();
				for (int i = 0; i < 9; i++) {
					XMLNode n = node.getNode("input" + (i + 1));
					if (n != null)
						data.add(XMLParser.parseNode(n, NodeType.INPUT));
					else
						break;
				}
				addRecipe(new ShapelessFluidRecipe(output, data.toArray()));
			} else
				throw new IllegalArgumentException("Invalid recipe name: " + node.getName());
		}
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}