package ganymedes01.zzzzzcustomconfigs.lib;

import ganymedes01.zzzzzcustomconfigs.xml.XMLHelper;
import ganymedes01.zzzzzcustomconfigs.xml.XMLNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigFile {

	private static File basePath;
	private static String baseHeader = "";
	static {
		baseHeader += "The syntax for an item stack is the same as you'd use in the /give command. NBT tags are supported! (e.g. minecraft:skull 1 3 {SkullOwner:\"ganymedes01\"})\n";
		baseHeader += "The syntax for a fluid stack is simiar to the item stack, it should be the fluid name followed by the amount. NBT tags are also supported! (e.g. water 1000)\n";
		baseHeader += "The syntax for ore dictionary values is the value between double-quotes. (e.g. \"ingotIron\")\n";
		baseHeader += "\n";
	}

	private final File configFile;
	private final String name, header;

	protected XMLNode xmlNode;

	public ConfigFile(String name, String header) {
		this.name = name;
		this.header = header;

		configFile = new File(basePath, name + ".xml");
	}

	public static void setPath(String path) {
		ConfigFile.basePath = new File(path + File.separator + Reference.MOD_ID + File.separator);
		ConfigFile.basePath.mkdirs();
	}

	public abstract void preInit();

	public abstract void init();

	public abstract void postInit();

	public abstract boolean isEnabled();

	public final void initFile() {
		try {
			if (!configFile.exists()) {
				BufferedWriter bw = XMLHelper.getWriter(configFile, name, baseHeader + header);
				bw.write("\t<!--Add your recipes here!-->");
				bw.newLine();
				bw.close();
				xmlNode = new XMLNode(name);
			} else {
				String file = XMLHelper.readFile(configFile);
				List<XMLNode> nodes = new ArrayList<XMLNode>();
				try {
					XMLHelper.getNodes(file, nodes);
				} catch (Exception e) {
					throw new RuntimeException("Error thrown when reading the file " + configFile);
				}

				xmlNode = nodes.get(0);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}