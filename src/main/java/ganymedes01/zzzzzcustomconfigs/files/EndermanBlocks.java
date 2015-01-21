package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;
import ganymedes01.zzzzzcustomconfigs.xml.XMLBuilder;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;

public class EndermanBlocks extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {
		XMLBuilder builder = new XMLBuilder("EndermanBlocks");
		builder.makeEntry("entry1", Blocks.crafting_table).addProperty("canCarry", "true");
		builder.makeEntry("entry2", Blocks.grass).addProperty("canCarry", "false");
		builder.makeEntry("entry3", Blocks.bedrock).addProperty("canCarry", "true");
		builder.makeEntry("entry4", Blocks.planks).addProperty("canCarry", "true");
		header += builder.toString();
	}

	public EndermanBlocks() {
		super("EndermanBlocks", header);
	}

	@Override
	public void init() {
		for (XMLNode node : xmlNode.getNodes()) {
			Block block = (Block) Block.blockRegistry.getObject(node.getValue());
			boolean canCarry = Boolean.parseBoolean(node.getProperty("canCarry"));
			EntityEnderman.setCarriable(block, canCarry);
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