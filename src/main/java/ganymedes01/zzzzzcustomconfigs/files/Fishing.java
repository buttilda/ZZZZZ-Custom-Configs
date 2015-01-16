package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser.NodeType;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;

public class Fishing extends ConfigFile {

	public static final List<IRecipe> addedRecipes = new LinkedList<IRecipe>();

	private static String header = "Examples:\n\n";
	static {
		header += "This file allows adding items that can be fished with the vanilla fishing rod (whether or not it works with modded fishing rods is up to the modder who made them)\n";
		header += "The higher the \"chance\" is the more likely it is that the item will be fished!\n";
		header += "The are three types of loot that can be obtained from fishing: fish, junk and treasure\n\n";

		XMLBuilder builder = new XMLBuilder("fish");
		builder.makeEntry("fishable", new ItemStack(Items.fish));
		builder.makeEntry("chance", 60);
		header += "The following adds the vanilla fish with it's default chance of 60\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("fish");
		builder.makeEntry("fishable", new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()));
		builder.makeEntry("chance", 13);
		header += "The following adds the pufferfish with it's default chance of 13\n";
		header += builder.toString() + "\n\n";

		header += "It's also possible to add damagable items and enchantable items!\n";
		builder = new XMLBuilder("treasure");
		builder.makeEntry("fishable", new ItemStack(Items.diamond_hoe));
		builder.makeEntry("chance", 5);
		builder.makeEntry("damage", 0.9F);
		header += "The following adds a diamond hoe that can have up to 90% of it's durability taken. It will NOT be enchanted!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("treasure");
		builder.makeEntry("fishable", new ItemStack(Items.golden_hoe));
		builder.makeEntry("chance", 5);
		builder.makeEntry("damage", 0.5F);
		builder.makeEntry("enchantable", true);
		header += "The following adds a golden hoe that can have up to 50% of it's durability taken. It will always be enchanted with up to level 30 enchants (there is no way to change the enchants level)!\n";
		header += builder.toString() + "\n\n";

		header += "It's also possible to add items with NBT tags!\n";
		builder = new XMLBuilder("junk");
		ItemStack stack = new ItemStack(Items.enchanted_book);
		Items.enchanted_book.addEnchantment(stack, new EnchantmentData(Enchantment.baneOfArthropods, 5));
		builder.makeEntry("fishable", stack);
		builder.makeEntry("chance", 100);
		header += "The following adds a enchanted book with Bane of Arthropods V\n";
		header += builder.toString();
	}

	public Fishing() {
		super("Fishing", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes()) {
			ItemStack fishable = XMLParser.parseItemStackNode(node.getNode("fishable"), NodeType.OUTPUT);
			int change = Integer.parseInt(node.getNode("chance").getValue());

			WeightedRandomFishable randFish = new WeightedRandomFishable(fishable, change);

			XMLNode n = node.getNode("damage");
			if (n != null)
				randFish.func_150709_a(Float.parseFloat(n.getValue()));
			n = node.getNode("enchantable");
			if (n != null && Boolean.parseBoolean(n.getValue()))
				randFish.func_150707_a();

			if (node.getName().equals("fish"))
				FishingHooks.addFish(randFish);
			else if (node.getName().equals("junk"))
				FishingHooks.addJunk(randFish);
			else if (node.getName().equals("treasure"))
				FishingHooks.addTreasure(randFish);
			else
				throw new IllegalArgumentException("Invalid fishable type: " + node.getName());
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