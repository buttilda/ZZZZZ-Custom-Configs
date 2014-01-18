package ganymedes01.zzzzzcustomconfigs.registers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.entity.EntityList;

public class BlacklistedEntities {

	public static final ArrayList<String> entityBlacklist = new ArrayList<String>();

	public static void blacklistEntityFromLine(Logger logger, String line) {
		if (EntityList.stringToClassMapping.get(line) == null)
			logger.log(Level.SEVERE, "\tEntity " + line + " not found");
		else {
			entityBlacklist.add(line);
			logger.log(Level.INFO, "\tEntity " + line + " blacklisted");
		}
	}
}