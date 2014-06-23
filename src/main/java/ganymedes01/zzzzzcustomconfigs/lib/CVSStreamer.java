package ganymedes01.zzzzzcustomconfigs.lib;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CVSStreamer {

	private int index = 0;
	private final String[] data;

	public CVSStreamer(String string) {
		data = string.split(",");

		for (int i = 0; i < data.length; i++)
			data[i] = data[i].trim();
	}

	public CVSStreamer(String[] string) {
		data = string;

		for (int i = 0; i < data.length; i++)
			data[i] = data[i].trim();
	}

	public ItemStack getItemStack() {
		Item item = (Item) Item.itemRegistry.getObject(data[index]);
		index++;
		int size = Integer.parseInt(data[index]);
		index++;
		int meta = Integer.parseInt(data[index]);
		index++;

		return new ItemStack(item, size, meta);
	}

	public int getInteger() {
		int integer = Integer.parseInt(data[index]);
		index++;
		return integer;
	}

	public double getDouble() {
		double d = Double.parseDouble(data[index]);
		index++;
		return d;
	}

	public Object getObject(Class<?> type) {
		if (type == ItemStack.class)
			return getItemStack();
		else if (type == int.class || type == Integer.class)
			return getInteger();
		else if (type == double.class || type == Double.class)
			return getDouble();

		throw new RuntimeException("Illegal type:" + type);
	}
}