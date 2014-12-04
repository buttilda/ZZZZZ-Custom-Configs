package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class EntityDrops extends ConfigFile {

	private static String header = "Vanilla entities names:\n\n";
	static {
		header += "Creeper\n";
		header += "Skeleton\n";
		header += "Spider\n";
		header += "Zombie\n";
		header += "Slime\n";
		header += "Ghast\n";
		header += "PigZombie\n";
		header += "Enderman\n";
		header += "CaveSpider\n";
		header += "Silverfish\n";
		header += "Blaze\n";
		header += "LavaSlime\n";
		header += "EnderDragon\n";
		header += "WitherBoss\n";
		header += "Bat\n";
		header += "Witch\n";
		header += "Pig\n";
		header += "Sheep\n";
		header += "Cow\n";
		header += "Chicken\n";
		header += "Squid\n";
		header += "Wolf\n";
		header += "MushroomCow\n";
		header += "SnowMan\n";
		header += "Ozelot\n";
		header += "VillagerGolem\n";
		header += "EntityHorse\n";
		header += "Villager\n";
		header += "EnderCrystal\n";
		header += "\nModded entities ARE supported! Their names are usually in the form of modid.entityname. Examples: Natura.NitroCreeper, Thaumcraft.Pech\n";
		header += "CASE DOES MATTER! \"Creeper\" IS DIFFERENT THAN \"creeper\"!!!";

		XMLBuilder builder = new XMLBuilder("Creeper");
		builder.makeEntry("drop1", new ItemStack(Items.bone, 5)).addProperty("chance", "50");
		builder.makeEntry("drop2", new ItemStack(Items.cookie)).addProperty("chance", "100");
		header += "The following makes creepers drop up to 5 bones with a 50% chance, and drop up to 1 cookie with a 100% chance\n.";
		header += "The drop chance and amount WILL be influenced by the looting enchantment!\n";
		header += builder.toNode().addProperty("action", "add").toString() + "\n\n";

		builder = new XMLBuilder("Cow");
		builder.makeEntry("stack1", new ItemStack(Items.beef));
		builder.makeEntry("stack2", new ItemStack(Items.cooked_beef));
		builder.makeEntry("stack3", new ItemStack(Items.leather));
		header += "The following makes so that cows no longer drop beef, cooked beef or leather.\n";
		header += builder.toNode().addProperty("action", "remove").toString() + "\n";
	}

	public EntityDrops() {
		super("EntityDrops", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes()) {
			String type = node.getProperty("action");

			if (type.equals("add"))
				for (XMLNode n : node.getNodes()) {
					ItemStack stack = XMLParser.parseItemStackNode(n);
					int chance = Integer.parseInt(n.getProperty("chance"));
					addDrop(node.getName(), stack, chance);
				}
			else if (type.equals("remove"))
				for (XMLNode n : node.getNodes())
					addBan(node.getName(), XMLParser.parseItemStackNode(n));
			else
				throw new IllegalArgumentException("Invalid operation: " + node.getName());
		}
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private static Random rand = new Random();

	public static void onDropEvent(EntityLivingBase entity, int looting, List<EntityItem> items) {
		String name = (String) EntityList.classToStringMapping.get(entity.getClass());
		if (name == null)
			return;

		List<WeightedItemStack> droppables = drops.get(name);
		List<WeightedItemStack> banned = bans.get(name);

		if (items != null && !items.isEmpty() && banned != null && !banned.isEmpty())
			for (WeightedItemStack ban : banned) {
				List<EntityItem> remove = new LinkedList<EntityItem>();
				for (EntityItem item : items)
					if (areStacksTheSame(ban.stack, item.getEntityItem()))
						remove.add(item);
				for (EntityItem item : remove)
					items.remove(item);
			}

		if (droppables != null && !droppables.isEmpty())
			for (WeightedItemStack drop : droppables)
				if (drop.chance >= rand.nextFloat()) {
					int amount = rand.nextInt(drop.stack.stackSize) + rand.nextInt(1 + looting);
					for (int i = 0; i < amount; i++) {
						ItemStack copy = drop.stack.copy();
						copy.stackSize = 1;
						dropStack(copy, entity, items);
					}
				}
	}

	private static void dropStack(ItemStack stack, Entity entity, List<EntityItem> list) {
		if (stack.stackSize <= 0)
			return;

		EntityItem entityItem = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, stack);
		entityItem.delayBeforeCanPickup = 10;
		list.add(entityItem);
	}

	private static void addDrop(String entity, ItemStack stack, int chance) {
		addToMap(drops, entity, new WeightedItemStack(stack, chance));
	}

	private static void addBan(String entity, ItemStack stack) {
		addToMap(bans, entity, new WeightedItemStack(stack, 0));
	}

	private static <T, U> void addToMap(Map<T, List<U>> map, T key, U value) {
		if (map.containsKey(key))
			map.get(key).add(value);
		else {
			List<U> list = new LinkedList<U>();
			list.add(value);
			map.put(key, list);
		}
	}

	private static boolean areStacksTheSame(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null)
			return false;

		if (stack1.getItem() == stack2.getItem())
			return stack1.getItemDamage() == stack2.getItemDamage() || isWildcard(stack1.getItemDamage()) || isWildcard(stack2.getItemDamage());
		return false;
	}

	private static boolean isWildcard(int meta) {
		return meta == OreDictionary.WILDCARD_VALUE;
	}

	private static Map<String, List<WeightedItemStack>> drops = new HashMap<String, List<WeightedItemStack>>();
	private static Map<String, List<WeightedItemStack>> bans = new HashMap<String, List<WeightedItemStack>>();

	private static class WeightedItemStack {

		final ItemStack stack;
		final float chance;

		WeightedItemStack(ItemStack stack, int chance) {
			this.stack = stack;
			this.chance = Math.min(100, Math.max(0, chance)) / 100F;
		}
	}
}