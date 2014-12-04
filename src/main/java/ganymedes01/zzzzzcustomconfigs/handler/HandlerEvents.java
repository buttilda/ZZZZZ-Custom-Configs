package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.ZZZZZCustomConfigs;
import ganymedes01.zzzzzcustomconfigs.files.EntityDrops;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void tooltip(ItemTooltipEvent event) {
		if (ZZZZZCustomConfigs.showTooltips) {
			String string = EnumChatFormatting.DARK_GREEN + Item.itemRegistry.getNameForObject(event.itemStack.getItem());
			if (!event.toolTip.contains(string))
				event.toolTip.add(string);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		if (!event.isCanceled())
			EntityDrops.onDropEvent(event.entityLiving, event.lootingLevel, event.drops);
	}
}