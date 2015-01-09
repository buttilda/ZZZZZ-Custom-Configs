package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.Reference;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;

import java.lang.reflect.Method;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.Loader;

public class RotaryCraft extends CraftingRecipes {

	private static String header = "Examples:\n\n";
	static {
		header += "This file allows adding recipes to the RotaryCraft Worktable. Adding recipes with RotaryCraft items as outputs is NOT ALLOWED! You will be able to add recipes that use those items as inputs.";
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
		ItemStack stack = new ItemStack(Items.skull);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("SkullOwner", "Notch");
		builder.makeEntry("z", stack);

		header += builder.toString();
	}

	public RotaryCraft() {
		super("RotaryCraft", header);
	}

	@Override
	protected void addRecipe(IRecipe recipe) {
		try {
			Class<?> WorktableAPI = Class.forName("Reika.RotaryCraft.API.WorktableAPI");
			Method addRecipe = WorktableAPI.getMethod("addRecipe", IRecipe.class);
			addRecipe.invoke(null, recipe);
		} catch (Exception e) {
			System.out.println("[" + Reference.MOD_ID + "] Error adding recipe to RotaryCraft's Worktable. Either your version of RotaryCraft is outdated, or I did something wrong.");
		}
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("RotaryCraft");
	}
}