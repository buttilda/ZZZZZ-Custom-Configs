package ganymedes01.zzzzzcustomconfigs.lib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class StackUtils {

	public static boolean areStacksTheSame(ItemStack stack1, ItemStack stack2, boolean matchNBT) {
		if (stack1 == null || stack2 == null)
			return false;

		if (stack1.getItem() == stack2.getItem())
			if (stack1.getItemDamage() == stack2.getItemDamage() || isWildcard(stack1.getItemDamage()) || isWildcard(stack2.getItemDamage()))
				if (matchNBT) {
					if (stack1.hasTagCompound() && stack2.hasTagCompound())
						return stack1.getTagCompound().equals(stack2.getTagCompound());
					return stack1.hasTagCompound() == stack2.hasTagCompound();
				} else
					return true;
		return false;
	}

	private static boolean isWildcard(int meta) {
		return meta == OreDictionary.WILDCARD_VALUE;
	}
}