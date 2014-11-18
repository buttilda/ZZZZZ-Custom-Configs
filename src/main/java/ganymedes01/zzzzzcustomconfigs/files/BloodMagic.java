package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.items.ShapelessBloodOrbRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class BloodMagic extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {

	}

	public BloodMagic() {
		super("BloodMagic", header);
	}

	@Override
	public void preInit() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("altar")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				int tier = Integer.parseInt(node.getNode("tier").getValue());
				int bloodAmount = Integer.parseInt(node.getNode("bloodAmount").getValue());
				int consumptionRate = Integer.parseInt(node.getNode("consumptionRate").getValue());
				int drainRate = Integer.parseInt(node.getNode("drainRate").getValue());

				AltarRecipeRegistry.registerAltarRecipe(output, input, tier, bloodAmount, consumptionRate, drainRate, false);
			} else if (node.getName().equals("bloodorb")) {
				String prop = node.getProperty("type");
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				if (prop.equals("shaped")) {
					List<Object> data = new ArrayList<Object>();
					String types = "";
					for (int i = 0; i < 3; i++) {
						XMLNode n = node.getNode("row" + (i + 1));
						if (n != null) {
							Object obj = XMLParser.parseNode(n);
							types += obj.toString().replace(" ", "");
							data.add(obj);
						}
					}

					for (char c : types.toCharArray()) {
						data.add(c);
						data.add(XMLParser.parseNode(node.getNode(Character.toString(c))));
					}

					addRecipe(new ShapedBloodOrbRecipe(output, data.toArray()));
				} else if (prop.equals("shapeless")) {
					List<Object> data = new ArrayList<Object>();
					for (int i = 0; i < 9; i++) {
						XMLNode n = node.getNode("input" + (i + 1));
						if (n != null)
							data.add(XMLParser.parseNode(n));
						else
							break;
					}

					addRecipe(new ShapelessBloodOrbRecipe(output, data.toArray()));
				}
			} else if (node.getName().equals("binding")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));

				BindingRegistry.registerRecipe(output, input);
			} else if (node.getName().equals("alchemy")) {
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				int time = Integer.parseInt(node.getNode("time").getValue());
				ItemStack[] recipe = new ItemStack[5];
				int bloodOrbLevel = Integer.parseInt(node.getNode("bloodOrbLevel").getValue());
				for (int i = 0; i < 5; i++) {
					XMLNode n = node.getNode("input" + (i + 1));
					if (n != null)
						recipe[i] = XMLParser.parseItemStackNode(n);
				}

				AlchemyRecipeRegistry.registerRecipe(output, time, recipe, bloodOrbLevel);
			}
	}

	private void addRecipe(IRecipe recipe) {
		CraftingRecipes.addedRecipes.add(recipe);
		GameRegistry.addRecipe(recipe);
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("AWWayofTime");
	}
}