package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.registers.BlacklistedEntities;
import net.minecraft.entity.EntityList;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

public class HandlerEvents {

	@ForgeSubscribe
	public void entitySpawnEvent(CheckSpawn event) {
		if (BlacklistedEntities.entityBlacklist.contains(EntityList.getEntityString(event.entityLiving).toLowerCase()))
			event.setResult(Result.DENY);
	}
}