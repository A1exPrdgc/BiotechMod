package net.A1exPrdgc.biotechmod.item;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, BiotechMod.MOD_ID);

	public static final RegistryObject<Item> COMPOSITE = ITEMS.register("composite materials",
			() -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

	public static void Register(IEventBus eventBus)
	{
		ITEMS.register(eventBus);
	}



}
