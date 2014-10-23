package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.Loader;

public class IndustrialCraft2 extends ConfigFile {

	public IndustrialCraft2() {
		super("IC2", "");
	}

	@Override
	public void preInit() {
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
				IRecipeInput input = getInput(node);
				int minHeat = Integer.parseInt(node.getNode("minHeat").getValue());
				List<ItemStack> outputs = new LinkedList<ItemStack>();
				for (int i = 0; i < 3; i++) {
					XMLNode n = node.getNode("output" + (i + 1));
					if (n != null)
						outputs.add((ItemStack) XMLHelper.processEntry(n, ItemStack.class));
				}
				addThermalCentrifugeRecipe(input, minHeat, outputs.toArray(new ItemStack[0]));
			} else if (name.equals("blockcutter"))
				addCutterRecipe(getInput(node), Integer.parseInt(node.getNode("cutterLevel").getValue()), (ItemStack) XMLHelper.processEntry(node.getNode("output"), ItemStack.class));
			else if (name.equals("blastfurance")) {
				IRecipeInput input = getInput(node);
				List<ItemStack> outputs = new LinkedList<ItemStack>();
				for (int i = 0; i < 2; i++) {
					XMLNode n = node.getNode("output" + (i + 1));
					if (n != null)
						outputs.add((ItemStack) XMLHelper.processEntry(n, ItemStack.class));
				}
				Recipes.blastfurance.addRecipe(input, null, outputs.toArray(new ItemStack[0]));
			} else if (name.equals("metalformerExtruding"))
				addBasic(Recipes.metalformerExtruding, node);
			else if (name.equals("metalformerCutting"))
				addBasic(Recipes.metalformerCutting, node);
			else if (name.equals("metalformerRolling"))
				addBasic(Recipes.metalformerRolling, node);
			else if (name.equals("oreWashing")) {
				IRecipeInput input = getInput(node);
				List<ItemStack> outputs = new LinkedList<ItemStack>();
				for (int i = 0; i < 3; i++) {
					XMLNode n = node.getNode("output" + (i + 1));
					if (n != null)
						outputs.add((ItemStack) XMLHelper.processEntry(n, ItemStack.class));
				}
				addOreWashingRecipe(input, outputs.toArray(new ItemStack[0]));
			} else if (name.equals("cannerBottle")) {
				IRecipeInput input1 = this.getInput(XMLHelper.processEntry(node.getNode("input1"), ItemStack.class));
				IRecipeInput input2 = this.getInput(XMLHelper.processEntry(node.getNode("input2"), ItemStack.class));
				Recipes.cannerBottle.addRecipe(input1, input2, (ItemStack) XMLHelper.processEntry(node.getNode("output"), ItemStack.class));
			} else if (name.equals("cannerEnrich")) {
				FluidStack input = (FluidStack) XMLHelper.processEntry(node.getNode("input"), ItemStack.class);
				FluidStack output = (FluidStack) XMLHelper.processEntry(node.getNode("output"), ItemStack.class);
				IRecipeInput additive = this.getInput(XMLHelper.processEntry(node.getNode("additive"), ItemStack.class));
				Recipes.cannerEnrich.addRecipe(input, additive, output);
			}
		}
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("IC2");
	}

	private void addBasic(IMachineRecipeManager machine, XMLNode node) {
		machine.addRecipe(getInput(node), null, (ItemStack) XMLHelper.processEntry(node.getNode("output"), ItemStack.class));
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
		return getInput(XMLHelper.processEntry(node.getNode("input"), ItemStack.class));
	}

	private IRecipeInput getInput(Object obj) {
		if (obj instanceof ItemStack)
			return new RecipeInputItemStack((ItemStack) obj);
		else if (obj instanceof String) {
			String str = (String) obj;
			String[] split = str.split(" ");
			if (split.length > 1)
				return new RecipeInputOreDict(split[0], Integer.parseInt(split[1]));
			else
				return new RecipeInputOreDict(str);
		} else
			throw new RuntimeException("Invali type, must be a ItemStack or a String");
	}
}