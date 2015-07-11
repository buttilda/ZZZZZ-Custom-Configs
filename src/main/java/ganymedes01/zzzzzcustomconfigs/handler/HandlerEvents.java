package ganymedes01.zzzzzcustomconfigs.handler;

import ganymedes01.zzzzzcustomconfigs.ZZZZZCustomConfigs;
import ganymedes01.zzzzzcustomconfigs.files.BlockDrop;
import ganymedes01.zzzzzcustomconfigs.files.EntityDrops;
import ganymedes01.zzzzzcustomconfigs.files.OreDict;
import ganymedes01.zzzzzcustomconfigs.files.OreGen;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerEvents {

	public static final HandlerEvents INSTANCE = new HandlerEvents();

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void tooltip(ItemTooltipEvent event) {
		if (ZZZZZCustomConfigs.showTooltips) {
			for (Integer oreID : OreDictionary.getOreIDs(event.itemStack))
				event.toolTip.add(EnumChatFormatting.DARK_GRAY + OreDictionary.getOreName(oreID));
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
			OreGen.onPostOreGen(event.world, event.rand, event.worldX, event.worldZ);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onOreGen(OreGenEvent.GenerateMinable event) {
		if (!event.isCanceled())
			OreGen.onOreGen(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onBlockHarvest(HarvestDropsEvent event) {
		if (!event.isCanceled())
			BlockDrop.onBlockHarvest(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onOreRegister(OreRegisterEvent event) {
		OreDict.onOreRegister(event);
	}
}