package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import cpw.mods.fml.common.Loader;

public class Botania extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		XMLBuilder builder = new XMLBuilder("petals");
		builder.makeEntry("output", new ItemStack(Items.poisonous_potato));
		builder.makeEntries("input", new Object[] { new ItemStack(Items.potato), "ingotGold" });
		header += "The number of inputs should not be larger than 16!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("runealtar");
		builder.makeEntry("output", new ItemStack(Items.diamond));
		builder.makeEntry("mana", 1000);
		builder.makeEntries("input", new Object[] { new ItemStack(Items.coal), new ItemStack(Items.coal, 1, 1), "gemEmerald" });
		header += "The number of inputs should not be larger than 16!\n";
		header += "The amount of mana should be smaller than 100,000!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("elventrade");
		builder.makeEntry("output", new ItemStack(Blocks.bedrock));
		builder.makeEntries("input", new Object[] { new ItemStack(Items.dye, 1, 4), "gemDiamond", "gemEmerald" });
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("manainfusion");
		builder.makeEntry("output", new ItemStack(Blocks.bedrock));
		builder.makeEntry("input", new ItemStack(Blocks.obsidian));
		builder.makeEntry("mana", 90000);
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("manainfusion");
		builder.makeEntry("output", new ItemStack(Blocks.bedrock));
		builder.makeEntry("input", new ItemStack(Blocks.obsidian));
		builder.makeEntry("mana", 90000);
		header += builder.toNode().addProperty("type", "conjuration").toString() + "\n\n";

		builder = new XMLBuilder("manainfusion");
		builder.makeEntry("output", new ItemStack(Blocks.bedrock));
		builder.makeEntry("input", new ItemStack(Blocks.obsidian));
		builder.makeEntry("mana", 90000);
		header += builder.toNode().addProperty("type", "alchemy").toString();
	}

	public Botania() {
		super("Botania", header);
	}

	@Override
	public void preInit() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("petals")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));

				BotaniaAPI.registerPetalRecipe(output, getArray(node, "input"));
			} else if (node.getName().equals("runealtar")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				int mana = Integer.parseInt(node.getNode("mana").getValue());

				BotaniaAPI.registerRuneAltarRecipe(output, mana, getArray(node, "input"));
			} else if (node.getName().equals("elventrade")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));

				BotaniaAPI.registerElvenTradeRecipe(output, getArray(node, "input"));
			} else if (node.getName().equals("manainfusion")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				Object input = XMLParser.parseNode(node.getNode("input"));
				int mana = Integer.parseInt(node.getNode("mana").getValue());

				String type = node.getProperty("type");
				if (type.equals("null"))
					BotaniaAPI.registerManaInfusionRecipe(output, input, mana);
				else if (type.equals("conjuration"))
					BotaniaAPI.registerManaConjurationRecipe(output, input, mana);
				else if (type.equals("alchemy"))
					BotaniaAPI.registerManaAlchemyRecipe(output, input, mana);
			}
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("Botania");
	}

	private Object[] getArray(XMLNode node, String name) {
		List<Object> inputs = new ArrayList<Object>();
		for (int i = 0; i < 16; i++) {
			XMLNode n = node.getNode(name + (i + 1));
			if (n != null)
				inputs.add(XMLParser.parseNode(n));
		}

		return inputs.toArray();
	}
}