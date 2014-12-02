package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class Smelting extends ConfigFile {

	public static List<ItemStack> addedInputs = new LinkedList<ItemStack>();
	private static String header = "Examples:\n\n";
	static {
		header += "The following shows a recipe where you'd smelt a diamond sword and get 2 diamonds.\n";
		header += "The xp parameter determines how much xp the player is given when picking the output up. IT MUSTN'T BE BIGGER THAN 1 OR SMALLER THAN 0!\n";

		XMLBuilder builder = new XMLBuilder("recipe");
		builder.makeEntry("output", new ItemStack(Items.diamond, 2));
		builder.makeEntry("input", new ItemStack(Items.diamond_sword));
		builder.makeEntry("xp", 1.0F);
		header += builder.toString();
	}

	public Smelting() {
		super("Smelting", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("recipe")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				float xp = (float) Math.min(1.0, Math.max(0, Float.parseFloat(node.getNode("xp").getValue())));

				GameRegistry.addSmelting(input, output, xp);
				addedInputs.add(input);
			}
	}

	@Override
	public void preInit() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}