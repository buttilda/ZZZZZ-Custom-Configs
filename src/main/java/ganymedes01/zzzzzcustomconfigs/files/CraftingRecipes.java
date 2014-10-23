package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingRecipes extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		header += "<shapeless>\n";
		header += "\t<output>minecraft:iron_ingot 3 0</output>\n";
		header += "\t<input1>minecraft:iron_bars 1 0</input1>\n";
		header += "\t<input2>\"gemDiamond\"</input2>\n";
		header += "\t<input3>minecraft:skull 1 3 {SkullOwner:\"Notch\"}</input3>\n";
		header += "</shapeless>\n";
		header += "\n";
		header += "<shaped>\n";
		header += "\t<output>minecraft:diamond_sword 1 0</output>\n";
		header += "\t<row1>\"x z\"</row1>\n";
		header += "\t<row2>\" x \"</row2>\n";
		header += "\t<row3>\" y \"</row3>\n";
		header += "\t<x>\"stickWood\"</x>\n";
		header += "\t<y>minecraft:paper 1 0</y>\n";
		header += "\t<z>minecraft:skull 1 3 {SkullOwner:\"Notch\"}</z>\n";
		header += "</shaped>";
	}

	public CraftingRecipes() {
		super("CraftingRecipes", header);
	}

	@Override
	public void preInit() {
		for (XMLNode node : xmlNode.getNodes()) {
			ItemStack output = (ItemStack) XMLHelper.processEntry(node.getNode("output"), ItemStack.class);
			if (node.getName().equals("shaped")) {
				List<Object> data = new ArrayList<Object>();
				String types = "";
				for (int i = 0; i < 3; i++) {
					XMLNode n = node.getNode("row" + (i + 1));
					if (n != null) {
						Object obj = XMLHelper.processEntry(n, String.class);
						types += obj.toString().replace(" ", "");
						data.add(obj);
					}
				}

				for (char c : types.toCharArray()) {
					data.add(c);
					data.add(XMLHelper.processEntry(node.getNode(Character.toString(c)), String.class));
				}
				GameRegistry.addRecipe(new ShapedOreRecipe(output, data.toArray()));
			} else if (node.getName().equals("shapeless")) {
				List<Object> data = new ArrayList<Object>();
				for (int i = 0; i < 9; i++) {
					XMLNode n = node.getNode("input" + (i + 1));
					if (n != null)
						data.add(XMLHelper.processEntry(n, ItemStack.class));
					else
						break;
				}
				GameRegistry.addRecipe(new ShapelessOreRecipe(output, data.toArray()));
			} else
				throw new RuntimeException("Invalid recipe name: " + node.getName());
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