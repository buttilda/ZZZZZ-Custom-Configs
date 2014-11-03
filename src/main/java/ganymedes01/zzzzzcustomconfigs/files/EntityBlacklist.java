package ganymedes01.zzzzzcustomconfigs.files;

import ganymedes01.zzzzzcustomconfigs.lib.ConfigFile;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityBlacklist extends ConfigFile {

	private static String header = "Examples:\n\n";
	static {

	}

	public EntityBlacklist() {
		super("EntityBlacklist", header);
	}

	public static final ArrayList<String> entityBlacklist = new ArrayList<String>();

	public static void blacklistEntityFromLine(Logger logger, String line) {
		entityBlacklist.add(line.toLowerCase());
		logger.log(Level.INFO, "\tEntity " + line + " blacklisted");
	}

	@Override
	public void preInit() {
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