package ganymedes01.zzzzzcustomconfigs.files;

import exnihilo.registries.CompostRegistry;
import exnihilo.registries.CrucibleRegistry;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.HeatRegistry;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.Color;
import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import ganymedes01.zzzzzcustomconfigs.xml.XMLParser;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.Loader;

public class ExNihilo extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		XMLBuilder builder = new XMLBuilder("compost");
		builder.makeEntry("input", new ItemStack(Items.carrot));
		builder.makeEntry("value", 0.08F);
		builder.makeEntry("colour", "0xFF9B0F");
		header += "The following shows an example of how to add an item to the compost.\n";
		header += "The \"value\" parameter represents how much of the cruicible will be filled by this item. 0.08 = 8%\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("sieve");
		builder.makeEntry("input", new ItemStack(Blocks.netherrack));
		builder.makeEntry("output", new ItemStack(Items.blaze_powder));
		builder.makeEntry("rarity", 150);
		header += "The following shows an example of how to add a sieving recipe.\n";
		header += "The \"input\" MUST be a block! It cannot contain an item!\n";
		header += "The \"rarity\" is the chance out the ouptut being yielded. 150 = 1 chance in 150\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("crucible");
		builder.makeEntry("input", new ItemStack(Blocks.packed_ice));
		builder.makeEntry("output", new FluidStack(FluidRegistry.WATER, 2000));
		header += "The following shows an example of how to add a cruicible recipe.\n";
		header += "The \"input\" MUST be a block! It cannot contain an item!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("hammer");
		builder.makeEntry("input", new ItemStack(Blocks.ice));
		builder.makeEntry("output", new ItemStack(Items.fish));
		builder.makeEntry("chance", 0.5F);
		builder.makeEntry("luckMultiplier", 0.05F);
		header += "The following shows an example of how to add a cruicible recipe.\n";
		header += "The \"chance\" is how often the item will be dropped. 0.5 = 50%\n";
		header += "The \"luckMultiplier\" will be mutiplied by the fortune of the hammer and then added to the chance. e.g: chance of 0.5, luckMultiplier of 0.25 and fortune 2 = 0.5 + (0.25 x 2) = 1 = 100%\n";
		header += "The \"input\" MUST be a block! It cannot contain an item!\n";
		header += builder.toString() + "\n\n";

		builder = new XMLBuilder("heat");
		builder.makeEntry("input", new ItemStack(Blocks.cake));
		builder.makeEntry("value", 0.5F);
		header += "The following shows an example of how to add a block that can be used to power a cruicible.\n";
		header += "As a reference for the \"value\" parameter, lava has a value of 0.2, fire is 0.3, a torch is 0.1.\n";
		header += "The \"input\" MUST be a block! It cannot contain an item!\n";
		header += builder.toString() + "\n\n";
	}

	public ExNihilo() {
		super("exnihilo", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes())
			if (node.getName().equals("compost")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				float value = Float.parseFloat(node.getNode("value").getValue());
				Color colour = new Color(node.getNode("colour").getValue());

				CompostRegistry.register(input.getItem(), input.getItemDamage(), value, colour);
			} else if (node.getName().equals("sieve")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				int rarity = Integer.parseInt(node.getNode("rarity").getValue());

				SieveRegistry.register(getBlock(input), input.getItemDamage(), output.getItem(), output.getItemDamage(), rarity);
			} else if (node.getName().equals("crucible")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				FluidStack output = XMLParser.parseFluidStackNode(node.getNode("output"));
				Block block = getBlock(input);

				CrucibleRegistry.register(block, input.getItemDamage(), 2000, output.getFluid(), output.amount, block);
			} else if (node.getName().equals("hammer")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				ItemStack output = XMLParser.parseItemStackNode(node.getNode("output"));
				float chance = Float.parseFloat(node.getNode("chance").getValue());
				float luckMultiplier = Float.parseFloat(node.getNode("luckMultiplier").getValue());

				HammerRegistry.register(getBlock(input), input.getItemDamage(), output.getItem(), output.getItemDamage(), chance, luckMultiplier);
			} else if (node.getName().equals("heat")) {
				ItemStack input = XMLParser.parseItemStackNode(node.getNode("input"));
				float value = Float.parseFloat(node.getNode("value").getValue());

				HeatRegistry.register(getBlock(input), input.getItemDamage(), value);
			} else
				throw new IllegalArgumentException("Invalid recipe name: " + node.getName());
	}

	private Block getBlock(ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		if (block == null || block == Blocks.air)
			throw new IllegalArgumentException("Stack must contain a block!: " + stack.getUnlocalizedName());
		return block;
	}

	@Override
	public void postInit() {
	}

	@Override
	public boolean isEnabled() {
		return Loader.isModLoaded("exnihilo");
	}
}