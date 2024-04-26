package net.A1exPrdgc.biotechmod.item;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.item.custom.FireBottle;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, BiotechMod.MOD_ID);

	public static final RegistryObject<Item> COMPOSITE = ITEMS.register("composite",
			() -> new Item(new Item.Properties().group(ModItemGroup.BIOTECHMOD_GROUP)));

	public static final RegistryObject<Item> ASH = ITEMS.register("ash",
			() -> new Item(new Item.Properties().group(ModItemGroup.BIOTECHMOD_GROUP)));

	public static final RegistryObject<Item> FIRE_BOTTLE = ITEMS.register("fire_bottle",
			() -> new FireBottle(new Item.Properties().group(ModItemGroup.BIOTECHMOD_GROUP).maxDamage(12)));

	public static final RegistryObject<Item> ROOT = ITEMS.register("root",
			() -> new Item(new Item.Properties().group(ModItemGroup.BIOTECHMOD_GROUP)
					.food(new Food.Builder().hunger(5).saturation(.2f)
							.effect(() -> new EffectInstance(Effects.POISON, 100, 1), 0.75f)
							.effect(() -> new EffectInstance(Effects.NAUSEA, 300, 2), 0.75f).build())));



	public static void register(IEventBus eventBus)
	{
		ITEMS.register(eventBus);
	}



}
