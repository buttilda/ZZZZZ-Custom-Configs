package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.ZZZZZCustomConfigs;
import ganymedes01.zzzzzcustomconfigs.registers.BlacklistedEntities;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void spawnEvent(EntityJoinWorldEvent event) {
		String name = EntityList.getEntityString(event.entity);
		if (name != null)
			if (BlacklistedEntities.entityBlacklist.contains(name.toLowerCase()))
				event.setCanceled(true);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void tooltip(ItemTooltipEvent event) {
		if (ZZZZZCustomConfigs.showTooltips) {
			String string = EnumChatFormatting.DARK_GREEN + Item.itemRegistry.getNameForObject(event.itemStack.getItem());
			if (!event.toolTip.contains(string))
				event.toolTip.add(string);
		}
	}
}