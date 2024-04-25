package net.A1exPrdgc.biotechmod.block;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.item.ModItemGroup;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS
			= DeferredRegister.create(ForgeRegistries.BLOCKS, BiotechMod.MOD_ID);

	public static final RegistryObject<Block> ROOT = registerBlock("root_block",
			() -> new Block(AbstractBlock.Properties.create(Material.WOOD)
					.harvestLevel(1).harvestTool(ToolType.AXE).setRequiresTool().hardnessAndResistance(3f)));

	private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block)
	{
		RegistryObject<T> toReturn = BLOCKS.register(name, block);

		registerBlockItem(name, toReturn);

		return toReturn;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block)
	{
		ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().group(ModItemGroup.BIOTECHMOD_GROUP)));
	}

	public static void register(IEventBus eventBus)
	{
		BLOCKS.register(eventBus);
	}
}
