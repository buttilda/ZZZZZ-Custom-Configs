package ganymedes01.zzzzzcustomconfigs.recipes;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessFluidRecipe extends ShapelessOreRecipe {

	public ShapelessFluidRecipe(ItemStack result, Object... recipe) {
		super(result, fixInput(recipe));
	}

	private static Object[] fixInput(Object... inputs) {
		for (int i = 0; i < inputs.length; i++)
			if (inputs[i] instanceof FluidStack)
				inputs[i] = ItemFluid.setFluid(new ItemStack(ItemFluid.instance), (FluidStack) inputs[i]);
		return inputs;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean matches(InventoryCrafting var1, World world) {
		ArrayList<Object> required = new ArrayList<Object>(getInput());

		for (int x = 0; x < var1.getSizeInventory(); x++) {
			ItemStack slot = var1.getStackInSlot(x);

			if (slot != null) {
				boolean inRecipe = false;
				Iterator<Object> req = required.iterator();

				while (req.hasNext()) {
					boolean match = false;

					Object next = req.next();

					if (next instanceof ItemStack) {
						Item item = ((ItemStack) next).getItem();
						if (slot != null && next != null && item instanceof ItemFluid) {
							FluidStack targetFluid = ItemFluid.getFluidStack((ItemStack) next);
							FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(slot);
							if (fluid == null && slot.getItem() instanceof IFluidContainerItem)
								fluid = ((IFluidContainerItem) slot.getItem()).getFluid(slot);
							match = fluid != null && fluid.isFluidStackIdentical(targetFluid);
						} else
							match = OreDictionary.itemMatches((ItemStack) next, slot, false);
					} else if (next instanceof ArrayList) {
						Iterator<ItemStack> itr = ((ArrayList<ItemStack>) next).iterator();
						while (itr.hasNext() && !match)
							match = OreDictionary.itemMatches(itr.next(), slot, false);
					}

					if (match) {
						inRecipe = true;
						required.remove(next);
						break;
					}
				}

				if (!inRecipe)
					return false;
			}
		}

		return required.isEmpty();
	}
}