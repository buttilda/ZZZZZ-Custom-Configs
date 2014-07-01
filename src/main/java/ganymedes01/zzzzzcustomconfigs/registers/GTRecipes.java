package ganymedes01.zzzzzcustomconfigs.registers;

import ganymedes01.zzzzzcustomconfigs.lib.CVSStreamer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.minecraft.item.ItemStack;

public class GTRecipes {

	public static enum Recipe {
		fusion("aInput1, aInput2, aOutput1, aDuration, aEUt, aSpecialValue"),
		centrifuge("aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aDuration"),
		electrolyzer("aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aDuration, aEUt"),
		lathe("aInput1, aOutput1, aOutput2, aDuration, aEUt"),
		cutter("aInput1, aDuration, aOutput1, aEUt"),
		sawmill("aInput1, aInput2, aOutput1, aOutput2, aOutput3"),
		grinder("aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4"),
		distillation("aInput1, aCellAmount, aOutput1, aOutput2, aOutput3, aOutput4, aDuration, aEUt"),
		blast("aInput1, aInput2, aOutput1, aOutput2, aDuration, aEUt, aLevel"),
		implosion("aInput1, aInput2, aOutput1, aOutput2"),
		wiremill("aInput1, aEUt, aDuration, aOutput1"),
		bender("aEUt, aDuration, aInput1, aOutput1"),
		extruder("aEUt, aDuration, aInput1, aShape, aOutput1"),
		assembler("aInput1, aEUt, aInput2, aDuration, aOutput1"),
		alloySmelter("aInput1, aInput2, aEUt, aDuration, aOutput1"),
		canner("aInput1, aEUt, aInput2, aDuration, aOutput1, aOutput2"),
		vacuum("aInput1, aOutput1, aDuration"),
		chemical("aInput1, aInput2, aOutput1, aDuration");

		final String text;

		Recipe(String text) {
			this.text = text;
		}

		public String text() {
			return text;
		}
	}

	public static void registerRecipes(Logger logger, String line) {
		String[] data = line.split("=");
		CVSStreamer stream = new CVSStreamer(data[1]);

		for (Method method : GTRecipes.class.getMethods())
			if (method.getName().equalsIgnoreCase(data[0].trim() + "Recipe")) {
				Class<?>[] types = method.getParameterTypes();
				Object[] pars = new Object[types.length];
				for (int i = 0; i < pars.length; i++)
					pars[i] = stream.getObject(types[i]);

				try {
					method.invoke(null, pars);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		throw new RuntimeException("Recipe type not found: " + data[0]);
	}

	public static void fusionRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, int aSpecialValue) {
		getConstructor(aInput1, aInput2, aOutput1, aDuration, aEUt, aSpecialValue);
	}

	public static void centrifugeRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration) {
		getConstructor(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aDuration);
	}

	public static void electrolyserRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		getConstructor(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aDuration, aEUt);
	}

	public static void latheRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
		getConstructor(aInput1, aOutput1, aOutput2, aDuration, aEUt);
	}

	public static void cutterRecipe(ItemStack aInput1, int aDuration, ItemStack aOutput1, int aEUt) {
		getConstructor(aInput1, aDuration, aOutput1, aEUt);
	}

	public static void sawmillRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3) {
		getConstructor(aInput1, aInput2, aOutput1, aOutput2, aOutput3);
	}

	public static void grinderRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
		getConstructor(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4);
	}

	public static void distillationRecipe(ItemStack aInput1, int aCellAmount, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		getConstructor(aInput1, aCellAmount, aOutput1, aOutput2, aOutput3, aOutput4, aDuration, aEUt);
	}

	public static void blastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
		getConstructor(aInput1, aInput2, aOutput1, aOutput2, aDuration, aEUt, aLevel);
	}

	public static void implosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
		getConstructor(aInput1, aInput2, aOutput1, aOutput2);
	}

	public static void wiremillRecipe(ItemStack aInput1, int aEUt, int aDuration, ItemStack aOutput1) {
		getConstructor(aInput1, aEUt, aDuration, aOutput1);
	}

	public static void benderRecipes(int aEUt, int aDuration, ItemStack aInput1, ItemStack aOutput1) {
		getConstructor(aEUt, aDuration, aInput1, aOutput1);
	}

	public static void extruderRecipe(int aEUt, int aDuration, ItemStack aInput1, ItemStack aShape, ItemStack aOutput1) {
		getConstructor(aEUt, aDuration, aInput1, aShape, aOutput1);
	}

	public static void assemblerRecipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1) {
		getConstructor(aInput1, aEUt, aInput2, aDuration, aOutput1);
	}

	public static void alloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, int aEUt, int aDuration, ItemStack aOutput1) {
		getConstructor(aInput1, aInput2, aEUt, aDuration, aOutput1);
	}

	public static void cannerRecipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1, ItemStack aOutput2) {
		getConstructor(aInput1, aEUt, aInput2, aDuration, aOutput1, aOutput2);
	}

	public static void vacuumRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
		getConstructor(aInput1, aOutput1, aDuration);
	}

	public static void chemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration) {
		getConstructor(aInput1, aInput2, aOutput1, aDuration);
	}

	public static void getConstructor(Object... objs) {
		try {
			Class<?> cls = Class.forName("gregtech.api.util.GT_Recipe");

			Class<?>[] classes = new Class<?>[objs.length];
			for (int i = 0; i < objs.length; i++) {
				classes[i] = objs[i].getClass();
				if (classes[i] == Integer.class)
					classes[i] = int.class;
			}

			Constructor<?> cons = cls.getConstructor(classes);
			cons.newInstance(objs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}