package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.lib.StackUtils;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

public class RemoveRecipes extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "The following shows how to recipe recipes for the diamond sword, iron sword and gold sword.\n";
		header += "Smelting recipes will also be removed!\n";
		header += "Recipes added via this mod will NOT be removed!\n";

		XMLNode node = new XMLNode("output1");
		node.setValue(XMLHelper.toNodeValue(new ItemStack(Items.diamond_sword)));
		header += node.toString() + "\n";
		node = new XMLNode("output2");
		node.setValue(XMLHelper.toNodeValue(new ItemStack(Items.golden_sword)));
		header += node.toString() + "\n";
		node = new XMLNode("output3");
		node.setValue(XMLHelper.toNodeValue(new ItemStack(Items.iron_sword)));
		header += node.toString() + "\n";
	}

	public RemoveRecipes() {
		super("RemoveRecipes", header);
	}

	@Override
	public void init() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void postInit() {
		List<IRecipe> crafting = new LinkedList<IRecipe>();
		List<ItemStack> smelting = new LinkedList<ItemStack>();

		for (XMLNode node : xmlNode.getNodes()) {
			ItemStack stack = XMLParser.parseItemStackNode(node, NodeType.N_A);
			for (IRecipe recipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList())
				if (recipe != null && StackUtils.areStacksTheSame(stack, recipe.getRecipeOutput(), true) && !CraftingRecipes.addedRecipes.contains(recipe))
					crafting.add(recipe);

			for (Entry<ItemStack, ItemStack> entry : (Set<Entry<ItemStack, ItemStack>>) FurnaceRecipes.smelting().getSmeltingList().entrySet())
				if (StackUtils.areStacksTheSame(stack, entry.getValue(), true) && !Smelting.addedInputs.contains(entry.getKey()))
					smelting.add(entry.getKey());
		}

		for (IRecipe recipe : crafting)
			CraftingManager.getInstance().getRecipeList().remove(recipe);
		for (ItemStack entry : smelting)
			FurnaceRecipes.smelting().getSmeltingList().remove(entry);
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}