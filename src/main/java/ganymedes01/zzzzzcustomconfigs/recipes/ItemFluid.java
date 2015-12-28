package ganymedes01.zzzzzcustomconfigs.recipes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.zzzzzcustomconfigs.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

public class ItemFluid extends Item {

	public static final ItemFluid instance = new ItemFluid();

	public ItemFluid() {
		setMaxStackSize(1);
		setUnlocalizedName(Reference.MOD_ID + ".item_fluid");
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		FluidStack fluid = getFluidStack(stack);
		return fluid.getLocalizedName() + " (" + fluid.amount + " mB)";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack) {
		FluidStack fluid = getFluidStack(stack);
		return fluid.getFluid().getIcon(fluid);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		FluidStack fluid = getFluidStack(stack);
		return fluid.getFluid().getColor(fluid);
	}

	public static FluidStack getFluidStack(ItemStack stack) {
		if (stack.hasTagCompound())
			return FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));
		return null;
	}

	public static ItemStack setFluid(ItemStack stack, FluidStack fluid) {
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("Fluid", fluid.writeToNBT(new NBTTagCompound()));
		return stack;
	}
}