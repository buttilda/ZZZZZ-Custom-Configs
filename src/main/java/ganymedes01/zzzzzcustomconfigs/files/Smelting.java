package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class Smelting extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "<recipe>\n";
		header += "\t<output>minecraft:diamond 2 0</output>\n";
		header += "\t<input>minecraft:diamond_sword 1 0</input>\n";
		header += "\t<xp>1.0</xp>\n";
		header += "</recipe>";
	}

	public Smelting() {
		super("Smelting", header);
	}

	@Override
	public void preInit() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("recipe")) {
				ItemStack output = (ItemStack) XMLHelper.processEntry(node.getNode("output"), ItemStack.class);
				ItemStack input = (ItemStack) XMLHelper.processEntry(node.getNode("input"), ItemStack.class);
				float xp = Float.parseFloat(node.getNode("xp").getValue());

				GameRegistry.addSmelting(input, output, xp);
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
		return true;
	}
}