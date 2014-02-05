package ganymedes01.zzzzzcustomconfigs.registers;

import java.util.logging.Level;
import java.util.logging.Logger;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TC4AspectsRegister {

	public static void registerAspects(Logger logger, String line) {
		String[] data = line.split("=");

		String[] stack = data[0].trim().split(":");
		String aspects = data[1].trim();

		ThaumcraftApi.registerObjectTag(getInt(stack[0]), getInt(stack[1]), getAspects(aspects));
		logger.log(Level.INFO, "Registered aspects for " + stack[0]);
	}

	private static AspectList getAspects(String line) {
		AspectList list = new AspectList();
		String[] data = line.split(",");

		for (String s : data) {
			String[] asp = s.split(":");
			list.add(Aspect.getAspect(asp[0].toLowerCase().trim()), getInt(asp[1]));
		}

		return list;
	}

	private static int getInt(String s) {
		return Integer.parseInt(s.trim());
	}
}