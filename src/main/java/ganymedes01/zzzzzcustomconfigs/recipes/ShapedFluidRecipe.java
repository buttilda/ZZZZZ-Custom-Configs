package ganymedes01.zzzzzcustomconfigs.recipes;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedFluidRecipe extends ShapedOreRecipe {

	private final int width, height;
	private boolean mirrored;

	public ShapedFluidRecipe(ItemStack result, Object... recipe) {
		super(result, fixInput(recipe));

		width = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, this, "width");
		height = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, this, "height");
		mirrored = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, this, "mirrored");
	}

	private static Object[] fixInput(Object... inputs) {
		for (int i = 0; i < inputs.length; i++)
			if (inputs[i] instanceof FluidStack)
				inputs[i] = ItemFluid.setFluid(new ItemStack(ItemFluid.instance), (FluidStack) inputs[i]);
		return inputs;
	}

	@Override
	public ShapedOreRecipe setMirrored(boolean mirror) {
		ShapedOreRecipe result = super.setMirrored(mirror);
		mirrored = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, this, "mirrored");
		return result;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		for (int x = 0; x <= 3 - width; x++)
			for (int y = 0; y <= 3 - height; y++) {
				if (checkMatch(inv, x, y, false))
					return true;
				if (mirrored && checkMatch(inv, x, y, true))
					return true;
			}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		Object[] input = getInput();
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++) {
				int subX = x - startX;
				int subY = y - startY;
				Object target = null;

				if (subX >= 0 && subY >= 0 && subX < width && subY < height)
					if (mirror)
						target = input[width - subX - 1 + subY * width];
					else
						target = input[subX + subY * width];

				ItemStack slot = inv.getStackInRowAndColumn(x, y);

				if (target instanceof ItemStack) {
					Item item = ((ItemStack) target).getItem();
					if (slot != null && target != null && item instanceof ItemFluid) {
						FluidStack targetFluid = ItemFluid.getFluidStack((ItemStack) target);
						FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(slot);
						if (fluid == null && slot.getItem() instanceof IFluidContainerItem)
							fluid = ((IFluidContainerItem) slot.getItem()).getFluid(slot);
						if (fluid == null || !fluid.isFluidStackIdentical(targetFluid))
							return false;
					} else if (!OreDictionary.itemMatches((ItemStack) target, slot, false))
						return false;
				} else if (target instanceof ArrayList) {
					boolean matched = false;

					Iterator<ItemStack> itr = ((ArrayList<ItemStack>) target).iterator();
					while (itr.hasNext() && !matched)
						matched = OreDictionary.itemMatches(itr.next(), slot, false);

					if (!matched)
						return false;
				} else if (target == null && slot != null)
					return false;
			}

		return true;
	}
}