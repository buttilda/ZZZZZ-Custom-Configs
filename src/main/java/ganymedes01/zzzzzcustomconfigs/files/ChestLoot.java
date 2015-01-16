package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class ChestLoot extends ConfigFile {

	private static String header = "Chest loot types:\n\n";
	static {
		try {
			Field f = ChestGenHooks.class.getDeclaredField("chestInfo");
			f.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashMap<String, ChestGenHooks> chestInfo = (HashMap<String, ChestGenHooks>) f.get(null);
			for (String str : chestInfo.keySet())
				header += str + "\n";
		} catch (Exception e) {
			e.printStackTrace();
		}

		header += "\nExamples:\n\n";
		header += "The following adds Diamond Horse Armour as a loot that can be found on bonus chests\n";
		header += "You can replace \"bonusChest\" with any of the values listed above.\n";
		header += "The weight value is how often this loot will be found. The greater the value the more common it will be\n";
		header += "The minAmount and maxAmount values are the amount of this loot that can be found in a chest.\n";
		header += "In the example below, if the item is picked to be put in a chest, that chest will have at least 3 of that item but 5 at most.\n\n";

		XMLBuilder builder = new XMLBuilder("bonusChest");
		builder.makeEntry("loot", new ItemStack(Items.diamond_horse_armor));
		builder.makeEntry("weight", 5);
		builder.makeEntry("minAmount", 3);
		builder.makeEntry("maxAmount", 5);
		header += builder.toString() + "\n";
	}

	public ChestLoot() {
		super("ChestLoot", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes()) {
			ItemStack stack = XMLParser.parseItemStackNode(node.getNode("loot"), NodeType.N_A);
			int weight = Integer.parseInt(node.getNode("weight").getValue());
			int min = Integer.parseInt(node.getNode("minAmount").getValue());
			int max = Integer.parseInt(node.getNode("maxAmount").getValue());

			ChestGenHooks.addItem(node.getName(), new WeightedRandomChestContent(stack, min, max, weight));
		}
	}

	@Override
	public void postInit() {
	}

	@Override
	public void serverStarting() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}