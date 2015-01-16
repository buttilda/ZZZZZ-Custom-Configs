package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
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
import net.minecraftforge.oredict.OreDictionary;

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
	public void postInit() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void serverStarting() {
		List<IRecipe> crafting = new LinkedList<IRecipe>();
		List<ItemStack> smelting = new LinkedList<ItemStack>();

		for (XMLNode node : xmlNode.getNodes()) {
			ItemStack stack = XMLParser.parseItemStackNode(node, NodeType.N_A);
			for (IRecipe recipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList())
				if (recipe != null && areStacksTheSame(stack, recipe.getRecipeOutput()) && !CraftingRecipes.addedRecipes.contains(recipe))
					crafting.add(recipe);

			for (Entry<ItemStack, ItemStack> entry : (Set<Entry<ItemStack, ItemStack>>) FurnaceRecipes.smelting().getSmeltingList().entrySet())
				if (areStacksTheSame(stack, entry.getValue()) && !Smelting.addedInputs.contains(entry.getKey()))
					smelting.add(entry.getKey());
		}

		for (IRecipe recipe : crafting)
			CraftingManager.getInstance().getRecipeList().remove(recipe);
		for (ItemStack entry : smelting)
			FurnaceRecipes.smelting().getSmeltingList().remove(entry);
	}

	private boolean areStacksTheSame(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null)
			return false;

		if (stack1.getItem() == stack2.getItem())
			if (stack1.getItemDamage() == stack2.getItemDamage() || isWildcard(stack1.getItemDamage()) || isWildcard(stack2.getItemDamage())) {
				if (stack1.hasTagCompound() && stack2.hasTagCompound())
					return stack1.getTagCompound().equals(stack2.getTagCompound());
				return stack1.hasTagCompound() == stack2.hasTagCompound();
			}
		return false;
	}

	private boolean isWildcard(int meta) {
		return meta == OreDictionary.WILDCARD_VALUE;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}