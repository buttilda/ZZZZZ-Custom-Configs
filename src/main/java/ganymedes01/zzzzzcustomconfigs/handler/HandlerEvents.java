package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.ZZZZZCustomConfigs;
import ganymedes01.zzzzzcustomconfigs.files.EntityDrops;
import ganymedes01.zzzzzcustomconfigs.files.OreSpawn;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
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

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPostOreGen(OreGenEvent.Post event) {
		if (!event.isCanceled())
			OreSpawn.onPostOreGen(event.world, event.rand, event.worldX, event.worldZ);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onOreGen(OreGenEvent.GenerateMinable event) {
		if (!event.isCanceled())
			OreSpawn.onOreGen(event);
	}
}